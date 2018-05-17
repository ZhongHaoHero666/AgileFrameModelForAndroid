package com.android.szh.common.logger.parser;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;


import com.android.szh.common.logger.utils.LogConvert;

import java.lang.reflect.Field;

/**
 * {@link Intent}解析器
 *
 * Created by sunzhonghao
 * @date 2017/7/21 17:48
 */
public class IntentParse implements IParser<Intent> {

    private static final String HEADER = "%s ";
    private static final String ITEM = "    %s = %s";
    private static final String FLAG_PREFIX = "FLAG_";
    private static final SparseArray<String> FLAGARRAY = new SparseArray<>();

    static {
        Class cla = Intent.class;
        Field[] fields = cla.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getName().startsWith(FLAG_PREFIX)) {
                int value = 0;
                try {
                    Object object = field.get(cla);
                    if (object instanceof Integer || object.getClass().equals(int.class)) {
                        value = (int) object;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (FLAGARRAY.get(value) == null) {
                    FLAGARRAY.put(value, field.getName());
                }
            }
        }
    }

    @Override
    public Class<Intent> parseClassType() {
        return Intent.class;
    }

    @Override
    public String parseString(Intent intent) {
        if (intent != null) {
            String simpleName = intent.getClass().getSimpleName();
            String header = String.format(LOCALE, HEADER, simpleName);
            StringBuilder builder = new StringBuilder(header);
            builder.append("[").append(LINE_SEPARATOR);
            builder.append(String.format(ITEM, "Scheme", intent.getScheme())).append(LINE_SEPARATOR);
            builder.append(String.format(ITEM, "Action", intent.getAction())).append(LINE_SEPARATOR);
            builder.append(String.format(ITEM, "DataString", intent.getDataString())).append(LINE_SEPARATOR);
            builder.append(String.format(ITEM, "Type", intent.getType())).append(LINE_SEPARATOR);
            builder.append(String.format(ITEM, "Package", intent.getPackage())).append(LINE_SEPARATOR);
            builder.append(String.format(ITEM, "ComponentInfo", intent.getComponent())).append(LINE_SEPARATOR);
            builder.append(String.format(ITEM, "Flags", getFlags(intent.getFlags()))).append(LINE_SEPARATOR);
            builder.append(String.format(ITEM, "Categories", intent.getCategories())).append(LINE_SEPARATOR);
            Bundle extras = intent.getExtras();
            builder.append(String.format(ITEM, "Extras", extras == null ? null : LogConvert.objectToStringWithFormat(extras))).append(LINE_SEPARATOR);
            builder.append("]");
            return builder.toString();
        }
        return null;
    }

    /**
     * 获取flag的值
     *
     * @param flags
     */
    private String getFlags(int flags) {
        int size = FLAGARRAY.size();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            int flagKey = FLAGARRAY.keyAt(i);
            if ((flagKey & flags) == flagKey) {
                builder.append(FLAGARRAY.get(flagKey));
                builder.append(" | ");
            }
        }
        if (TextUtils.isEmpty(builder.toString())) {
            builder.append(flags);
        } else if (builder.indexOf("|") != -1) {
            builder.delete(builder.length() - 2, builder.length());
        }
        return builder.toString();
    }

}
