package me.xuanming.utils;

import java.util.HashMap;

/**
 * 返回数据
 *
 * @author xingxuanming
 */
public class R extends HashMap<String, Object> {

    private static final long serialVersionUID = 1L;


    public R() {
        put("code", 0);
        put("msg", "success");
    }

    public static R unknown_error() {
        return error(HttpStatusEnum.UNKNOWN_ERROR.getCode(), HttpStatusEnum.UNKNOWN_ERROR.getMsg());
    }

    public static R loginFail() {
        return error(HttpStatusEnum.USER_LOGIN_CHECK_FAIL.getCode(), HttpStatusEnum.USER_LOGIN_CHECK_FAIL.getMsg());
    }


    public static R fieldError() {
        return error(HttpStatusEnum.USER_LOGIN_CHECK_FAIL.getCode(), HttpStatusEnum.USER_LOGIN_CHECK_FAIL.getMsg());
    }
    public static R error(int code, String msg) {
        R r = new R();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }


    public static R loginSuccess(Object data) {
        R r = new R();
        r.put("msg", HttpStatusEnum.USER_LOGIN_CHECK_SUCCESS.getMsg());
        r.put("code", HttpStatusEnum.USER_LOGIN_CHECK_SUCCESS.getCode());
        r.put("data", data);
        return r;
    }


    public static R ok() {
        return new R();
    }

    @Override
    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public Integer getCode() {
        return (Integer) this.get("code");
    }

    public String getMsg() {
        return (String) this.get("msg");
    }

    public Object getData() {
        return (Object) this.get("data");
    }
}
