package com.android.szh.common.greendao;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;


import com.android.szh.common.logger.Logger;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.StandardDatabase;
import org.greenrobot.greendao.internal.DaoConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * GreenDao数据库升级处理辅助类
 *
 * @author liyunlong
 * @date 2017/10/20 18:14
 */
final class GreenDaoUpgradeHelper {

    private static String TAG = "GreenDaoUpgradeHelper";
    private static final String SQLITE_TEMP_TABLE_SUFFIX = "_TEMP";
    private static final String SQLITE_MASTER = "sqlite_master";
    private static final String SQLITE_TEMP_MASTER = "sqlite_temp_master";
    private static final String METHOD_DROP_TABLE = "dropTable";
    private static final String METHOD_CREATE_TABLE = "createTable";

    /**
     * 迁移数据库
     *
     * @param db         数据库对象
     * @param daoClasses 所有的Dao对象
     */
    public static void migrate(SQLiteDatabase db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        migrate(db, Arrays.asList(daoClasses));
    }

    /**
     * 迁移数据库
     *
     * @param db         数据库对象
     * @param daoClasses 所有的Dao对象
     */
    public static void migrate(SQLiteDatabase db, List<Class<? extends AbstractDao<?, ?>>> daoClasses) {
        if (daoClasses == null || daoClasses.isEmpty()) {
            return;
        }
        Logger.i(TAG, "数据库版本升级开始...");
        Database database = new StandardDatabase(db);
        Logger.i(TAG, "旧数据库版本：" + db.getVersion());
        Logger.i(TAG, "生成临时表开始...");
        generateTempTables(database, daoClasses);
        Logger.i(TAG, "生成临时表完成！！！");

        dropAllTables(database, true, daoClasses);
        createAllTables(database, false, daoClasses);

        Logger.i(TAG, "恢复数据开始...");
        restoreData(database, daoClasses);
        Logger.i(TAG, "恢复数据完成！！！");
        Logger.i(TAG, "数据库版本升级完成！！！");
    }

    /**
     * 生成临时表
     *
     * @param db         数据库对象
     * @param daoClasses 所有的Dao对象
     */
    private static void generateTempTables(Database db, List<Class<? extends AbstractDao<?, ?>>> daoClasses) {
        for (Class<? extends AbstractDao<?, ?>> daoClass : daoClasses) {
            String tempTableName = null;

            DaoConfig daoConfig = new DaoConfig(db, daoClass);
            String tableName = daoConfig.tablename;
            if (!isTableExists(db, false, tableName)) { // 判断指定的数据库表是否存在
                Logger.i(TAG, "新数据库表：" + tableName);
                continue;
            }
            try {
                tempTableName = daoConfig.tablename.concat(SQLITE_TEMP_TABLE_SUFFIX);
                db.execSQL(String.format("DROP TABLE IF EXISTS %s;", tempTableName));

                db.execSQL(String.format("CREATE TEMPORARY TABLE %s AS SELECT * FROM %s;", tempTableName, tableName));
                Logger.i(TAG, "数据库表：" + tableName + "\n所有列：" + getColumnsStr(daoConfig));
                Logger.i(TAG, "生成临时表：" + tempTableName);
            } catch (SQLException e) {
                Log.e(TAG, "生成临时表失败：" + tempTableName, e);
            }
        }
    }

