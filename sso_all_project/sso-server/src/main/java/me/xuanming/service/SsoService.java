package me.xuanming.service;

import lombok.extern.slf4j.Slf4j;
import me.xuanming.config.SsoLoginConfig;
import me.xuanming.constant.RedisConstant;
import me.xuanming.pojo.SsoEntity;
import me.xuanming.utils.HttpClientUtils;
import me.xuanming.utils.HttpStatusEnum;
import me.xuanming.utils.JsonUtil;
import me.xuanming.utils.R;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author :         xingxuanming
 * @version :        1.0
 * @Description:
 * @Telephone :      15135964789
 * @createDate :     2022/2/15 11:33 上午
 * @updateUser :     Mingxuan_x
 * @updateDate :     2022/2/15 11:33 上午
 * @updateRemark :   修改内容
 **/
@Service
@Slf4j
public class SsoService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private SsoLoginConfig ssoLoginConfig;

    /**
     * 获取用户信息 仅判断token是否存在
     *
     * @Author: xingxuanming
     * @Date: 2022/2/15
     */
    public R getUserInfoByToken(SsoEntity ssoEntity) {
        //1.首先判断用户key是否存在于redis中
        String userInfo = stringRedisTemplate.opsForValue().get(RedisConstant.SSO_USER_EXPIR + ssoEntity.getUserToken());
        if (StringUtils.isNotEmpty(userInfo)) {
            SsoEntity data = JsonUtil.jsonToPojo(userInfo, SsoEntity.class);
            //2.其次判断存储的token是否与用户传来的一致
            if (!data.getUserId().equals(ssoEntity.getUserId())) {
                return R.loginFail();
            }
            return R.loginSuccess(data.getUserInfoDTO());
        }
        return R.loginFail();


    }


    /**
     * 用户登录
     *
     * @Author: xingxuanming
     * @Date: 2022/2/15
     */
    public R ssoLogin(SsoEntity.UserVaildInfo userVaildInfo) {
        LocalTime now = LocalTime.now();
        LocalTime requestTotalTime = now.plus(ssoLoginConfig.getRetryLoginTime(), ChronoUnit.MILLIS);
        while (now.isBefore(requestTotalTime)) {
            String requestResult = ssoLoginByHttp(JsonUtil.objectToJson(userVaildInfo));
            //1.如果成功调用用户服务 返回成功的用户信息
            if (StringUtils.isNotEmpty(requestResult)) {
                if (requestResult.contains((HttpStatusEnum.VAILD_FIELD_ERROR.getMsg()))) {
                    return R.fieldError();
                }
                if (requestResult.contains(HttpStatusEnum.USER_LOGIN_CHECK_FAIL.getMsg())) {
                    return R.loginFail();
                }
                if (requestResult.contains(HttpStatusEnum.USER_LOGIN_CHECK_SUCCESS.getMsg())) {
                    SsoEntity ssoEntity = new SsoEntity();
                    R r = JsonUtil.jsonToPojo(requestResult, R.class);
                    LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) r.getData();
                    SsoEntity.UserInfoDTO userInfoDTO = new SsoEntity.UserInfoDTO();
                    userInfoDTO.setUserId((String) data.get("userId"));
                    userInfoDTO.setUserName((String) data.get("userName"));
                    userInfoDTO.setAvatarPath((String) data.get("avatarPath"));
                    userInfoDTO.setGender((String) data.get("gender"));
                    userInfoDTO.setEmail((String) data.get("email"));

                    ssoEntity.setUserInfoDTO(userInfoDTO);
                    ssoEntity.setUserId(userInfoDTO.getUserId());

                    //2.生成登录状态的token，保存到redis，返回给客户
                    String userToken = createSsoToken();
                    ssoEntity.setUserToken(userToken);
                    stringRedisTemplate.opsForValue().set(RedisConstant.SSO_USER_EXPIR + userToken, JsonUtil.objectToJson(ssoEntity), RedisConstant.TOKEN_EXPIR_TIME, TimeUnit.MINUTES);
                    log.info("userInfo insert redis success, userId:{} token:{} expir time:{}", userInfoDTO.getUserId(), ssoEntity.getUserToken(), RedisConstant.TOKEN_EXPIR_TIME);
                    return R.loginSuccess(ssoEntity);
                }
            }

            log.info("call user service fail wait {} time retry", ssoLoginConfig.getRetryLoginPollingTime());
            try {
                TimeUnit.MILLISECONDS.sleep(ssoLoginConfig.getRetryLoginPollingTime());
            } catch (InterruptedException e) {
                log.error(" handle ssoLogin method error thread interrupt error msg:{}", e.getMessage());
            }
            now = LocalTime.now();

        }
        return R.unknown_error();


    }

    /**
     * 生成token算法，暂时使用uuid保存
     *
     * @Author: xingxuanming
     * @Date: 2022/2/15
     */
    private String createSsoToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }


    /**
     * 发起登录请求
     *
     * @return:
     * @Author: xingxuanming
     * @Date: 2022/2/15
     */
    private String ssoLoginByHttp(String info) {
        String url = ssoLoginConfig.getUserLoginUrl() + ssoLoginConfig.getUserLoginApiUrl();
        HashMap<String, String> headerMap = new HashMap<>(2);
        headerMap.put("accept", "application/json, text/plain, */*");
        headerMap.put("Content-Type", "application/json");
        try {
            String response = HttpClientUtils.post(url, info, headerMap, ssoLoginConfig.getUserLoginSynTime(), TimeUnit.SECONDS);
            //请求头不打印，保密用户信息，只输出返回的数据
            log.info(" send ssoLoginByHttp response, response {} ", response);
            return response;
        } catch (Exception e) {
            log.error(" send ssoLoginByHttp error, url {}  error {}", url, e.getMessage());
            return null;
        }

    }

    /**
     * 校验token是否生效
     *
     * @Author: xingxuanming
     * @Date: 2022/2/15
     */
    public R checkLogin(SsoEntity ssoEntity) {
        String userInfo = stringRedisTemplate.opsForValue().get(RedisConstant.SSO_USER_EXPIR + ssoEntity.getUserToken());
        if (StringUtils.isNotEmpty(userInfo)) {
            SsoEntity checkEntity = JsonUtil.jsonToPojo(userInfo, SsoEntity.class);
            if (ssoEntity.getUserId().equals(checkEntity.getUserId())) {
                return R.loginSuccess(null);
            }
        }
        return R.loginFail();

    }
}
