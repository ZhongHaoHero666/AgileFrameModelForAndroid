package com.android.szh.common.http;

import java.util.HashMap;


/**
 * Created by sunzhonghao on 2018/4/19.
 * desc:token 异常 code 此处需要和服务端约定
 */
public class TokenCode {

    /**
     * token已注销
     */
    public static final int LOGIN_TOKEN_LOGOUT = 1001;
    /**
     * token登录过期
     */
    public static final int LOGIN_TOKEN_OUTOFDATE = 1002;
    /**
     * token密码已修改
     */
    public static final int LOGIN_TOKEN_PWD_MODIF = 1003;
    /**
     * 账户禁用
     */
    public static final int LOGIN_TOKEN_ACCOUNT_FORBIDDEN = 1004;
    /**
     * 未查到相关token记录
     */
    public static final int LOGIN_TOKEN_NOT_FOUND = 1005;
    /**
     * 查询token信息出错
     */
    public static final int LOGIN_TOKEN_ERROR = 1006;
    /**
     * token验证不通过
     */
    public static final int LOGIN_TOKEN_NOT_IDENTIFY = 1007;

    /**
     * 未登录
     */
    public static final int LOGIN_UN_LOGIN = 1008;

    /**
     * token需要重新获取
     */
    public static final int LOGIN_TOKEN_NEED_REGET = 1009;

    /**
     * 用户信息更新时间限制
     */
    public static final int USER_UPDATE_LIMIT_ERROR = 1010;

    private static final HashMap<Integer, String> FAILURE_CODE = new HashMap<Integer, String>() {
        {
            put(LOGIN_TOKEN_LOGOUT, "token已注销");
            put(LOGIN_TOKEN_OUTOFDATE, "token登录过期");
            put(LOGIN_TOKEN_PWD_MODIF, "token密码已修改");
            put(LOGIN_TOKEN_ACCOUNT_FORBIDDEN, "账户禁用");
            put(LOGIN_TOKEN_NOT_FOUND, "未查到相关token记录");
            put(LOGIN_TOKEN_ERROR, "查询token信息出错");
            put(LOGIN_TOKEN_NOT_IDENTIFY, "token验证不通过");
            put(LOGIN_UN_LOGIN, "未登录");
            put(LOGIN_TOKEN_NEED_REGET, "token需要重新获取");
            put(USER_UPDATE_LIMIT_ERROR, "用户信息更新时间限制");
        }
    };

    /**
     * 获取Token校验失败描述
     */
    public static String getFailedDesc(int code) {
        return FAILURE_CODE.get(code);
    }

    /**
     * 判断Token是否校验失败
     */
    public static boolean isTokenCheckFailed(int code) {
        return FAILURE_CODE.containsKey(code);
    }

}
