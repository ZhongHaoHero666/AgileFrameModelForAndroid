package com.android.szh.common.logger.parser;

import android.graphics.Bitmap;
import android.os.Build;

import com.android.szh.common.logger.utils.BitmapHelper;


/**
 * {@link Bitmap}解析器
 *
 * Created by sunzhonghao
 * @date 2017/7/25 9:36
 */
public class BitmapParse implements IParser<Bitmap> {

    private static final String HEADER = "%s ";
    private static final String ITEM = "    %s : %s";

    @Override
    public Class<Bitmap> parseClassType() {
        return Bitmap.class;
    }

    @Override
    public String parseString(Bitmap bitmap) {
        if (bitmap != null) {
            String simpleName = bitmap.getClass().getSimpleName();
            String header = String.format(LOCALE, HEADER, simpleName);
            StringBuilder builder = new StringBuilder(header);
            builder.append("[").append(LINE_SEPARATOR);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
                builder.append(String.format(ITEM, "generationId", bitmap.getGenerationId())).append(LINE_SEPARATOR);
            }
            builder.append(String.format(ITEM, "bitmapWidth", String.valueOf(bitmap.getWidth()))).append(LINE_SEPARATOR);
            builder.append(String.format(ITEM, "bitmapHeight", String.valueOf(bitmap.getHeight()))).append(LINE_SEPARATOR);
            builder.append(String.format(ITEM, "bitmapSize", BitmapHelper.getFormatBitmapSize(bitmap))).append(LINE_SEPARATOR);
            Bitmap.Config config = bitmap.getConfig();
            builder.append(String.format(ITEM, "bitmapConfig", config == null ? null : config.name())).append(LINE_SEPARATOR);
            builder.append(String.format(ITEM, "isMutable", String.valueOf(bitmap.isMutable()))).append(LINE_SEPARATOR);
            builder.append(String.format(ITEM, "isRecycled", String.valueOf(bitmap.isRecycled()))).append(LINE_SEPARATOR);
            builder.append("]");
            return builder.toString();
        }
        return null;
    }

}
