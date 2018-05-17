package com.android.szh.common.logger.parser;


import com.android.szh.common.logger.utils.LogConstants;

import java.util.Locale;

/**
 * 解析器接口
 *
 * Created by sunzhonghao
 * @date 2017/7/21 16:59
 */
public interface IParser<T> {

    String LINE_SEPARATOR = LogConstants.LINE_SEPARATOR;
    Locale LOCALE = Locale.getDefault();

    Class<T> parseClassType();

    String parseString(T t);
}
