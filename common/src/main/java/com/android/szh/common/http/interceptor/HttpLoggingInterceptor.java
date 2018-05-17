package com.android.szh.common.http.interceptor;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.android.szh.common.logger.Logger;
import com.android.szh.common.utils.DateHelper;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;

/**
 * OkHttp记录请求和响应信息的拦截器
 *
 */
public class HttpLoggingInterceptor implements Interceptor {

    private static final String TAG = "HTTP";
    private static final Charset UTF8 = Charset.forName("UTF-8");
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private static final List<String> HEADERS = new ArrayList<>();

    static {
        HEADERS.add("Date");
        HEADERS.add("Connection");
        HEADERS.add("Last-Modified");
        HEADERS.add("Content-Type");
        HEADERS.add("Content-Length");
        HEADERS.add("Cookie");
        HEADERS.add("Set-Cookie");
        HEADERS.add("Cache-Control");
        HEADERS.add("THE-TIME");
    }

    public enum Level {
        /**
         * No logs.
         */
        NONE,
        /**
         * Logs request and response lines.
         * <p>
         * <p>Example:
         * <pre>{@code
         * --> POST /greeting http/1.1 (3-byte body)
         *
         * <-- 200 OK (22ms, 6-byte body)
         * }</pre>
         */
        BASIC,
        /**
         * Logs request and response lines and their respective headers.
         * <p>
         * <p>Example:
         * <pre>{@code
         * --> POST /greeting http/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         * --> END POST
         *
         * <-- 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         * <-- END HTTP
         * }</pre>
         */
        HEADERS,
        /**
         * Logs request and response lines and their respective headers and bodies (if present).
         * <p>
         * <p>Example:
         * <pre>{@code
         * --> POST /greeting http/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         *
         * Hi?
         * --> END POST
         *
         * <-- 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         *
         * Hello!
         * <-- END HTTP
         * }</pre>
         */
        BODY
    }

    private volatile Level level = Level.NONE;

    /**
     * 更改此拦截器日志的级别
     */
    public HttpLoggingInterceptor setLevel(Level level) {
        if (level == null) throw new NullPointerException("level == null. Use Level.NONE instead.");
        this.level = level;
        return this;
    }

    public Level getLevel() {
        return level;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Level level = this.level;

        Request request = chain.request();
        if (level == Level.NONE) {
            return chain.proceed(request);
        }

        boolean logBody = level == Level.BODY;
        boolean logHeaders = logBody || level == Level.HEADERS;

        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;

        StringBuilder requestBuilder = new StringBuilder();
        Connection connection = chain.connection();
        Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;
        requestBuilder.append("--> ").append(request.method()).append(' ').append(protocol).append(' ').append(request.url());
        if (!logHeaders && hasRequestBody) {
            requestBuilder.append(LINE_SEPARATOR).append(" (").append(requestBody.contentLength()).append("-byte body)");
        }

        if (logHeaders) {
            if (hasRequestBody) {
                // Request body headers are only present when installed as a network interceptor. Force
                // them to be included (when available) so there values are known.
                if (requestBody.contentType() != null) {
                    requestBuilder.append(LINE_SEPARATOR).append("Content-Type: ").append(requestBody.contentType());
                }
                if (requestBody.contentLength() != -1) {
                    requestBuilder.append(LINE_SEPARATOR).append("Content-Length: ").append(requestBody.contentLength());
                }
            }

            Headers headers = request.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                String name = headers.name(i);
                // Skip headers from the request body as they are explicitly logged above.
                if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                    requestBuilder.append(LINE_SEPARATOR).append(name).append(": ").append(headers.value(i));
                }
            }

