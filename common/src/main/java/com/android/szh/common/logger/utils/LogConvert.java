package com.android.szh.common.logger.utils;


import android.text.TextUtils;


import com.android.szh.common.logger.Logger;
import com.android.szh.common.logger.parser.IParser;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 日志转换工具
 *
 * Created by sunzhonghao
 * @date 2017/7/21 17:05
 */
public class LogConvert {

    /**
     * 获取数组的纬度
     */
    private static int getArrayDimension(Object object) {
        int dim = 0;
        for (int i = 0; i < object.toString().length(); ++i) {
            if (object.toString().charAt(i) == '[') {
                ++dim;
            } else {
                break;
            }
        }
        return dim;
    }

    /**
     * 是否为数组
     */
    private static boolean isArray(Object object) {
        return object.getClass().isArray();
    }

    /**
     * 获取数组类型
     *
     * @param object 如L为int型
     */
    private static char getType(Object object) {
        if (isArray(object)) {
            String str = object.toString();
            return str.substring(str.lastIndexOf("[") + 1, str.lastIndexOf("[") + 2).charAt(0);
        }
        return 0;
    }

    /**
     * 遍历数组
     *
     * @param result
     * @param array
     */
    private static void traverseArray(StringBuilder result, Object array) {
        if (isArray(array)) {
            if (getArrayDimension(array) == 1) {
                switch (getType(array)) {
                    case 'I':
                        result.append(Arrays.toString((int[]) array));
                        break;
                    case 'D':
                        result.append(Arrays.toString((double[]) array));
                        break;
                    case 'Z':
                        result.append(Arrays.toString((boolean[]) array));
                        break;
                    case 'B':
                        result.append(Arrays.toString((byte[]) array));
                        break;
                    case 'S':
                        result.append(Arrays.toString((short[]) array));
                        break;
                    case 'J':
                        result.append(Arrays.toString((long[]) array));
                        break;
                    case 'F':
                        result.append(Arrays.toString((float[]) array));
                        break;
                    case 'L':
                        Object[] objects = (Object[]) array;
                        result.append("[");
                        for (int i = 0; i < objects.length; ++i) {
                            result.append(objectToString(objects[i]));
                            if (i != objects.length - 1) {
                                result.append(",");
                            }
                        }
                        result.append("]");
                        break;
                    default:
                        result.append(Arrays.toString((Object[]) array));
                        break;
                }
            } else {
                result.append("[");
                for (int i = 0; i < ((Object[]) array).length; i++) {
                    traverseArray(result, ((Object[]) array)[i]);
                    if (i != ((Object[]) array).length - 1) {
                        result.append(",");
                    }
                }
                result.append("]");
            }
        } else {
            result.append("not a array!!");
        }
    }

    /**
     * 将数组内容转化为字符串
     */
    private static String parseArray(Object array) {
        StringBuilder result = new StringBuilder();
        traverseArray(result, array);
        return result.toString();
    }

    /**
     * 将对象转化为String
     *
     * @param object
     */
    public static <T> String objectToStringWithFormat(T object) {
        String value = objectToString(object, 0);
        return fotmatValue(value);
    }

    /**
     * 将对象转化为String
     */
    public static <T> String objectToString(T object) {
        return objectToString(object, 0);
    }

    /**
     * 将对象转化为String
     *
     * @param object
     * @param childLevel 对象包含子对象层级
     */
    private static <T> String objectToString(T object, int childLevel) {
        if (object == null) {
            return "Object[object is null]";
        }
        if (childLevel > LogConstants.MAX_CHILD_LEVEL) {
            return object.toString();
        }
        List<IParser> parseList = Logger.getLogConfig().getParseList(); // 获取默认解析类
        if (parseList != null && parseList.size() > 0) {
            for (IParser parser : parseList) {
                Class<T> clazz = parser.parseClassType();
                if (clazz.isAssignableFrom(object.getClass())) {
                    return parser.parseString(object);
                }
            }
        }
        if (isArray(object)) {
            return parseArray(object);
        }
        if (object.toString().startsWith(object.getClass().getName() + "@")) {
            StringBuilder builder = new StringBuilder();
            getClassFields(object.getClass(), builder, object, false, childLevel);
            Class superClass = object.getClass().getSuperclass();
            while (!superClass.equals(Object.class)) {
                getClassFields(superClass, builder, object, true, childLevel);
                superClass = superClass.getSuperclass();
            }
            return builder.toString();
        } else {
            // 若对象重写toString()方法默认走toString()
            return object.toString();
        }
    }

