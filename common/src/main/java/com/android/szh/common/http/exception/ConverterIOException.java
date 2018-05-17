package com.android.szh.common.http.exception;

import java.io.IOException;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * 用于Convert包装时的异常
 */
public class ConverterIOException extends IOException {

    private RequestBody requestBody;
    private ResponseBody responseBody;

    public ConverterIOException(Throwable cause) {
        super(cause);
    }

    public ConverterIOException(Throwable cause, RequestBody requestBody) {
        super(cause);
        this.requestBody = requestBody;
    }

    public ConverterIOException(Throwable cause, ResponseBody responseBody) {
        super(cause);
        this.responseBody = responseBody;
    }

    public RequestBody getRequestBody() {
        return requestBody;
    }

    public ResponseBody getResponseBody() {
        return responseBody;
    }

}
