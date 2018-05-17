package com.android.szh.common.logger.parser;

import android.util.SparseArray;

import com.android.szh.common.logger.utils.LogConvert;


/**
 * {@link SparseArray}解析器
 *
 * Created by sunzhonghao
 * @date 2017/7/25 9:22
 */
public class SparseArrayParse implements IParser<SparseArray> {

    private static final String ITEM = "    %s -> %s";
    private static final String HEADER = "%s size = %d ";

    @Override
    public Class<SparseArray> parseClassType() {
        return SparseArray.class;
    }

    @Override
    public String parseString(SparseArray sparseArray) {
        if (sparseArray != null) {
            int size = sparseArray.size();
            String simpleName = sparseArray.getClass().getName();
            String header = String.format(LOCALE, HEADER, simpleName, size);
            StringBuilder builder = new StringBuilder(header);
            builder.append("[").append(LINE_SEPARATOR);
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    int key = sparseArray.keyAt(i);
                    Object value = sparseArray.valueAt(i);
                    if (value != null) {
                        if (value instanceof String) {
                            value = "\"" + value + "\"";
                        } else if (value instanceof Character) {
                            value = "\'" + value + "\'";
                        }
                    }
                    builder.append(String.format(ITEM, LogConvert.objectToString(key), LogConvert.objectToStringWithFormat(value)))
                            .append(LINE_SEPARATOR);
                }
            }
            builder.append("]");
            return builder.toString();
        }
        return null;
    }

}