            if (!logBody || !hasRequestBody) {
                requestBuilder.append(LINE_SEPARATOR).append("--> END ").append(request.method());
            } else if (bodyEncoded(request.headers())) {
                requestBuilder.append(LINE_SEPARATOR).append("--> END ").append(request.method()).append(" (encoded body omitted)");
            } else {
                if (!(requestBody instanceof MultipartBody)) {
                    Buffer buffer = new Buffer();
                    requestBody.writeTo(buffer);

                    MediaType contentType = requestBody.contentType();
                    Charset charset = getCharset(contentType);

                    Logger.i(TAG, "");
                    if (isPlaintext(buffer)) {
                        requestBuilder.append(LINE_SEPARATOR).append(buffer.readString(charset));
                        requestBuilder.append(LINE_SEPARATOR).append("--> END ").append(request.method()).append(" (").append(requestBody.contentLength()).append("-byte body)");
                    } else {
                        requestBuilder.append(LINE_SEPARATOR).append("--> END ").append(request.method()).append(" (binary ").append(requestBody.contentLength()).append("-byte body omitted)");
                    }
                }
            }
        }
        Logger.i(TAG, requestBuilder.toString());

        long startNs = System.nanoTime();
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            Logger.e(TAG, "<-- HTTP FAILED: " + e);
            throw e;
        }

        if (response.request().url().pathSegments().contains("404.html")) { // 过滤404错误页面
            Logger.e(TAG, "<-- HTTP FAILED: " + response.request().url());
            return response;
        }

        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        StringBuilder responseBuilder = new StringBuilder();
        ResponseBody responseBody = response.body();
        long contentLength = 0;
        if (responseBody != null) {
            contentLength = responseBody.contentLength();
        }
        String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";
        responseBuilder.append("<-- ").append(response.code())
                .append(' ').append(response.message())
                .append(' ').append(response.request().url())
                .append(' ').append("(").append(tookMs).append("ms")
                .append(!logHeaders ? ", " + bodySize + " body" : "").append(')');

        if (logHeaders) {
            String dateTime = null;
            Headers headers = response.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                String name = headers.name(i);
                if (HEADERS.contains(name)) {
                    String value = headers.value(i);
                    if ("Date".equals(name)) {
                        dateTime = DateHelper.formatDateTime(value);
                    }
                    responseBuilder.append(LINE_SEPARATOR).append(name).append(": ").append(value);
                }
            }
            if (!TextUtils.isEmpty(dateTime)) {
                responseBuilder.append(LINE_SEPARATOR).append("服务器时间" + ": ").append(dateTime);
            }

            if (!logBody || !HttpHeaders.hasBody(response)) {
                responseBuilder.append(LINE_SEPARATOR).append("<-- END HTTP");
            } else if (bodyEncoded(response.headers())) {
                responseBuilder.append(LINE_SEPARATOR).append("<-- END HTTP (encoded body omitted)");
            } else {
                if (responseBody != null) {
                    BufferedSource source = responseBody.source();
                    source.request(Long.MAX_VALUE); // Buffer the entire body.
                    Buffer buffer = source.buffer();

                    MediaType contentType = responseBody.contentType();
                    Charset charset = getCharset(contentType);

                    if (!isPlaintext(buffer)) {
                        responseBuilder.append(LINE_SEPARATOR).append("");
                        responseBuilder.append(LINE_SEPARATOR).append("<-- END HTTP (binary ").append(buffer.size()).append("-byte body omitted)");
                        Logger.i(TAG, responseBuilder.toString());
                        return response;
                    }

                    if (contentLength != 0) {
                        responseBuilder.append(LINE_SEPARATOR).append("");
                        responseBuilder.append(LINE_SEPARATOR).append(buffer.clone().readString(charset));
                    }

                    responseBuilder.append(LINE_SEPARATOR).append("<-- END HTTP (").append(buffer.size()).append("-byte body)");
                }
            }
            Logger.i(TAG, responseBuilder.toString());
        }

        return response;
    }

    @NonNull
    private Charset getCharset(MediaType contentType) {
        Charset charset = null;
        if (contentType != null) {
            charset = contentType.charset(UTF8);
        }
        if (charset == null) {
            charset = UTF8;
        }
        return charset;
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }

}
