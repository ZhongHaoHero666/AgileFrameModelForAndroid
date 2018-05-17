package com.android.szh.common.utils;

import android.text.TextUtils;
import android.text.format.DateUtils;


import com.android.szh.common.exception.DateParseException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 日期时间工具类
 */
public class DateHelper {

    public final static String FORMAT_DATE = "yyyy-MM-dd";
    public final static String FORMAT_TIME = "HH:mm:ss";
    public final static String FORMAT_MONTH_DAY = "MM-dd";
    public final static String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public final static String FORMAT_MONTH_DAY_OTHER_TIME = "MM-dd  HH:mm";
    public final static String FORMAT_MONTH_DAY_NEWLINE_TIME = "MM-dd\nHH:mm";
    public final static String FORMAT_DATE_OUT_SECOND_TIME = "yyyy-MM-dd HH:mm";
    public final static String FORMAT_MONTH_DAY_TIME = "MM月dd日 HH:mm:ss";
    public static final String PATTERN_ASCTIME = "EEE MMM d HH:mm:ss yyyy";
    public static final String PATTERN_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss zzz";
    public static final String PATTERN_RFC1036 = "EEEE, dd-MMM-yy HH:mm:ss zzz";
    public static final String PATTERN_RFCCST = "EEEE, dd-MMM-yy HH:mm:ss 'CST'";
    public static final String VALID_DATA = "yy/MM";
    public static final String VALID_DATA_CHINESE = "yyyy年MM月";
    private static final int YEAR = 365 * 24 * 60 * 60;// 年
    private static final int MONTH = 30 * 24 * 60 * 60;// 月
    private static final int DAY = 24 * 60 * 60;// 天
    private static final int HOUR = 60 * 60;// 小时
    private static final int MINUTE = 60;// 分钟

    private static final String WEEK_FORMAT_STR = "%s(%s)";

    private static final Calendar defaultCalender = new GregorianCalendar();

    /**
     * @see Calendar .DAY_OF_WEEK
     */
    private static final String[] WEEK_TIME = {"", "周日", "周一", "周二", "周三", "周四", "周五", "周六"};//星期从 周日开始 = 1

    private static final Collection<String> DEFAULT_PATTERNS = Arrays.asList(
            FORMAT_DATE,
            FORMAT_TIME,
            FORMAT_DATE_TIME,
            FORMAT_DATE_OUT_SECOND_TIME,
            FORMAT_MONTH_DAY_TIME,
            PATTERN_ASCTIME,
            PATTERN_RFC1036,
            PATTERN_RFCCST,
            PATTERN_RFC1123
    );

    private static final Date DEFAULT_TWO_DIGIT_YEAR_START;

