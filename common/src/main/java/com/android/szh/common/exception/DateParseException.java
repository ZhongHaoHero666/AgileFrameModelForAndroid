package com.android.szh.common.exception;

/**
 * 日期解析异常
 */
public class DateParseException extends Exception {

    public DateParseException() {
    }

    public DateParseException(String message) {
        super(message);
    }

    public DateParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public DateParseException(Throwable cause) {
        super(cause);
    }
}
