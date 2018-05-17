package com.android.szh.common.logger.printer;

import android.text.TextUtils;
import android.util.Log;


import com.android.szh.common.logger.Logger;
import com.android.szh.common.logger.config.LogConfig;
import com.android.szh.common.logger.utils.LogConstants;
import com.android.szh.common.logger.utils.LogConvert;
import com.android.szh.common.logger.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.MissingFormatArgumentException;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * 日志树功能实现
 *
 * Created by sunzhonghao
 * @date 2017/7/24 9:31
 */
public abstract class Printer implements IPrinter {

    protected final LogConfig mLogConfig = Logger.getLogConfig();
    protected final ThreadLocal<String> localTags = new ThreadLocal<>();

    public IPrinter setTag(String tag) {
        if (!TextUtils.isEmpty(tag) && mLogConfig.isLogEnabled()) {
            localTags.set(tag);
        }
        return this;
    }

    @Override
    public void v(String message, Object... args) {
        logString(Log.VERBOSE, null, message, args);
    }

    @Override
    public void v(String tag, String message, Object... args) {
        logString(Log.VERBOSE, tag, message, args);
    }

    @Override
    public void v(Object object) {
        logObject(Log.VERBOSE, null, object);
    }

    @Override
    public void v(String tag, Object object) {
        logObject(Log.VERBOSE, tag, object);
    }

    @Override
    public void d(String message, Object... args) {
        logString(Log.DEBUG, null, message, args);
    }

    @Override
    public void d(String tag, String message, Object... args) {
        logString(Log.DEBUG, tag, message, args);
    }

    @Override
    public void d(Object object) {
        logObject(Log.DEBUG, null, object);
    }

    @Override
    public void d(String tag, Object object) {
        logObject(Log.DEBUG, tag, object);
    }

    @Override
    public void i(String message, Object... args) {
        logString(Log.INFO, null, message, args);
    }

    @Override
    public void i(String tag, String message, Object... args) {
        logString(Log.INFO, tag, message, args);
    }

    @Override
    public void i(Object object) {
        logObject(Log.INFO, null, object);
    }

    @Override
    public void i(String tag, Object object) {
        logObject(Log.INFO, tag, object);
    }

    @Override
    public void w(String message, Object... args) {
        logString(Log.WARN, null, message, args);
    }

    @Override
    public void w(String tag, String message, Object... args) {
        logString(Log.WARN, tag, message, args);
    }

    @Override
    public void w(Object object) {
        logObject(Log.WARN, null, object);
    }

    @Override
    public void w(String tag, Object object) {
        logObject(Log.WARN, tag, object);
    }

    @Override
    public void e(String message, Object... args) {
        logString(Log.ERROR, null, message, args);
    }

    @Override
    public void e(String tag, String message, Object... args) {
        logString(Log.ERROR, tag, message, args);
    }

    @Override
    public void e(Object object) {
        logObject(Log.ERROR, null, object);
    }

    @Override
    public void e(String tag, Object object) {
        logObject(Log.ERROR, tag, object);
    }

    @Override
    public void wtf(String message, Object... args) {
        logString(Log.ASSERT, null, message, args);
    }

    @Override
    public void wtf(String tag, String message, Object... args) {
        logString(Log.ASSERT, tag, message, args);
    }

    @Override
    public void wtf(Object object) {
        logObject(Log.ASSERT, null, object);
    }

    @Override
    public void wtf(String tag, Object object) {
        logObject(Log.ASSERT, tag, object);
    }

    @Override
    public void json(String json) {
        json(null, json);
    }

    @Override
    public void json(String tag, String json) {
        if (TextUtils.isEmpty(json)) {
            d(tag, "JSON{json is empty}");
            return;
        }
        int indent = 4;
        try {
            json = json.trim();
            if (json.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(json);
                String msg = jsonObject.toString(indent);
                i(tag, msg);
            } else if (json.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(json);
                String msg = jsonArray.toString(indent);
                i(tag, msg);
            }
        } catch (JSONException e) {
            e(tag, "json = " + json + "\n\n" + e.toString());
        }
    }

    @Override
    public void xml(String xml) {
        xml(null, xml);
    }

    @Override
    public void xml(String tag, String xml) {
        if (TextUtils.isEmpty(xml)) {
            d(tag, "XML{xml is empty}");
            return;
        }
        try {
            xml = xml.trim();
            Source xmlInput = new StreamSource(new StringReader(xml));
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(xmlInput, xmlOutput);
            i(tag, xmlOutput.getWriter().toString().replaceFirst(">", ">\n"));
        } catch (TransformerException e) {
            e(tag, "xml = " + xml + "\n\n" + e.toString());
        }
    }

    private void logObject(int level, String tag, Object object) {
        logString(level, tag, LogConvert.objectToString(object));
    }

    private void logString(int level, String tag, String message, Object... args) {
        if (!mLogConfig.isLogEnabled()) {//判定是否显示日志
            return;
        }
        if (level < mLogConfig.getLogMinLevel()) {//判断日志显示最小级别
            return;
        }
        if (TextUtils.isEmpty(tag)) {
            tag = generateTag();
        }
        if (args != null && args.length > 0) {//有格式化参数
            try {
                message = String.format(message, args);
            } catch (NullPointerException | MissingFormatArgumentException e) {
                e.printStackTrace();
            }
        }
        printMessage(level, tag, message);
    }

    protected void printMessage(int level, String tag, String message) {
        StringBuilder builder = new StringBuilder();
        if (mLogConfig.isShowThreadInfo()) { // 是否打印线程信息
            builder.append(LogUtils.getThreadInfo()).append(LogConstants.LINE_SEPARATOR);
        }
        if (mLogConfig.isShowMethodInfo()) { // 是否打印方法信息(类名/方法名/行号)
            builder.append(LogUtils.getTopStackInfo()).append(LogConstants.LINE_SEPARATOR);
        }
        builder.append(message);
        printLog(level, tag, builder.toString());
    }

    /**
     * 输出日志的具体实现方式，可以是打印、文件存储等
     *
     * @param level
     * @param tag
     * @param message
     */
    protected abstract void printLog(int level, String tag, String message);

    /**
     * 生成标签
     */
    private String generateTag() {
        String tempTag = localTags.get();
        if (!TextUtils.isEmpty(tempTag)) {
            localTags.remove();
            return tempTag;
        }
        return mLogConfig.getCommonTag();
    }

}
