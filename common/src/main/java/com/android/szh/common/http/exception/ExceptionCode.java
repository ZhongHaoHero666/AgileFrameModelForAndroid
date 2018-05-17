package com.android.szh.common.http.exception;

import android.util.SparseArray;

/**
 * 自定义错误码辅助类
 * 该辅助类需要和服务端约定
 */
public final class ExceptionCode {

    /**
     * 网络错误
     */
    public static final int NETWORD_ERROR = 1000;
    /**
     * 服务器连接超时
     */
    public static final int CONNECT_TIMEOUT = 1001;
    /**
     * 服务器响应超时
     */
    public static final int SOCKET_TIMEOUT = 1002;
    /**
     * 未知的主机域名
     */
    public static final int UNKNOWN_HOST = 1003;
    /**
     * 不支持的编码格式异常
     */
    public static final int UNSUPPORTED_ENCODING = 1004;
    /**
     * URL异常
     */
    public static final int MALFORMED_URL = 1006;
    /**
     * 证书出错
     */
    public static final int SSL_ERROR = 1007;
    /**
     * 证书未找到
     */
    public static final int SSL_NOT_FOUND = 1008;
    /**
     * 类型转换异常
     */
    public static final int CLASS_CAST = 1009;
    /**
     * 解析错误
     */
    public static final int PARSE_ERROR = 1010;
    /**
     * 服务端返回数据格式异常
     */
    public static final int FORMAT_ERROR = 1011;
    /**
     * 数据出现空值
     */
    public static final int DATA_NULL = 1012;
    /**
     * 日期格式解析异常
     */
    public static final int DATE_PARSE = 1013;
    /**
     * Token校验异常
     */
    public static final int TOKEN_CHECK = 1014;
    /**
     * 未知错误
     */
    public static final int UNKNOWN = 9999;

    private static final SparseArray<String> CODE_MAP = new SparseArray<>();

    static {
        CODE_MAP.put(NETWORD_ERROR, "网络错误");
        CODE_MAP.put(CONNECT_TIMEOUT, "服务器连接超时");
        CODE_MAP.put(SOCKET_TIMEOUT, "服务器响应超时");
        CODE_MAP.put(UNKNOWN_HOST, "未知的主机域名");
        CODE_MAP.put(UNSUPPORTED_ENCODING, "不支持的编码格式异常");
        CODE_MAP.put(MALFORMED_URL, "URL异常");
        CODE_MAP.put(SSL_ERROR, "证书验证失败");
        CODE_MAP.put(SSL_NOT_FOUND, "证书路径没找到");
        CODE_MAP.put(CLASS_CAST, "类型转换异常");
        CODE_MAP.put(DATE_PARSE, "类型转换异常");
        CODE_MAP.put(PARSE_ERROR, "数据解析异常");
        CODE_MAP.put(FORMAT_ERROR, "服务端返回数据格式异常");
        CODE_MAP.put(DATA_NULL, "数据出现空值");
        CODE_MAP.put(DATE_PARSE, "日期格式解析异常");
        CODE_MAP.put(TOKEN_CHECK, "Token校验异常");
        CODE_MAP.put(UNKNOWN, "未知错误");
    }

    public static String get(int code) {
        return CODE_MAP.get(code);
    }

}