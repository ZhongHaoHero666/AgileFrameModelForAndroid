package com.android.szh.agileframedemo.activity;

import android.view.View;
import android.widget.TextView;

import com.android.szh.agileframedemo.R;
import com.android.szh.agileframedemo.api.APPApiService;
import com.android.szh.common.base.BaseActivity;
import com.android.szh.common.http.HttpManager;
import com.android.szh.common.http.exception.HttpException;
import com.android.szh.common.rxjava.HttpObserver;
import com.android.szh.common.utils.ToastUtil;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;

/**
 * @author sunzhonghao
 * @date 2018/8/16
 * desc:动态切换BaseUrl演示界面
 */
public class MultipleBaseUrlSwitchActivity extends BaseActivity {
    @BindView(R.id.mvp_result)
    TextView mvpResult;

    @Override
    protected void initViews() {

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_mutiple_base_url_switch;
    }

    @OnClick({R.id.test_normal, R.id.test_switch_base_url})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.test_normal:
                subscribe(HttpManager.getInstance().getRetrofit().create(APPApiService.class).getCityInfo(),
                        new HttpObserver<ResponseBody>(getContext()) {
                            @Override
                            public void _onNext(ResponseBody responseBody) {
                                try {
                                    mvpResult.setText(responseBody.string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void _onError(HttpException exception) {
                                ToastUtil.showToast(exception.getDesc());
                            }
                        });
                break;
            case R.id.test_switch_base_url:
                subscribe(HttpManager.getInstance().getRetrofit().create(APPApiService.class).testSwitchBaseUrl(1220562),
                        new HttpObserver<ResponseBody>(getContext()) {
                            @Override
                            public void _onNext(ResponseBody responseBody) {
                                try {
                                    mvpResult.setText(responseBody.string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void _onError(HttpException exception) {
                                ToastUtil.showToast(exception.getDesc());
                            }
                        });
                break;
        }
    }
}
