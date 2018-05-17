package com.android.szh.common.logger.parser;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * {@link Throwable}解析器
 *
 * Created by sunzhonghao
 * @date 2017/7/21 17:47
 */
public class ThrowableParse implements IParser<Throwable> {

    @Override
    public Class<Throwable> parseClassType() {
        return Throwable.class;
    }

    @Override
    public String parseString(Throwable throwable) {
        if (throwable != null) {
            StringWriter stringWriter = new StringWriter(256);
            PrintWriter printWriter = new PrintWriter(stringWriter, false);
            throwable.printStackTrace(printWriter);
            printWriter.flush();
            return stringWriter.toString();
        }
        return null;
    }

}
