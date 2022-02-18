package me.xuanming.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.xuanming.utils.JsonUtil;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Null;

/**
 * @author :         xingxuanming
 * @version :        1.0
 * @Description:
 * @Telephone :      15135964789
 * @createDate :     2022/2/15 11:23 上午
 * @updateUser :     Mingxuan_x
 * @updateDate :     2022/2/15 11:23 上午
 * @updateRemark :   修改内容
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SsoEntity {

    @NotEmpty(groups = {getUserInfoGroup.class, checkLogin.class})
    private String userToken;

    @NotEmpty(groups = {getUserInfoGroup.class,checkLogin.class})
    private String userId;

    private String hostIp;

    private String targetUrl;


    /**
     * 用户session信息
     */
    private UserInfoDTO userInfoDTO;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserVaildInfo {
        @NotEmpty(groups = ssologin.class)
        private String userName;
        @NotEmpty(groups = ssologin.class)
        private String password;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserInfoDTO {
        private String userId;

        /**
         * 用户姓名
         */
        private String userName;
        /**
         * 性别
         */
        private String gender;

        /**
         * 头像地址
         */
        private String avatarPath;
        /**
         * 用户邮箱
         */
        private String email;

    }


    public interface getUserInfoGroup {

    }

    public interface checkTokenVaild {

    }


    public interface ssologin {

    }

    public interface checkLogin {

    }


}
