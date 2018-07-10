package com.android.szh.common.greendao;

import android.database.sqlite.SQLiteDatabase;


import com.android.szh.common.BaseApplication;

import org.greenrobot.greendao.database.Database;

/**
 * GreenDao辅助类
 *
 * @author liyunlong
 * @date 2017/10/20 18:21
 */
public final class GreenDaoManager {

    private static final String DATABASE_NAME = "AglieFrame.db"; //你自定义的数据库的名称

    private DaoMaster mDaoMaster; // GreenDao的顶级对象，作为数据库对象、用于创建表和删除表
    private DaoSession mDaoSession; // 管理所有的Dao对象，Dao对象中存在着增删改查等API
    private Database mDatabase; // GreenDao的数据库对象
    private GreenDaoSQLiteOpenHelper mOpenHelper; // 创建SQLite数据库的SQLiteOpenHelper的具体实现
    private static volatile GreenDaoManager mInstance; // GreenDao辅助类实例

    private GreenDaoManager() {
        if (mInstance == null) {
            BaseApplication context = BaseApplication.getInstance(); // 获取上下文
            mOpenHelper = new GreenDaoSQLiteOpenHelper(context, DATABASE_NAME, null); // 创建数据库
            SQLiteDatabase db = mOpenHelper.getWritableDatabase(); // 获取可写数据库
            mDaoMaster = new DaoMaster(db);// 获取数据库对象
            mDaoSession = mDaoMaster.newSession();// 获取Dao对象管理者
            mDatabase = mDaoSession.getDatabase();
        }
    }

    public static GreenDaoManager getInstance() {
        if (mInstance == null) {
            synchronized (GreenDaoManager.class) {
                if (mInstance == null) {
                    mInstance = new GreenDaoManager();
                }
            }
        }
        return mInstance;
    }

    public DaoMaster getMaster() {
        return mDaoMaster;
    }

    public DaoSession getSession() {
        return mDaoSession;
    }

    public DaoSession getNewSession() {
        return mDaoMaster.newSession();
    }

    public Database getDatabase() {
        return mDatabase;
    }

    /**
     * 关闭数据库
     */
    public void closeAll() {
        closeHelper();
        closeDaoSession();
        closeDatabase();
    }

    private void closeDaoSession() {
        if (mDaoSession != null) {
            mDaoSession.clear();
            mDaoSession = null;
        }
    }

    private void closeHelper() {
        if (mOpenHelper != null) {
            mOpenHelper.close();
            mOpenHelper = null;
        }
    }

    private void closeDatabase() {
        if (mDatabase != null) {
            mDatabase.close();
            mDatabase = null;
        }
    }
}
