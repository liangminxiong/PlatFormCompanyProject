package com.yuefeng.home.presenter;

import com.common.base.codereview.BasePresenterImpl;
import com.common.event.CommonEvent;
import com.common.network.ApiException;
import com.common.network.HttpObservable;
import com.common.network.HttpResultObserver;
import com.common.utils.Constans;
import com.yuefeng.home.contract.AppVersionDetailContract;
import com.yuefeng.home.modle.AppVersionDetailBean;
import com.yuefeng.home.ui.activity.AppVersionDetailActivtiy;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;

/**
 * 消息列表
 */

public class AppVersionDetailPresenter extends BasePresenterImpl<AppVersionDetailContract.View,
        AppVersionDetailActivtiy> implements AppVersionDetailContract.Presenter {
    public AppVersionDetailPresenter(AppVersionDetailContract.View view, AppVersionDetailActivtiy activity) {
        super(view, activity);
    }

    @Override
    public void getAppVersionDetail(String id) {

        HttpObservable.getObservable(apiRetrofit.getAppVersionDetail(id))
//                .subscribe(new HttpResultObserver<ResponseCustom<String>>() {
                .subscribe(new HttpResultObserver<AppVersionDetailBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
//                        if (isLoad) {
//                            showLoadingDialog("加载中...");
//                        }
                    }

                    @Override
                    protected void onSuccess(AppVersionDetailBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.isSuccess()) {
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
