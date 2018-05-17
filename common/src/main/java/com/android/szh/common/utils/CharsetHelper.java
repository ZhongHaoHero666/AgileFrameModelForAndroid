package com.android.szh.common.utils;

import java.nio.charset.Charset;

/**
 * 字符集编码格式辅助类
 */
public final class CharsetHelper {

    private CharsetHelper() {
        throw new UnsupportedOperationException("Instantiation operation is not supported.");
    }

    public static final Charset GBK = Charset.forName("GBK");
    public static final Charset GB_2312 = Charset.forName("GB2312");
    public static final Charset GB_18030 = Charset.forName("GB18030");
    public static final Charset UTF_8 = Charset.forName("UTF-8");
    public static final Charset UTF_16 = Charset.forName("UTF-16");
    public static final Charset UTF_16BE = Charset.forName("UTF-16BE");
    public static final Charset UTF_16LE = Charset.forName("UTF-16LE");
    public static final Charset US_ASCII = Charset.forName("US-ASCII");
    public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");

}
