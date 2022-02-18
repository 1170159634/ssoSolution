package me.xuanming.utils;


/**
 * @author :         xingxuanming
 * @version :        1.0
 * @Description:
 * @Telephone :      15135964789
 * @createDate :     2022/2/14 4:47 下午
 * @updateUser :     xingxuanmming
 * @updateDate :     2022/2/14 4:47 下午
 * @updateRemark :   修改内容
 **/
public enum HttpStatusEnum {

    USER_LOGIN_CHECK_SUCCESS(10000, "用户校验成功"),

    USER_LOGIN_CHECK_FAIL(10001, "用户校验失败,清重新登录"),

    USER_SERVICE_ERROR(10002, "用户服务异常"),

    UNKNOWN_ERROR(10003, "未知异常"),

    VAILD_FIELD_ERROR(10004, "字段校验错误");


    private Integer code;
    private String msg;

    HttpStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;

    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static String getEnumMessage(Integer value) {
        HttpStatusEnum[] crc = values();
        for (int i = 0; i < crc.length; ++i) {
            if (crc[i].getCode().equals(value)) {
                return crc[i].getMsg();
            }
        }
        return null;
    }

    public static Integer getEnumCode(String value) {
        HttpStatusEnum[] crc = values();
        for (int i = 0; i < crc.length; ++i) {
            if (crc[i].getMsg().equals(value)) {
                return crc[i].getCode();
            }
        }
        return null;
    }
}
