package com.android.szh.common.logger.parser;

import android.os.Bundle;


import com.android.szh.common.logger.utils.LogConvert;

import java.util.Set;

/**
 * {@link Bundle}解析器
 *
 * Created by sunzhonghao
 * @date 2017/7/21 17:03
 */
public class BundleParse implements IParser<Bundle> {

    private static final String ITEM = "    %s => %s";
    private static final String HEADER = "%s size = %d ";

    @Override
    public Class<Bundle> parseClassType() {
        return Bundle.class;
    }

    @Override
    public String parseString(Bundle bundle) {
        if (bundle != null) {
            String simpleName = bundle.getClass().getName();
            String header = String.format(LOCALE, HEADER, simpleName, bundle.size());
            StringBuilder builder = new StringBuilder(header);
            builder.append("[").append(LINE_SEPARATOR);
            if (!bundle.isEmpty()) {
                Set<String> keySet = bundle.keySet();
                for (String key : keySet) {
                    builder.append(String.format(LOCALE, ITEM, key, LogConvert.objectToStringWithFormat(bundle.get(key))))
                            .append(LINE_SEPARATOR);
                }
            }
            builder.append("]");
            return builder.toString();
        }
        return null;
    }

}
