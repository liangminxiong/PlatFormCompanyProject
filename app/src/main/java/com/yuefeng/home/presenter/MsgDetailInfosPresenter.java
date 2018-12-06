package com.yuefeng.home.presenter;

import com.common.base.codereview.BasePresenterImpl;
import com.common.event.CommonEvent;
import com.common.network.ApiException;
import com.common.network.HttpObservable;
import com.common.network.HttpResultObserver;
import com.common.utils.Constans;
import com.yuefeng.home.contract.MsgDetailInfosContract;
import com.yuefeng.home.ui.activity.OnlyMsgDetailInfosActivtiy;
import com.yuefeng.home.modle.MsgDataDetailBean;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;

/**
 * 消息列表详情
 */

public class MsgDetailInfosPresenter extends BasePresenterImpl<MsgDetailInfosContract.View,
        OnlyMsgDetailInfosActivtiy> implements MsgDetailInfosContract.Presenter {
    public MsgDetailInfosPresenter(MsgDetailInfosContract.View view, OnlyMsgDetailInfosActivtiy activity) {
        super(view, activity);
    }

    @Override
    public void getMsgDetail(String reviewid) {

        HttpObservable.getObservable(apiRetrofit.getMsgDetail(reviewid))
//                .subscribe(new HttpResultObserver<ResponseCustom<String>>() {
                .subscribe(new HttpResultObserver<MsgDataDetailBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
                        showLoadingDialog("加载中...");
                    }

                    @Override
                    protected void onSuccess(MsgDataDetailBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.isSuccess()) {
                                EventBus.getDefault().post(new CommonEvent(Constans.MSG_DETAIL_SSUCESS, o.getResult()));
                            } else {
                                EventBus.getDefault().post(new CommonEvent(Constans.MSG_DETAIL_ERROR, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.MSG_DETAIL_ERROR, e.getMsg()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.MSG_DETAIL_ERROR, ""));
                        super.onError(e);
                    }

                    @Override
                    protected void _onError(ApiException error) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.MSG_DETAIL_ERROR, error.getMsg()));
                        super._onError(error);
                    }
                });

    }

    @Override
    public void getMsgDetail(String function, String reviewid) {

        HttpObservable.getObservable(apiRetrofit.getMsgDetail(function, reviewid))
//                .subscribe(new HttpResultObserver<ResponseCustom<String>>() {
                .subscribe(new HttpResultObserver<MsgDataDetailBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
//                        showLoadingDialog("加载中...");
                    }

                    @Override
                    protected void onSuccess(MsgDataDetailBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
//                            if (o.isSuccess()) {
//                                EventBus.getDefault().post(new CommonEvent(Constans.MSG_DETAIL_SSUCESS, o.getResult()));
//                            } else {
//                                EventBus.getDefault().post(new CommonEvent(Constans.MSG_DETAIL_ERROR, o.getMsg()));
//                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
//                        EventBus.getDefault().post(new CommonEvent(Constans.MSG_DETAIL_ERROR, e.getMsg()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissLoadingDialog();
//                        EventBus.getDefault().post(new CommonEvent(Constans.MSG_DETAIL_ERROR, ""));
                        super.onError(e);
                    }

                    @Override
                    protected void _onError(ApiException error) {
                        dismissLoadingDialog();
//                        EventBus.getDefault().post(new CommonEvent(Constans.MSG_DETAIL_ERROR, error.getMsg()));
                        super._onError(error);
                    }
                });

    }

}
