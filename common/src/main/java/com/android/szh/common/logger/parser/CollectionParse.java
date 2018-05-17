package com.android.szh.common.logger.parser;


import com.android.szh.common.logger.utils.LogConvert;

import java.util.Collection;
import java.util.Iterator;

/**
 * {@link Collection}解析器
 *
 * Created by sunzhonghao
 * @date 2017/7/21 17:18
 */
public class CollectionParse implements IParser<Collection> {

    private static final String ITEM = "    [%d]:%s";
    private static final String HEADER = "%s size = %d ";

    @Override
    public Class<Collection> parseClassType() {
        return Collection.class;
    }

    @Override
    public String parseString(Collection collection) {
        if (collection != null) {
            String simpleName = collection.getClass().getName();
            String header = String.format(LOCALE, HEADER, simpleName, collection.size());
            StringBuilder builder = new StringBuilder(header);
            builder.append("[").append(LINE_SEPARATOR);
            if (!collection.isEmpty()) {
                Iterator iterator = collection.iterator();
                int flag = 0;
                while (iterator.hasNext()) {
                    Object item = iterator.next();
                    builder.append(String.format(LOCALE, ITEM, flag, LogConvert.objectToStringWithFormat(item)))
                            .append(flag++ < collection.size() - 1 ? "," + LINE_SEPARATOR : LINE_SEPARATOR);

                }
            }
            builder.append("]");
            return builder.toString();
        }
        return null;
    }

}
