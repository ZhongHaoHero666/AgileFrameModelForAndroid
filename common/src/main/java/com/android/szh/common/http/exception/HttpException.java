package com.android.szh.common.http.exception;

import android.text.TextUtils;

import com.android.szh.common.utils.CharsetHelper;

import okhttp3.Request;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Response;

/**
 * Http请求异常
 */
public class HttpException extends Exception {

    private int code;
    private String desc;
    private String message;
    private String httpUrl;
    private String responseTime;
    private String responseContent;
    private Request request;
    private Response<?> response;

    public HttpException(Throwable throwable) {
        super(throwable);
        this.message = throwable.getMessage();
    }

    public HttpException(retrofit2.HttpException httpException) {
        if (httpException != null) {
            this.code = httpException.code();
            this.message = httpException.message();
            this.response = httpException.response();
            if (response != null) {
                this.request = response.raw().request();
                if (request != null) {
                    this.httpUrl = request.url().toString();
                }
                ResponseBody responseBody = response.errorBody();
                if (responseBody == null) {
                    responseBody = response.raw().body();
                }
                if (responseBody != null) {
                    BufferedSource source = responseBody.source();
                    Buffer buffer = source.buffer();
                    this.responseContent = buffer.clone().readString(CharsetHelper.UTF_8);
                }
            }
        }
    }

    public HttpException(Throwable throwable, Request request) {
        super(throwable);
        this.request = request;
        this.message = throwable.getMessage();
        if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;
            this.code = httpException.getCode();
            this.desc = httpException.getDesc();
        }
        if (request != null) {
            this.httpUrl = request.url().toString();
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHttpUrl() {
        if (TextUtils.isEmpty(httpUrl)) {
            if (request != null) {
                this.httpUrl = request.url().toString();
            }
        }
        return httpUrl;
    }

    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public String getResponseContent() {
        return responseContent;
    }

    public void setResponseContent(String responseContent) {
        this.responseContent = responseContent;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Response<?> getResponse() {
        return response;
    }

    public void setResponse(Response<?> response) {
        this.response = response;
    }


    @Override
    public String toString() {
        return "HttpException{" +
                "code=" + code +
                ", desc='" + desc + '\'' +
                ", message='" + message + '\'' +
                ", httpUrl='" + httpUrl + '\'' +
                ", responseTime='" + responseTime + '\'' +
                ", responseContent='" + responseContent + '\'' +
                '}';
    }

}
