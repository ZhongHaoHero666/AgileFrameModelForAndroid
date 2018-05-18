package com.android.szh.common.utils;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.android.szh.common.config.UrlConfig;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Url处理辅助类
 */
public class UrlHelper {

    /**
     * HTTP协议
     */
    public static final String PROTOCOL_HTTP = "http://";
    /**
     * HTTPS协议
     */
    public static final String PROTOCOL_HTTPS = "https://";
    /**
     * 一级域名提取的正则表达式
     */
    private static final String REGEX_DOMAIN_LEVEL_ONE = "(\\w*\\.?){1}\\.(com.cn|net.cn|gov.cn|org\\.nz|org.cn|com|net|org|gov|cc|biz|info|cn|co)$";
    /**
     * 二级域名提取的正则表达式
     */
    private static final String REGEX_DOMAIN_LEVEL_TWO = "(\\w*\\.?){2}\\.(com.cn|net.cn|gov.cn|org\\.nz|org.cn|com|net|org|gov|cc|biz|info|cn|co)$";
    /**
     * 三级域名提取的正则表达式
     */
    private static final String REGEX_DOMAIN_LEVEL_THREE = "(\\w*\\.?){3}\\.(com.cn|net.cn|gov.cn|org\\.nz|org.cn|com|net|org|gov|cc|biz|info|cn|co)$";

    /**
     * 判断Url是否为空
     */
    public static boolean isUrlEmpty(String url) {
        return TextUtils.isEmpty(url)
                || TextUtils.isEmpty(url.trim())
                || PROTOCOL_HTTP.equals(url)
                || PROTOCOL_HTTPS.equals(url);
    }

    /**
     * 获取baseUrl
     */
    public static String getBaseUrl(String baseUrl) {
        if (TextUtils.isEmpty(baseUrl)) {
            baseUrl = UrlConfig.getDominUrl();
        }
        baseUrl = getFullUrlWithProtocol(baseUrl);
        if (!baseUrl.endsWith("/")) {
            baseUrl = baseUrl + "/";
        }
        return baseUrl;
    }

    /**
     * 返回完整的Url(检查Url是否包含域名)
     */
    public static String getFullUrl(String url) {
        if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(url.trim())) {
            url = url.trim();
            if (!url.startsWith(PROTOCOL_HTTP) && !url.startsWith(PROTOCOL_HTTPS)) {
                if (url.contains("//")) {
                    url = url.replaceAll("//", "/");
                }
                String domain = UrlConfig.getDominUrl();
                url = domain + url;
            }
        }
        return url;
    }

    /**
     * 返回完整的Url(检查Url是否包含协议)
     */
    public static String getFullUrlWithProtocol(String url) {
        if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(url.trim())) {
            url = url.trim();
            if (!url.startsWith(PROTOCOL_HTTP) && !url.startsWith(PROTOCOL_HTTPS)) {
                if (url.contains("//")) {
                    url = url.replaceAll("//", "/");
                }
                url = PROTOCOL_HTTPS + url;
            }
        }
        return url;
    }

    /**
     * 返回完整Url(检查Url是否包含域名)
     */
    public static String getFullUrlWithDomain(String url) {
        String domain = UrlConfig.getDominUrl();
        return getFullUrlWithDomain(url, domain, false);
    }

    /**
     * 返回完整Url(检查Url是否包含域名，并强制替换为当前域名)
     */
    public static String getFullUrlWithDomain(String url, String domain, boolean replace) {
        if (TextUtils.isEmpty(domain)) {
            domain = UrlConfig.getDominUrl();
        }
        if (!TextUtils.isEmpty(url)) {
            String host = Uri.parse(url).getHost();
            if (TextUtils.isEmpty(host)) { // 判断域名是否为空
                if (url.startsWith("//")) { // 判断Url是否以//开头
                    url = url.replace("//", "/");
                }
                url = domain + url; // 拼接域名
            } else {
                if (replace) {
                    String useHost = Uri.parse(domain).getHost();
                    if (!host.equals(useHost)) { // 判断域名是否相同
                        url = url.replace(host, useHost);
                    }
                }
            }
        }
        url = getFullUrlWithProtocol(url);
        return url;
    }

    /**
     * 返回一级域名
     */
    public static String getTopDomainLevelOne(String url) {
        return getTopDomainLevel(REGEX_DOMAIN_LEVEL_ONE, url);
    }

    /**
     * 返回二级域名
     */
    public static String getTopDomainLevelTwo(String url) {
        return getTopDomainLevel(REGEX_DOMAIN_LEVEL_TWO, url);
    }

    /**
     * 返回三级域名
     */
    public static String getTopDomainLevelThree(String url) {
        return getTopDomainLevel(REGEX_DOMAIN_LEVEL_THREE, url);
    }

    /**
     * 根据正则表达式返回需要的域名
     *
     * @param regex 正则表达式
     * @param url   Url
     */
    private static String getTopDomainLevel(String regex, String url) {
        if (TextUtils.isEmpty(url)) {
            return url;
        }
        String domain;
        Uri uri = Uri.parse(url);
        domain = uri.getHost();
        try {
            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(domain);
            if (matcher.find()) {
                domain = matcher.group();
            } else {
                domain = getTopDomainLevelByDomain(regex, domain);
            }
        } catch (Exception e) {
            domain = getTopDomainLevelByDomain(regex, domain);
        }
        return domain;
    }

    /**
     * 根据域名返回需要的域名
     *
     * @param regex  正则表达式
     * @param domain 域名
     */
    @Nullable
    private static String getTopDomainLevelByDomain(String regex, String domain) {
        if (!TextUtils.isEmpty(domain)) {
            String[] split = domain.split("\\.");
            int length = split.length;
            if (REGEX_DOMAIN_LEVEL_ONE.equalsIgnoreCase(regex)) {
                if (length > 2) {
                    domain = split[length - 2] + "." + split[length - 1];
                }
            } else if (REGEX_DOMAIN_LEVEL_TWO.equalsIgnoreCase(regex)) {
                if (length > 3) {
                    domain = split[length - 3] + "." + split[length - 2] + "." + split[length - 1];
                }
            } else if (REGEX_DOMAIN_LEVEL_THREE.equalsIgnoreCase(regex)) {
                if (length > 4) {
                    domain = split[length - 4] + "." + split[length - 3] + "." + split[length - 2] + "." + split[length - 1];
                }
            }
        }
        return domain;
    }

    public static String encode(String content) {
        if (TextUtils.isEmpty(content)) {
            return "";
        }
        try {
            return URLEncoder.encode(content, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return URLEncoder.encode(content);
        }
    }

    public static String decode(String content) {
        if (TextUtils.isEmpty(content)) {
            return "";
        }
        try {
            return URLDecoder.decode(content, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return URLDecoder.decode(content);
        }
    }

}
