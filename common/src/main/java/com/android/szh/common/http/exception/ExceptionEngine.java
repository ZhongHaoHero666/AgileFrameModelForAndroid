package com.android.szh.common.http.exception;

import android.net.ParseException;
import android.nfc.FormatException;

import com.android.szh.common.http.HttpCode;
import com.google.gson.JsonParseException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.cert.CertPathValidatorException;

import javax.net.ssl.SSLHandshakeException;

/**
 * 网络请求异常处理

 */
public class ExceptionEngine {

    public static HttpException handleException(Throwable e) {
        if (e instanceof HttpException) {
            return (HttpException) e;
        } else if (e instanceof retrofit2.HttpException) {
            retrofit2.HttpException exception = (retrofit2.HttpException) e;
            HttpException httpException = new HttpException(exception);
            httpException.setDesc(HttpCode.get(exception.code()));
            return httpException;
        } else {
            int code = getExceptionCode(e);
            HttpException httpException = new HttpException(e);
            httpException.setCode(code);
            httpException.setDesc(ExceptionCode.get(code));
            httpException.setMessage(e.getMessage());
            return httpException;
        }
    }

    public static int getExceptionCode(Throwable e) {
        int code;
        if (e instanceof NetErrorException || e instanceof ConnectException || e instanceof SocketException) {
            code = ExceptionCode.NETWORD_ERROR;
        } else if (e instanceof ConnectTimeoutException) {
            code = ExceptionCode.CONNECT_TIMEOUT;
        } else if (e instanceof SocketTimeoutException) {
            code = ExceptionCode.SOCKET_TIMEOUT;
        } else if (e instanceof UnknownHostException) {
            code = ExceptionCode.UNKNOWN_HOST;
        } else if (e instanceof UnsupportedEncodingException) {
            code = ExceptionCode.UNSUPPORTED_ENCODING;
        } else if (e instanceof MalformedURLException) {
            code = ExceptionCode.MALFORMED_URL;
        } else if (e instanceof SSLHandshakeException) {
            code = ExceptionCode.SSL_ERROR;
        } else if (e instanceof CertPathValidatorException) {
            code = ExceptionCode.SSL_NOT_FOUND;
        } else if (e instanceof ClassCastException) {
            code = ExceptionCode.CLASS_CAST;
        } else if (e instanceof JsonParseException || e instanceof JSONException) {
            code = ExceptionCode.PARSE_ERROR;
        } else if (e instanceof FormatException) {
            code = ExceptionCode.FORMAT_ERROR;
        } else if (e instanceof NullPointerException) {
            code = ExceptionCode.DATA_NULL;
        } else if (e instanceof ParseException) {
            code = ExceptionCode.DATE_PARSE;
        }  else if (e instanceof TokenCheckException) {
            code = ExceptionCode.TOKEN_CHECK;
        } else {
            code = ExceptionCode.UNKNOWN;
        }
        return code;
    }

}