    static {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, Calendar.JANUARY, 1, 0, 0);
        DEFAULT_TWO_DIGIT_YEAR_START = calendar.getTime();
    }

    private DateHelper() {
        throw new UnsupportedOperationException("Instantiation operation is not supported.");
    }

    /**
     * 根据时间戳获取描述性时间(如：刚刚、3分钟前、1天前)
     *
     * @param timestamp 以毫秒为单位的时间戳
     * @return 返回描述性时间字符串
     */
    public static String getDescriptionTimeFromTimestamp(long timestamp) {
        long currentTime = System.currentTimeMillis();
        long timeGap = (currentTime - timestamp) / 1000;// 与现在时间相差秒数
        System.out.println("timeGap: " + timeGap);
        String timeString;
        if (timeGap > YEAR) {// 1年以上
            timeString = timeGap / YEAR + "年前";
        } else if (timeGap > MONTH) {// 1个月-1年
            timeString = timeGap / MONTH + "个月前";
        } else if (timeGap > DAY) {// 1天-1个月
            timeString = timeGap / DAY + "天前";
        } else if (timeGap > HOUR) {// 1小时-24小时
            timeString = timeGap / HOUR + "小时前";
        } else if (timeGap > MINUTE) {// 1分钟-59分钟
            timeString = timeGap / MINUTE + "分钟前";
        } else {// 1秒钟-59秒钟
            timeString = "刚刚";
        }
        return timeString;
    }


    /**
     * 根据时间戳获取描述性时间(如：刚刚、3分钟前、07-09 11：36)
     *
     * @param timestamp 以毫秒为单位的时间戳
     * @return 返回描述性时间字符串
     */
    public static String getPublishDate(long timestamp) {
        long currentTime = System.currentTimeMillis();
        long timeGap = (currentTime - timestamp) / 1000;// 与现在时间相差秒数
        System.out.println("timeGap: " + timeGap);
        String timeString;
        if (timeGap > YEAR) {// 1年以上
            timeString = timeGap / YEAR + "年前";
        } else if (timeGap > MONTH || timeGap > DAY) {// 1个月-1年 ,1天-1个月  07-09 11：36
            timeString = getFormatTimeFromTimestamp(timestamp, FORMAT_MONTH_DAY_OTHER_TIME);
        } else if (timeGap > HOUR) {// 1小时-24小时
            timeString = timeGap / HOUR + "小时前";
        } else if (timeGap > MINUTE) {// 1分钟-59分钟
            timeString = timeGap / MINUTE + "分钟前";
        } else {// 1秒钟-59秒钟
            timeString = "刚刚";
        }
        return timeString;
    }

    /**
     * 获取当前时间(单位：毫秒)
     */
    public static long getCuttentTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 获取格式化的当前时间(如：2016-10-01 23:59:00)
     *
     * @return 返回字符串形式的格式化当前日期
     */
    public static String getCuttentTimeFormat() {
        return getCuttentTimeFormat(FORMAT_DATE_TIME);
    }

    /**
     * 获取格式化的当前时间
     *
     * @param format 日期格式
     * @return 返回字符串形式的格式化当前日期
     */
    public static String getCuttentTimeFormat(String format) {
        return getFormatTimeFromTimestamp(System.currentTimeMillis(), format);
    }

    /**
     * 根据时间戳获取指定格式的时间(如:2016-10-01 23:59:00)
     *
     * @param timestamp 以毫秒为单位的时间戳
     * @param format    如果指定格式为null或空串则使用默认格式"yyyy-MM-dd HH:mm:ss"
     * @return 返回字符串形式的格式化日期
     */
    public static String getFormatTimeFromTimestamp(long timestamp, String format) {
        SimpleDateFormat dateFormat = DATE_FORMATER.get();
        if (TextUtils.isEmpty(format)) {
            dateFormat.applyPattern(FORMAT_DATE_TIME);
        } else {
            dateFormat.applyPattern(format);
        }
        return dateFormat.format(new Date(timestamp));
    }

    /**
     * 根据时间戳获取时间字符串，并根据指定的时间分割数partionSeconds来自动判断返回描述性时间还是指定格式的时间
     *
     * @param timestamp      以毫秒为单位的时间戳
     * @param partionSeconds 时间分割线
     * @param format         如果指定格式为null或空串则使用默认格式"yyyy-MM-dd HH:mm:ss"
     * @return 如果现在时间与指定的时间戳的秒数差大于这个分割线时则返回指定格式时间，否则返回描述性时间
     */
    public static String getMixTimeFromTimestamp(long timestamp, long partionSeconds, String format) {
        long currentTime = System.currentTimeMillis();
        long timeGap = (currentTime - timestamp) / 1000;// 与现在时间相差秒数
        if (timeGap <= partionSeconds) {
            return getDescriptionTimeFromTimestamp(timestamp);
        } else {
            return getFormatTimeFromTimestamp(timestamp, format);
        }
    }

    /**
     * 将日期字符串以指定格式转换为Date
     *
     * @param dateValue 日期字符串
     * @param format    指定的日期格式，如果为null或空串则使用指定的格式"yyyy-MM-dd HH:mm:ss"
     * @return 返回格式化的日期
     */
    public static Date formatDate(String dateValue, String format) {
        SimpleDateFormat dateFormat = DATE_FORMATER.get();
        if (TextUtils.isEmpty(format)) {
            dateFormat.applyPattern(FORMAT_DATE_TIME);
        } else {
            dateFormat.applyPattern(format);
        }
        try {
            return dateFormat.parse(dateValue);
        } catch (ParseException e) {
            return new Date();
        }
    }

    /**
     * 将Date以指定格式转换为日期时间字符串
     *
     * @param date 日期
     * @return 返回字符串形式的格式化日期
     */
    public static String formatDateValue(Date date) {
        return formatDateValue(date, FORMAT_DATE_TIME);
    }

    /**
     * 将Date以指定格式转换为日期时间字符串
     *
     * @param date   日期
     * @param format 指定的日期时间格式，如果为null或空串则使用指定的格式"yyyy-MM-dd HH:mm:ss"
     * @return 返回字符串形式的格式化日期
     */
    public static String formatDateValue(Date date, String format) {
        SimpleDateFormat dateFormat = DATE_FORMATER.get();
        if (TextUtils.isEmpty(format)) {
            dateFormat.applyPattern(FORMAT_DATE_TIME);
        } else {
            dateFormat.applyPattern(format);
        }
        return dateFormat.format(date);
    }

    /**
     * 将日期格式化
     *
     * @param dateValue 日期的字符串形式
     * @return 返回字符串形式的格式化日期
     */
    public static String formatDateValue(String dateValue) {
        return formatDateValue(dateValue, FORMAT_DATE_TIME);
    }

    /**
     * 将日期格式化
     *
     * @param dateValue 日期的字符串形式
     * @param format    日期格式
     * @return 返回字符串形式的格式化日期
     */
    public static String formatDateValue(String dateValue, String format) {
        if (TextUtils.isEmpty(dateValue)) {
            return "";
        }
        SimpleDateFormat dateFormat = DATE_FORMATER.get();
        dateFormat.setTimeZone(TimeZone.getDefault());
        if (TextUtils.isEmpty(format)) {
            dateFormat.applyPattern(FORMAT_DATE_TIME);
        } else {
            dateFormat.applyPattern(format);
        }
        return dateFormat.format(new Date(dateValue));
    }

    /**
     * 计算指定日期和当前日期相差多少毫秒
     *
     * @param time1 日期格式
     * @return 返回指定日期和当前日期相差毫秒数
     */
    public static long getIntervalTime(String time1, String time2) {
        return getIntervalTime(time1, time2, FORMAT_DATE_TIME);
    }

    /**
     * 计算指定日期和当前日期相差多少毫秒
     *
     * @param time1 日期格式
     * @return 返回指定日期和当前日期相差毫秒数
     */
    public static long getIntervalTime(String time1, String time2, String format) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
            dateFormat.applyPattern(format);
            return getIntervalTime(dateFormat.parse(time1).getTime(), dateFormat.parse(time2).getTime());//输出结果
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 计算指定日期和当前日期相差多少毫秒
     *
     * @param time1 日期格式
     * @return 返回指定日期和当前日期相差毫秒数
     */
    public static long getIntervalTime(long time1, long time2) {
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long nh = 1000 * 60 * 60;// 一小时的毫秒数
        long nm = 1000 * 60;// 一分钟的毫秒数
        long ns = 1000;// 一秒钟的毫秒数
        long interval = time1 - time2;// 获得两个时间的毫秒时间差异
        // long day = diff / nd;// 计算差多少天
        // long hour = diff % nd / nh;// 计算差多少小时
        // long min = diff % nd % nh / nm;// 计算差多少分钟
        long sec = interval % nd % nh % nm / ns;// 计算差多少秒
        return sec;//输出结果
    }

    /**
     * 计算指定日期和当前日期相差多少毫秒
     *
     * @param dateValue 日期格式
     * @return 返回指定日期和当前日期相差毫秒数
     */
    public static long getIntervalTime(String dateValue) {
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long nh = 1000 * 60 * 60;// 一小时的毫秒数
        long nm = 1000 * 60;// 一分钟的毫秒数
        long ns = 1000;// 一秒钟的毫秒数
        long diff;// 获得两个时间的毫秒时间差异
        try {
            SimpleDateFormat dateFormat = DATE_FORMATER.get();
            dateFormat.applyPattern(FORMAT_DATE_TIME);
            diff = dateFormat.parse(dateValue).getTime() - System.currentTimeMillis();
            // long day = diff / nd;// 计算差多少天
            // long hour = diff % nd / nh;// 计算差多少小时
            // long min = diff % nd % nh / nm;// 计算差多少分钟
            long sec = diff % nd % nh % nm / ns;// 计算差多少秒
            return sec;//输出结果
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String formatDateTime(String dateTime) {
        SimpleDateFormat greenwichDate = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        greenwichDate.setTimeZone(TimeZone.getTimeZone("GMT"));// 时区设为格林尼治
        try {
            Date date = greenwichDate.parse(dateTime);
            return DATE_FORMATER.get().format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将毫秒数转换为格式化的时间(格式：HH:mm:ss或mm:ss)
     *
     * @param timeMillis 毫秒数
     */
    public static String getFormatTime(long timeMillis) {
        SimpleDateFormat timeFormate;
        if (timeMillis > DateUtils.HOUR_IN_MILLIS) {
            timeFormate = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            timeFormate.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        } else {
            timeFormate = new SimpleDateFormat("mm:ss", Locale.getDefault());
        }
        return timeFormate.format(new Date(timeMillis));
    }


    /**
     * 使用指定的日期格式解析日期
     *
     * @param dateValue 需要解析的日期值
     * @return 返回格式化的日期
     * @throws DateParseException 如果没有可以解析该日期的格式则抛出异常
     * @see #parseDate(String, Collection)
     */
    public static Date parseDate(String dateValue) throws DateParseException {
        return parseDate(dateValue, DEFAULT_PATTERNS);
    }

    /**
     * 使用指定的日期格式解析日期
     *
     * @param dateValue   需要解析的日期值
     * @param dateFormats 需要使用的日期格式
     * @return 返回格式化的日期
     * @throws DateParseException 如果没有可以解析该日期的格式则抛出异常
     * @see #parseDate(String, Collection, Date)
     */
    public static Date parseDate(String dateValue, Collection<String> dateFormats) throws DateParseException {
        return parseDate(dateValue, dateFormats, DEFAULT_TWO_DIGIT_YEAR_START);
    }

    /**
     * 使用指定的日期格式解析日期
     *
     * @param dateValue   需要解析的日期值
     * @param dateFormats 需要使用的日期格式
     * @param startDate   设置100年周期的两位数年份，该年份将被解释为从用户指定的日期开始
     * @return 返回格式化的日期
     * @throws DateParseException 如果没有可以解析该日期的格式则抛出异常
     */
    public static Date parseDate(String dateValue, Collection<String> dateFormats, Date startDate) throws DateParseException {
        if (dateValue == null) {
            throw new IllegalArgumentException("dateValue is null.");
        }
        if (dateFormats == null) {
            dateFormats = DEFAULT_PATTERNS;
        }
        if (startDate == null) {
            startDate = DEFAULT_TWO_DIGIT_YEAR_START;
        }
        if (dateValue.length() > 1 && dateValue.startsWith("'") && dateValue.endsWith("'")) {
            dateValue = dateValue.substring(1, dateValue.length() - 1);
        }
        SimpleDateFormat dateParser = null;
        for (String dateFormat : dateFormats) {
            if (dateParser == null) {
                dateParser = new SimpleDateFormat(dateFormat, Locale.US);
                dateParser.setTimeZone(TimeZone.getTimeZone("GMT"));
                dateParser.set2DigitYearStart(startDate);
            } else {
                dateParser.applyPattern(dateFormat);
            }
            try {
                return dateParser.parse(dateValue);
            } catch (ParseException pe) {
                // ignore this exception, we will try the next format
            }
        }
        // we were unable to parse the date
        throw new DateParseException("Unable to parse the date " + dateValue);
    }

    public final static ThreadLocal<SimpleDateFormat> DATE_FORMATER = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        }
    };

    private static SimpleDateFormat sdfBefore = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

    /**
     * 日期格式转换
     *
     * @param formerFormat  转换前的日期格式
     * @param currentFormat 转换后的日期格式
     * @param dateTime      转换的时间
     * @return 新的时间
     */
    public static String dateExchange(String formerFormat, String currentFormat, String dateTime) {
        String result = null;
        if (TextUtils.isEmpty(currentFormat) || TextUtils.isEmpty(formerFormat)) {
            return result;
        }
        try {
            SimpleDateFormat dateFormat = DATE_FORMATER.get();
            dateFormat.applyPattern(formerFormat);
            Date date = dateFormat.parse(dateTime);
            dateFormat.applyPattern(currentFormat);
            result = dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 格式化日期  12-21(周四)
     *
     * @param time String
     * @return String lik 12-21(周四)
     */
    public static String formatWithWeek(String time) {
        String result = null;
        if (TextUtils.isEmpty(time)) {
            return "";
        }
        Date date = formatDate(time, null);
        defaultCalender.setTime(date);
        int i = defaultCalender.get(Calendar.DAY_OF_WEEK);
        String dateValue = formatDateValue(date, FORMAT_MONTH_DAY);
        if (i < WEEK_TIME.length) {
            result = String.format(WEEK_FORMAT_STR, dateValue, WEEK_TIME[i]);
        }
        return result;
    }

    public static boolean isToday(String time) {
        if (TextUtils.isEmpty(time)) {
            return false;
        }
        Date date = formatDate(time, null);
        String value = formatDateValue(date, FORMAT_DATE);
        String today = getCuttentTimeFormat(FORMAT_DATE);
        if (today.equals(value)) {
            return true;
        }
        return false;
    }


    public static String formatDateTime(String dataTime, String format) {
        if (TextUtils.isEmpty(dataTime)) {
            return "";
        }
        Date date = formatDate(dataTime, FORMAT_DATE_TIME);
        return formatDateValue(date, format);
    }

    public static String formatDateTime(String dataTime, String format, String beforeFormat) {
        if (TextUtils.isEmpty(dataTime)) {
            return "";
        }
        Date date = formatDate(dataTime, TextUtils.isEmpty(beforeFormat) ? FORMAT_DATE_TIME : beforeFormat);
        return formatDateValue(date, format);
    }

    /**
     * 日期格式字符串转换成时间戳
     *
     * @param date   字符串日期
     * @param format 如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static long date2TimeStamp(String date, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return Long.parseLong(String.valueOf(sdf.parse(date).getTime()));
        } catch (Exception e) {
            e.printStackTrace();

        }
        return 0;
    }
}