    /**
     * 判断指定的数据库表是否存在
     *
     * @param db        数据库对象
     * @param isTemp    是否是临时表
     * @param tableName 数据库表名称
     */
    private static boolean isTableExists(Database db, boolean isTemp, String tableName) {
        if (db == null || TextUtils.isEmpty(tableName)) {
            return false;
        }
        String dbName = isTemp ? SQLITE_TEMP_MASTER : SQLITE_MASTER;
        String sql = String.format("SELECT COUNT(*) FROM %s WHERE type = ? AND name = ?", dbName);
        Cursor cursor = null;
        int count = 0;
        try {
            cursor = db.rawQuery(sql, new String[]{"table", tableName});
            if (cursor == null || !cursor.moveToFirst()) {
                return false;
            }
            count = cursor.getInt(0);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return count > 0;
    }


    /**
     * 删除所有数据库表
     *
     * @param db         数据库对象
     * @param ifExists   是否存在
     * @param daoClasses 所有的Dao对象
     */
    private static void dropAllTables(Database db, boolean ifExists, @NonNull List<Class<? extends AbstractDao<?, ?>>> daoClasses) {
        reflectMethod(db, METHOD_DROP_TABLE, ifExists, daoClasses);
        Logger.i(TAG, "删除所有数据库表！！！");
    }

    /**
     * 创建所有数据库表
     *
     * @param db          数据库对象
     * @param ifNotExists 是否不存在
     * @param daoClasses  所有的Dao对象
     */
    private static void createAllTables(Database db, boolean ifNotExists, @NonNull List<Class<? extends AbstractDao<?, ?>>> daoClasses) {
        reflectMethod(db, METHOD_CREATE_TABLE, ifNotExists, daoClasses);
        Logger.i(TAG, "创建所有数据库表！！！");
    }

    /**
     * 通过反射调用{@link AbstractDao}中定义的SQL执行方法
     */
    private static void reflectMethod(Database db, @NonNull String methodName, boolean isExists, @NonNull List<Class<? extends AbstractDao<?, ?>>> daoClasses) {
        if (daoClasses.isEmpty()) {
            return;
        }
        try {
            for (Class cls : daoClasses) {
                Method method = cls.getDeclaredMethod(methodName, Database.class, boolean.class);
                method.invoke(null, db, isExists);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 恢复数据
     *
     * @param db         数据库对象
     * @param daoClasses 所有的Dao对象
     */
    private static void restoreData(Database db, List<Class<? extends AbstractDao<?, ?>>> daoClasses) {
        if (daoClasses == null || daoClasses.isEmpty()) {
            return;
        }
        for (Class<? extends AbstractDao<?, ?>> daoClass : daoClasses) {
            DaoConfig daoConfig = new DaoConfig(db, daoClass);
            String tableName = daoConfig.tablename;
            String tempTableName = daoConfig.tablename.concat(SQLITE_TEMP_TABLE_SUFFIX);

            if (!isTableExists(db, true, tempTableName)) {
                continue;
            }

            try {
                // 从临时数据库表获取所有列的集合
                List<String> columns = getColumns(db, tempTableName);
                ArrayList<String> properties = new ArrayList<>(columns.size());
                for (int j = 0; j < daoConfig.properties.length; j++) {
                    String columnName = daoConfig.properties[j].columnName;
                    if (columns.contains(columnName)) {
                        properties.add(columnName);
                    }
                }
                if (properties.size() > 0) {
                    final String columnSQL = TextUtils.join(",", properties);

                    db.execSQL(String.format("INSERT INTO %s (%s) SELECT %s FROM %s;", tableName, columnSQL, columnSQL, tempTableName));
                    Logger.i(TAG, "恢复数据到数据库表：" + tableName);
                }
                db.execSQL(String.format("DROP TABLE %s", tempTableName));
                Logger.i(TAG, "删除临时数据库表：" + tempTableName);
            } catch (SQLException e) {
                Log.e(TAG, "从临时表恢复数据失败：" + tempTableName, e);
            }
        }
    }

    /**
     * 返回数据库表的所有列的集合
     *
     * @param db        数据库对象
     * @param tableName 数据库表名称
     */
    private static List<String> getColumns(Database db, String tableName) {
        List<String> columns = null;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(String.format("SELECT * FROM %s limit 0", tableName), null);
            if (null != cursor && cursor.getColumnCount() > 0) {
                columns = Arrays.asList(cursor.getColumnNames());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            if (null == columns)
                columns = new ArrayList<>();
        }
        return columns;
    }

    private static String getColumnsStr(DaoConfig daoConfig) {
        if (daoConfig == null) {
            return "no columns";
        }
        int length = daoConfig.allColumns.length;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(daoConfig.allColumns[i]);
            builder.append(",");
        }
        if (builder.length() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

}