    /**
     * 是否为静态内部类
     */
    private static boolean isStaticInnerClass(Class clazz) {
        if (clazz != null && clazz.isMemberClass()) {
            int modifiers = clazz.getModifiers();
            if ((modifiers & Modifier.STATIC) == Modifier.STATIC) {
                return true;
            }
        }
        return false;
    }

    /**
     * 拼接class的字段和值
     *
     * @param clazz
     * @param builder
     * @param object      对象
     * @param isSubClass  死否为子class
     * @param childOffset 递归解析属性的层级
     */
    private static void getClassFields(Class clazz, StringBuilder builder, Object object, boolean isSubClass, int childOffset) {
        if (clazz.equals(Object.class)) {
            return;
        }
        if (isSubClass) {
            builder.append(LogConstants.LINE_SEPARATOR).append(LogConstants.LINE_SEPARATOR).append("=> ");
        }
        String breakLine = "";
        builder.append(clazz.getSimpleName()).append(" {");
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; ++i) {
            Field field = fields[i];
            field.setAccessible(true);
            if (clazz.isMemberClass() && !isStaticInnerClass(clazz) && i == 0) {
                continue;
            }
            Object subObject = null;
            try {
                subObject = field.get(object);
            } catch (IllegalAccessException e) {
                subObject = e;
            } finally {
                if (subObject != null) {
                    // 解决Instant Run情况下内部类死循环的问题
                    if (!isStaticInnerClass(clazz)
                            && (field.getName().equals("$change")
                            || field.getName().equalsIgnoreCase("this$0"))) {
                        continue;
                    }
                    if (subObject instanceof String) {
                        subObject = "\"" + subObject + "\"";
                    } else if (subObject instanceof Character) {
                        subObject = "\'" + subObject + "\'";
                    }
                    if (childOffset < LogConstants.MAX_CHILD_LEVEL) {
                        subObject = objectToString(subObject, childOffset + 1);
                    }
                }
                String formatString = breakLine + "%s = %s, ";
                builder.append(String.format(formatString, field.getName(), subObject == null ? "null" : subObject.toString()));
            }
        }
        if (builder.toString().endsWith("{")) {
            builder.append("}");
        } else {
            builder.replace(builder.length() - 2, builder.length() - 1, breakLine + "}");
        }
    }

    /**
     * 打印分割线
     *
     * @param dir
     */
    public static String printDividingLine(int dir) {
        switch (dir) {
            case LogConstants.DIVIDER_TOP:
                return "╔═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════";
            case LogConstants.DIVIDER_BOTTOM:
                return "╚═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════";
            case LogConstants.DIVIDER_NORMAL:
                return "║ ";
            case LogConstants.DIVIDER_CENTER:
                return "╟───────────────────────────────────────────────────────────────────────────────────────────────────────────────────";
            default:
                break;
        }
        return "";
    }


    /**
     * 长字符串转化为List
     */
    public static List<String> largeStringToList(String msg) {
        List<String> stringList = new ArrayList<>();
        int index = 0;
        int maxLength = LogConstants.LINE_MAX;
        int countOfSub = msg.length() / maxLength;
        if (countOfSub > 0) {
            for (int i = 0; i < countOfSub; i++) {
                String sub = msg.substring(index, index + maxLength);
                stringList.add(sub);
                index += maxLength;
            }
            stringList.add(msg.substring(index, msg.length()));
        } else {
            stringList.add(msg);
        }
        return stringList;
    }

    /**
     * 格式化
     */
    private static String fotmatValue(String value){
        if (!TextUtils.isEmpty(value) && value.contains("\n")) {
            value = value.replace("\n", "\n    ");
        }
        return value;
    }
}
