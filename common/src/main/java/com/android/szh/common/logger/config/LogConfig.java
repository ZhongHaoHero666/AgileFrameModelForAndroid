package com.android.szh.common.logger.config;

import android.util.Log;


import com.android.szh.common.logger.parser.BitmapParse;
import com.android.szh.common.logger.parser.BundleParse;
import com.android.szh.common.logger.parser.CollectionParse;
import com.android.szh.common.logger.parser.IParser;
import com.android.szh.common.logger.parser.IntentParse;
import com.android.szh.common.logger.parser.MapParse;
import com.android.szh.common.logger.parser.MessageParse;
import com.android.szh.common.logger.parser.ReferenceParse;
import com.android.szh.common.logger.parser.SparseArrayParse;
import com.android.szh.common.logger.parser.ThrowableParse;
import com.android.szh.common.logger.parser.UriParse;
import com.android.szh.common.logger.utils.LogConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 日志配置信息
 *
 * Created by sunzhonghao
 * @date 2017/7/21 18:12
 */
public class LogConfig {

    /** 默认支持解析库 */
    private static final List<Class<? extends IParser>> DEFAULT_PARSE_CLASS = new ArrayList<>();

    static {
        DEFAULT_PARSE_CLASS.add(UriParse.class);
        DEFAULT_PARSE_CLASS.add(BundleParse.class);
        DEFAULT_PARSE_CLASS.add(IntentParse.class);
        DEFAULT_PARSE_CLASS.add(MessageParse.class);
        DEFAULT_PARSE_CLASS.add(CollectionParse.class);
        DEFAULT_PARSE_CLASS.add(SparseArrayParse.class);
        DEFAULT_PARSE_CLASS.add(MapParse.class);
        DEFAULT_PARSE_CLASS.add(BitmapParse.class);
        DEFAULT_PARSE_CLASS.add(ThrowableParse.class);
        DEFAULT_PARSE_CLASS.add(ReferenceParse.class);
    }

    private boolean isLogEnabled; // 是否启用日志输出
    private String commonTag; // 公共Tag
    private boolean showThreadInfo; // 是否打印线程信息
    private boolean showMethodInfo; // 是否打印方法信息(类名/方法名/行号)
    private int logMinLevel; // 日志最小显示级别
    private List<IParser> parseList; // 自定义解析器集合

    public LogConfig() {
        this.isLogEnabled = true;
        this.showThreadInfo = false;
        this.showMethodInfo = false;
        this.commonTag = LogConstants.TAG;
        this.logMinLevel = Log.VERBOSE;
        this.parseList = new ArrayList<>();
        this.addParserClass(DEFAULT_PARSE_CLASS);
    }

    /**
     * 生成默认的{@link LogConfig}
     */
    public static LogConfig getDefault() {
        return new LogConfig();
    }

    /**
     * 返回是否启用日志输出
     */
    public boolean isLogEnabled() {
        return isLogEnabled;
    }

    /**
     * 设置是否启用日志输出
     */
    public LogConfig setLogEnabled(boolean enabled) {
        this.isLogEnabled = enabled;
        return this;
    }

    /**
     * 返回公共标签
     */
    public String getCommonTag() {
        return commonTag;
    }

    /**
     * 设置公共标签
     */
    public LogConfig setCommonTag(String prefix) {
        this.commonTag = prefix;
        return this;
    }

    /**
     * 返回是否打印线程信息
     */
    public boolean isShowThreadInfo() {
        return showThreadInfo;
    }

    /**
     * 设置是否打印线程信息
     */
    public LogConfig setShowThreadInfo(boolean showThreadInfo) {
        this.showThreadInfo = showThreadInfo;
        return this;
    }

    /**
     * 返回是否打印方法信息
     */
    public boolean isShowMethodInfo() {
        return showMethodInfo;
    }

    /**
     * 设置是否打印方法信息
     */
    public LogConfig setShowMethodInfo(boolean showMethodInfo) {
        this.showMethodInfo = showMethodInfo;
        return this;
    }

    /**
     * 返回日志最小输出级别
     */
    public int getLogMinLevel() {
        return logMinLevel;
    }

    /**
     * 设置日志最小输出级别
     */
    public void setLogMinLevel(int logMinLevel) {
        this.logMinLevel = logMinLevel;
    }

    /**
     * 返回解析器集合
     */
    public List<IParser> getParseList() {
        return parseList;
    }

    /**
     * 添加解析器
     */
    public LogConfig addParser(IParser parser) {
        parseList.add(0, parser);
        return this;
    }

    /**
     * 添加默认解析器
     */
    private LogConfig addParserClass(Class<? extends IParser>... classes) {
        return addParserClass(Arrays.asList(classes));
    }

    /**
     * 添加默认解析器
     */
    private LogConfig addParserClass(Collection<Class<? extends IParser>> classes) {
        for (Class<? extends IParser> clazz : classes) {
            try {
                parseList.add(0, clazz.newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this;
    }

}
