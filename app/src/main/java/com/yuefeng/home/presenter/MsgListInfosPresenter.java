package com.yuefeng.home.presenter;

import com.common.base.codereview.BasePresenterImpl;
import com.common.event.CommonEvent;
import com.common.network.ApiException;
import com.common.network.HttpObservable;
import com.common.network.HttpResultObserver;
import com.common.utils.Constans;
import com.yuefeng.home.contract.MsgListInfosContract;
import com.yuefeng.home.ui.activity.MsgListInfosActivtiy;
import com.yuefeng.home.ui.modle.MsgDataBean;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;

/**
 * 消息列表
 */

public class MsgListInfosPresenter extends BasePresenterImpl<MsgListInfosContract.View,
        MsgListInfosActivtiy> implements MsgListInfosContract.Presenter {
    public MsgListInfosPresenter(MsgListInfosContract.View view, MsgListInfosActivtiy activity) {
        super(view, activity);
    }

    @Override
    public void getAnMentDataList(String pid, int page, int limit, final boolean isShowLoad) {

        HttpObservable.getObservable(apiRetrofit.getAnMentDataList(pid, page, limit))
//                .subscribe(new HttpResultObserver<ResponseCustom<String>>() {
                .subscribe(new HttpResultObserver<MsgDataBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
                        if (isShowLoad) {
                            showLoadingDialog("加载中...");
                        }
                    }

                    @Override
                    protected void onSuccess(MsgDataBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.getCode() == 0) {
                                EventBus.getDefault().post(new CommonEvent(Constans.MSG_LIST_SSUCESS, o.getData()));
                            } else {
                                EventBus.getDefault().post(new CommonEvent(Constans.MSG_LIST_ERROR, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.MSG_LIST_ERROR, e.getMsg()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.MSG_LIST_ERROR, ""));
                        super.onError(e);
                    }

                    @Override
                    protected void _onError(ApiException error) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.MSG_LIST_ERROR, error.getMsg()));
                        super._onError(error);
                    }
                });

    }

}
