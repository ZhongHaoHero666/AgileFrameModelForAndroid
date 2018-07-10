package com.android.szh.common.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.android.szh.common.entity.GreenDaoTestEntity;

import org.greenrobot.greendao.AbstractDao;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库辅助类
 *
 * @author liyunlong
 * @date 2017/10/20 18:16
 */
final class GreenDaoSQLiteOpenHelper extends DaoMaster.OpenHelper {

    private static final List<Class<? extends AbstractDao<?, ?>>> ENTITIE_DAOS = new ArrayList<Class<? extends AbstractDao<?, ?>>>() {
        {
            add(GreenDaoTestEntityDao.class);              // 用于测试的dao
        }
    };

    public GreenDaoSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        GreenDaoUpgradeHelper.migrate(db, ENTITIE_DAOS);
    }

}
