package com.android.szh.common.logger.parser;

import android.net.Uri;


import com.android.szh.common.logger.utils.LogConvert;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * {@link Uri}解析器
 *
 * Created by sunzhonghao
 * @date 2017/7/25 12:50
 */
public class UriParse implements IParser<Uri> {

    private static final String HEADER = "%s ";
    private static final String ITEM = "    %s = %s";

    @Override
    public Class<Uri> parseClassType() {
        return Uri.class;
    }

    @Override
    public String parseString(Uri uri) {
        if (uri != null) {
            String simpleName = uri.getClass().getSimpleName();
            String header = String.format(LOCALE, HEADER, simpleName);
            StringBuilder builder = new StringBuilder(header);
            builder.append("[").append(LINE_SEPARATOR);
            builder.append(String.format(ITEM, "Scheme", uri.getScheme())).append(LINE_SEPARATOR);
            builder.append(String.format(ITEM, "Scheme-Specific-Part", uri.getSchemeSpecificPart())).append(LINE_SEPARATOR);
            builder.append(String.format(ITEM, "Authority", uri.getAuthority())).append(LINE_SEPARATOR);
            builder.append(String.format(ITEM, "Host", uri.getHost())).append(LINE_SEPARATOR);
            builder.append(String.format(ITEM, "Port", uri.getPort())).append(LINE_SEPARATOR);
            builder.append(String.format(ITEM, "Path", uri.getPath())).append(LINE_SEPARATOR);
            String pathSegments = LogConvert.objectToStringWithFormat(uri.getPathSegments());
            builder.append(String.format(ITEM, "PathSegments", pathSegments)).append(LINE_SEPARATOR);
            builder.append(String.format(ITEM, "Query", uri.getQuery())).append(LINE_SEPARATOR);
            String parameters = LogConvert.objectToStringWithFormat(getQueryParameters(uri));
            builder.append(String.format(ITEM, "Parameters", parameters)).append(LINE_SEPARATOR);
            builder.append(String.format(ITEM, "Fragment", uri.getFragment())).append(LINE_SEPARATOR);
            builder.append(String.format(ITEM, "isAbsolute", uri.isAbsolute())).append(LINE_SEPARATOR);
            builder.append("]");
            return builder.toString();
        }
        return null;
    }

    private HashMap<String, String> getQueryParameters(Uri uri) {
        if (uri.isOpaque()) {
            return new HashMap<>();
        }
        String query = uri.getEncodedQuery();
        if (query == null) {
            return new HashMap<>();
        }
        HashMap<String, String> params = new LinkedHashMap<>();
        int start = 0;
        do {
            int next = query.indexOf('&', start);
            int end = (next == -1) ? query.length() : next;
            int separator = query.indexOf('=', start);
            if (separator > end || separator == -1) {
                separator = end;
            }
            String key = query.substring(start, separator);
            String value = query.substring(separator + 1, end);
            params.put(key, value);
            start = end + 1;
        } while (start < query.length());

        return params;
    }

}
