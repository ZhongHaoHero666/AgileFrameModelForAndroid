package com.android.szh.agileframedemo.contract;



import com.android.szh.agileframedemo.entry.CityInfo;
import com.android.szh.common.base.BasePresenter;
import com.android.szh.common.mvp.IModel;
import com.android.szh.common.mvp.IView;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * Created by sunzhonghao on 2018/5/16.
 * desc: 供测试使用的契约管理接口
 */
public interface MVPTestContract {

    interface Model extends IModel {
        Observable<ResponseBody> getCityInfo();
    }

    interface View extends IView {
        void handleCityInfoResult(CityInfo cityInfo);
    }

    abstract class Presenter<Model> extends BasePresenter<Model, View> {
        public abstract void loadCityInfo();
    }
}
