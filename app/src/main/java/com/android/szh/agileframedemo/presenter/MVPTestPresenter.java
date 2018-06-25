package com.android.szh.agileframedemo.presenter;

import android.text.TextUtils;

import com.android.szh.agileframedemo.contract.MVPTestContract;
import com.android.szh.agileframedemo.entry.CityInfo;
import com.android.szh.agileframedemo.model.MVPTestModel;
import com.android.szh.common.http.exception.HttpException;
import com.android.szh.common.logger.Logger;
import com.android.szh.common.rxjava.HttpObserver;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;

/**
 * Created by sunzhonghao on 2018/5/16.
 * desc: Presenter 实现类
 */
public class MVPTestPresenter extends MVPTestContract.Presenter<MVPTestModel> {

    @Override
    public void loadCityInfo() {
        subscribe(getModel().getCityInfo(), new HttpObserver<ResponseBody>(getContext()) {
            @Override
            public void _onNext(ResponseBody responseBody) {
                Logger.i("getData success");
                try {
                    CityInfo cityInfo = new CityInfo();
                    String result = responseBody.string();
                    if (!TextUtils.isEmpty(result) && result.contains("{") && result.contains("}")) {
                        int start = result.indexOf("{");
                        int end = result.indexOf("}");
                        String content = result.substring(start, end + 1);
                        JSONObject jsonObject = new JSONObject(content);
                        String ipAddress = jsonObject.getString("cip");
                        String cityId = jsonObject.getString("cid");
                        String cityName = jsonObject.getString("cname");
                        cityInfo.setCityId(cityId);
                        cityInfo.setCityName(cityName);
                        cityInfo.setIpAddress(ipAddress);
                    }
                    getView().handleCityInfoResult(cityInfo);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void _onError(HttpException exception) {
                Logger.i(exception.getMessage());
            }
        });
    }
}
