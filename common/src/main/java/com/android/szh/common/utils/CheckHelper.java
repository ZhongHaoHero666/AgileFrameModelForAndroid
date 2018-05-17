package com.android.szh.common.utils;

import android.os.Looper;
import android.text.TextUtils;


import java.util.regex.Pattern;

/**
 * 检查工具类
 */
public class CheckHelper {
    /**
     * 手机号的校验规则
     */
    public final static String PHONE_REX = "^((13[0-9])|(15[^4,\\\\d])|(18[0-9])|(14[0-9])|(17[0-9])|(19[8-9])|(166))\\d{8}$";

    private CheckHelper() {
        throw new UnsupportedOperationException("Instantiation operation is not supported.");
    }

    public static <T> T checkNotNull(T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }

    public static boolean checkMainThread() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }

    /**
     * 检查是否是手机号格式
     */
    public static boolean isMobileNO(String mobiles) {
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(PHONE_REX);
    }

    //________________________以下为身份证校验逻辑_____________________________

    /**
     * 大陆地区地域编码最大值
     **/
    public static final int MAX_MAINLAND_AREACODE = 659004;
    /**
     * 大陆地区地域编码最小值
     **/
    public static final int MIN_MAINLAND_AREACODE = 110000;
    /**
     * 香港地域编码值
     **/
    public static final int HONGKONG_AREACODE = 810000;                                                                                                                                                 // 香港地域编码值
    /**
     * 台湾地域编码值
     **/
    public static final int TAIWAN_AREACODE = 710000;
    /**
     * 澳门地域编码值
     **/
    public static final int MACAO_AREACODE = 820000;

    /**
     * 数字正则
     **/
    public static final String regexNum = "^[0-9]*$";
    /**
     * 闰年生日正则
     **/
    public static final String regexBirthdayInLeapYear = "^((19[0-9]{2})|(200[0-9])|(201[0-5]))((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))$";
    /**
     * 平年生日正则
     **/
    public static final String regexBirthdayInCommonYear = "^((19[0-9]{2})|(200[0-9])|(201[0-5]))((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))$";


    /**
     * 身份证格式强校验
     * 身份证号码验证
     * 1、号码的结构 公民身份号码是特征组合码，由十七位数字本体码和一位校验码组成。排列顺序从左至右依次为：六位数字地址码， 八位数字出生日期码，三位数字顺序码和一位数字校验码。
     * 2、地址码(前六位数）表示编码对象常住户口所在县(市、旗、区)的行政区划代码，按GB/T2260的规定执行。
     * 3、出生日期码（第七位至十四位） 表示编码对象出生的年、月、日，按GB/T7408的规定执行，年、月、日代码之间不用分隔符。
     * 4、顺序码（第十五位至十七位） 表示在同一地址码所标识的区域范围内，对同年、同月、同日出生的人编定的顺序号， 顺序码的奇数分配给男性，偶数分配给女性。
     * 5、校验码（第十八位数）
     * （1）十七位数字本体码加权求和公式 S =Sum(Ai * Wi), i = 0, ... , 16 ，先对前17位数字的权求和 Ai:表示第i位置上的身份证号码数字值 Wi:表示第i位置上的加权因子 Wi: 7 9 10 5 8 4 2 1 6 3 7 9 10 58 4 2
     * （2）计算模 Y = mod(S, 11)
     * （3）通过模得到对应的校验码 Y: 0 1 2 3 4 5 6 7 8 9 10 校验码: 1 0 X 9 8 7 6 5 4 3 2
     */
    public static final boolean strongVerifyIdNumber(String idNumber) {
        if (TextUtils.isEmpty(idNumber)) {
            return false;
        }
        idNumber = idNumber.trim();

        if (!checkIdNumberRegex(idNumber)) {
            return false;
        }
        if (!checkIdNumberArea(idNumber.substring(0, 6))) {
            return false;
        }
        idNumber = convertFifteenToEighteen(idNumber);
        if (!checkBirthday(idNumber.substring(6, 14))) {
            return false;
        }
        if (!checkIdNumberVerifyCode(idNumber)) {
            return false;
        }
        return true;
    }

    /**
     * 身份证正则校验
     */
    private static boolean checkIdNumberRegex(String idNumber) {
        return Pattern.matches("^([0-9]{17}[0-9Xx])|([0-9]{15})$", idNumber);
    }

    /**
     * 身份证地区码检查
     */
    private static boolean checkIdNumberArea(String idNumberArea) {
        int areaCode = Integer.parseInt(idNumberArea);
        if (areaCode == HONGKONG_AREACODE || areaCode == MACAO_AREACODE || areaCode == TAIWAN_AREACODE) {
            return true;
        }
        if (areaCode <= MAX_MAINLAND_AREACODE && areaCode >= MIN_MAINLAND_AREACODE) {
            return true;
        }
        return false;
    }

    /**
     * 将15位身份证转换为18位
     */
    private static String convertFifteenToEighteen(String idNumber) {
        if (15 != idNumber.length()) {
            return idNumber;
        }
        idNumber = idNumber.substring(0, 6) + "19" + idNumber.substring(6, 15);
        idNumber = idNumber + getVerifyCode(idNumber);
        return idNumber;
    }

    /**
     * 根据身份证前17位计算身份证校验码
     */
    private static String getVerifyCode(String idNumber) {
        if (!Pattern.matches(regexNum, idNumber.substring(0, 17))) {
            return null;
        }
        String[] ValCodeArr = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
        String[] Wi = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2"};

        int sum = 0;
        for (int i = 0; i < 17; i++) {
            sum = sum + Integer.parseInt(String.valueOf(idNumber.charAt(i))) * Integer.parseInt(Wi[i]);
        }
        return ValCodeArr[sum % 11];
    }

    /**
     * 身份证出生日期嘛检查
     */
    private static boolean checkBirthday(String idNumberBirthdayStr) {
        Integer year = null;
        try {
            year = Integer.valueOf(idNumberBirthdayStr.substring(0, 4));
        } catch (Exception e) {
        }
        if (null == year) {
            return false;
        }
        if (isLeapYear(year)) {
            return Pattern.matches(regexBirthdayInLeapYear, idNumberBirthdayStr);
        } else {
            return Pattern.matches(regexBirthdayInCommonYear, idNumberBirthdayStr);
        }
    }

    /**
     * 判断是否为闰年
     */
    private static boolean isLeapYear(int year) {
        return (year % 400 == 0) || (year % 100 != 0 && year % 4 == 0);
    }

    /**
     * 身份证校验码检查
     */
    private static boolean checkIdNumberVerifyCode(String idNumber) {
        return getVerifyCode(idNumber).equalsIgnoreCase(idNumber.substring(17));
    }

}
