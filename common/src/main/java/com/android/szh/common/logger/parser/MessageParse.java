package com.android.szh.common.logger.parser;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.android.szh.common.logger.utils.LogConvert;


/**
 * {@link Message}解析器
 *
 * Created by sunzhonghao
 * @date 2017/7/26 10:06
 */
public class MessageParse implements IParser<Message> {

    private static final String HEADER = "%s ";
    private static final String ITEM = "    %s : %s";

    @Override
    public Class<Message> parseClassType() {
        return Message.class;
    }

    @Override
    public String parseString(Message message) {
        if (message != null) {
            String simpleName = message.getClass().getSimpleName();
            String header = String.format(LOCALE, HEADER, simpleName);
            StringBuilder builder = new StringBuilder(header);
            builder.append("[").append(LINE_SEPARATOR);
            builder.append(String.format(ITEM, "what", String.valueOf(message.what))).append(LINE_SEPARATOR);
            builder.append(String.format(ITEM, "arg1", String.valueOf(message.arg1))).append(LINE_SEPARATOR);
            builder.append(String.format(ITEM, "arg2", String.valueOf(message.arg2))).append(LINE_SEPARATOR);
            Object obj = message.obj;
            builder.append(String.format(ITEM, "obj", obj == null ? null : LogConvert.objectToStringWithFormat(obj))).append(LINE_SEPARATOR);
            builder.append(String.format(ITEM, "when", String.valueOf(message.getWhen()))).append(LINE_SEPARATOR);
            Handler target = message.getTarget();
            builder.append(String.format(ITEM, "target", target == null ? null : target.getClass().getName())).append(LINE_SEPARATOR);
            Runnable callback = message.getCallback();
            builder.append(String.format(ITEM, "callback", callback == null ? null : callback.getClass().getName())).append(LINE_SEPARATOR);
            Bundle data = message.getData();
            builder.append(String.format(ITEM, "data", data == null ? null : LogConvert.objectToStringWithFormat(data))).append(LINE_SEPARATOR);
            builder.append("]");
            return builder.toString();
        }
        return null;
    }
}
