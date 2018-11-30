package com.yuefeng.home.presenter;

import com.common.base.codereview.BasePresenterImpl;
import com.common.event.CommonEvent;
import com.common.network.ApiException;
import com.common.network.HttpObservable;
import com.common.network.HttpResultObserver;
import com.common.utils.Constans;
import com.yuefeng.home.contract.AppVersionContract;
import com.yuefeng.home.modle.HistoryAppVersionBean;
import com.yuefeng.home.ui.activity.HistoryAppVersionActivtiy;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;

/**
 * 消息列表
 */

public class AppVersionPresenter extends BasePresenterImpl<AppVersionContract.View,
        HistoryAppVersionActivtiy> implements AppVersionContract.Presenter {
    public AppVersionPresenter(AppVersionContract.View view, HistoryAppVersionActivtiy activity) {
        super(view, activity);
    }

    @Override
    public void getAppHistoryVersion(int page, int limit, String timestart, String timeend, final boolean isLoad) {

        HttpObservable.getObservable(apiRetrofit.getAppHistoryVersion(page, limit, timestart, timeend))
//                .subscribe(new HttpResultObserver<ResponseCustom<String>>() {
                .subscribe(new HttpResultObserver<HistoryAppVersionBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
                        if (isLoad) {
                            showLoadingDialog("加载中...");
                        }
                    }

                    @Override
                    protected void onSuccess(HistoryAppVersionBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.getCode() == 0) {
                                EventBus.getDefault().post(new CommonEvent(Constans.APP_VERSION_SUCCESS, o));
                            } else {
                                EventBus.getDefault().post(new CommonEvent(Constans.APP_VERSION_ERROR, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.APP_VERSION_ERROR, e.getMsg()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.APP_VERSION_ERROR, ""));
                        super.onError(e);
                    }

                    @Override
                    protected void _onError(ApiException error) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.APP_VERSION_ERROR, error.getMsg()));
                        super._onError(error);
                    }
                });

    }

}
