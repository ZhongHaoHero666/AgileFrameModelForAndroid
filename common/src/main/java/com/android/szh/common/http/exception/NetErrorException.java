package com.android.szh.common.http.exception;

/**
 * 网络异常类
 */
public class NetErrorException extends RuntimeException {
    public NetErrorException() {
        super("The network is unavailable.");
    }
}
