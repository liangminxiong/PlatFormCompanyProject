package com.yuefeng.home.presenter;

import com.common.base.codereview.BasePresenterImpl;
import com.common.event.CommonEvent;
import com.common.network.ApiException;
import com.common.network.HttpObservable;
import com.common.network.HttpResultObserver;
import com.common.utils.Constans;
import com.yuefeng.home.contract.AnnouncementDetailContract;
import com.yuefeng.home.modle.AnnouncementDeBean;
import com.yuefeng.home.ui.activity.AnnouncementDetailInfosActivtiy;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;

/**
 * 消息列表
 */

public class AnnouncementDetailPresenter extends BasePresenterImpl<AnnouncementDetailContract.View,
        AnnouncementDetailInfosActivtiy> implements AnnouncementDetailContract.Presenter {
    public AnnouncementDetailPresenter(AnnouncementDetailContract.View view, AnnouncementDetailInfosActivtiy activity) {
        super(view, activity);
    }

    @Override
    public void getAnnouncementDetail(String function,String id) {

        HttpObservable.getObservable(apiRetrofit.getAnnouncementDetail(function, id))
//                .subscribe(new HttpResultObserver<ResponseCustom<String>>() {
                .subscribe(new HttpResultObserver<AnnouncementDeBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
//                        showLoadingDialog("加载中...");
                    }

                    @Override
                    protected void onSuccess(AnnouncementDeBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.isSuccess()) {
                                EventBus.getDefault().post(new CommonEvent(Constans.DETAIL_MSG_SUCCESS, o.getResult()));
                            } else {
                                EventBus.getDefault().post(new CommonEvent(Constans.DETAIL_MSG_ERROR, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.DETAIL_MSG_ERROR, e.getMsg()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.DETAIL_MSG_ERROR, ""));
                        super.onError(e);
                    }

                    @Override
                    protected void _onError(ApiException error) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.DETAIL_MSG_ERROR, error.getMsg()));
                        super._onError(error);
                    }
                });

    }

}
