package me.xuanming.constant;


/**
 * @author :         xingxuanming
 * @version :        1.0
 * @Description: redis常量
 * @Telephone :      15135964789
 * @createDate :     2022/2/15 11:41 上午
 * @updateUser :     Mingxuan_x
 * @updateDate :     2022/2/15 11:41 上午
 * @updateRemark :   修改内容
 **/
public class RedisConstant {


    //设置过期时间30分钟
    public static final Integer TOKEN_EXPIR_TIME = 30;


    //设置redis key默认超时时间为30分钟
    public static final Integer REDIS_KEY_DEFAULT_EXPIR_TIME = 30;

    public static final String SSO_USER_EXPIR = "sso:user:expir:";


}
