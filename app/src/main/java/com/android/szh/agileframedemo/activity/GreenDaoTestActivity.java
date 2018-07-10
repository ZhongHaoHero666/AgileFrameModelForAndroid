package com.android.szh.agileframedemo.activity;

import android.view.View;
import android.widget.TextView;

import com.android.szh.agileframedemo.R;
import com.android.szh.common.base.BaseActivity;
import com.android.szh.common.entity.GreenDaoTestEntity;
import com.android.szh.common.greendao.GreenDaoManager;
import com.android.szh.common.greendao.GreenDaoTestEntityDao;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by sunzhonghao on 2018/7/10.
 * desc:GreenDao 简单演示界面  其他复杂操作见封装的
 */

public class GreenDaoTestActivity extends BaseActivity {
    @BindView(R.id.tv_show_result)
    TextView tvShowResult;
    //最后一条插入的数据的id 索引
    long lastInsertIndex;

    @Override
    protected void initViews() {

    }

    GreenDaoTestEntityDao greenDaoTestEntityDao;
    Random random;

    @Override
    protected void loadData() {
        super.loadData();
        greenDaoTestEntityDao = GreenDaoManager.getInstance().getSession().getGreenDaoTestEntityDao();
        random = new Random();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_greendao_test;
    }

    @OnClick({R.id.btn_insert, R.id.btn_delete, R.id.btn_query, R.id.btn_updata})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_insert:
                //插入数据
                lastInsertIndex = greenDaoTestEntityDao.insert(new GreenDaoTestEntity(null, "姓名 : " + random.nextInt(10000), +random.nextInt(75)));
                tvShowResult.setText(String.valueOf("插入成功 ：" + lastInsertIndex));
                break;
            case R.id.btn_delete:
                //删除数据
                try {
                    greenDaoTestEntityDao.deleteAll();
                } catch (Exception e) {
                    tvShowResult.setText(String.valueOf("error = " + e.getMessage()));
                }
                tvShowResult.setText("删除成功");
                break;
            case R.id.btn_query:
                //查询数据
                List<GreenDaoTestEntity> datas = greenDaoTestEntityDao.loadAll();
                if (datas == null || datas.size() <= 0) {
                    tvShowResult.setText("未查询到数据");
                    return;
                }
                StringBuilder stringBuilder = new StringBuilder();
                for (GreenDaoTestEntity data : datas) {
                    stringBuilder.append(data.toString() + "\n");
                }
                tvShowResult.setText(stringBuilder);
                break;
            case R.id.btn_updata:
                //更新id = 1 的数据
                try {
                    greenDaoTestEntityDao.update(new GreenDaoTestEntity(lastInsertIndex, "姓名 : 修改后", -1));
                } catch (Exception e) {
                    tvShowResult.setText(String.valueOf("error = " + e.getMessage()));
                }
                tvShowResult.setText("更新成功");
                break;
        }
    }

    @Override
    public void finish() {
        GreenDaoManager.getInstance().closeAll();
        super.finish();
    }
}
