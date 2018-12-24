package com.yuefeng.home.presenter;

import com.common.base.codereview.BasePresenterImpl;
import com.common.event.CommonEvent;
import com.common.network.ApiException;
import com.common.network.HttpObservable;
import com.common.network.HttpResultObserver;
import com.common.utils.Constans;
import com.yuefeng.home.contract.HomeContract;
import com.yuefeng.home.modle.NewMsgDataBean;
import com.yuefeng.home.ui.activity.NewRemindNorActivity;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;

/**
 * 报警消息列表
 */

public class NewRemindNorPresenter extends BasePresenterImpl<HomeContract.View,
        NewRemindNorActivity> implements HomeContract.Presenter {
    public NewRemindNorPresenter(HomeContract.View view, NewRemindNorActivity activity) {
        super(view, activity);
    }

    @Override
    public void getAnnouncementByuserid(String function, String pid, String timestart, String timeend, final boolean isFirstGetData) {

        HttpObservable.getObservable(apiRetrofit.getAnnouncementByuserid(function, pid, timestart, timeend))
//                .subscribe(new HttpResultObserver<ResponseCustom<String>>() {
                .subscribe(new HttpResultObserver<NewMsgDataBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
                        if (isFirstGetData) {
                            showLoadingDialog("加载中...");
                        }
                    }

                    @Override
                    protected void onSuccess(NewMsgDataBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.isSuccess()) {
                                EventBus.getDefault().post(new CommonEvent(Constans.REMINDLIST_SUCCESS, o.getMsg()));
                            } else {
                                EventBus.getDefault().post(new CommonEvent(Constans.REMINDLIST_ERROR, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.REMINDLIST_ERROR, e.getMsg()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.REMINDLIST_ERROR, ""));
                        super.onError(e);
                    }

                    @Override
                    protected void _onError(ApiException error) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.REMINDLIST_ERROR, error.getMsg()));
                        super._onError(error);
                    }
                });

    }

}
