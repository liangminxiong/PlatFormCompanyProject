package com.yuefeng.home.presenter;

import com.common.base.codereview.BasePresenterImpl;
import com.common.event.CommonEvent;
import com.common.network.ApiException;
import com.common.network.HttpObservable;
import com.common.network.HttpResultObserver;
import com.common.utils.Constans;
import com.yuefeng.home.contract.AnnouncementListInfosContract;
import com.yuefeng.home.ui.activity.AnnouncementListInfosActivtiy;
import com.yuefeng.home.modle.AnnouncementDataBean;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;

/**
 * 消息列表
 */

public class AnnouncementListInfosPresenter extends BasePresenterImpl<AnnouncementListInfosContract.View,
        AnnouncementListInfosActivtiy> implements AnnouncementListInfosContract.Presenter {
    public AnnouncementListInfosPresenter(AnnouncementListInfosContract.View view, AnnouncementListInfosActivtiy activity) {
        super(view, activity);
    }

    @Override
    public void getAnnouncementByuserid(String function, String pid, String timestart, String timeend, int page, int limit, final boolean isLoad) {

        HttpObservable.getObservable(apiRetrofit.getAnnouncementByuserid(function,pid, timestart, timeend,page,limit))
//                .subscribe(new HttpResultObserver<ResponseCustom<String>>() {
                .subscribe(new HttpResultObserver<AnnouncementDataBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
                        if (isLoad) {
                            showLoadingDialog("加载中...");
                        }
                    }

                    @Override
                    protected void onSuccess(AnnouncementDataBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.getCode()==0) {
                                EventBus.getDefault().post(new CommonEvent(Constans.ANMENT_LIST_SSUCESS, o));
                            } else {
                                EventBus.getDefault().post(new CommonEvent(Constans.ANMENT_LIST_ERROR, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.ANMENT_LIST_ERROR, e.getMsg()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.ANMENT_LIST_ERROR_NULL, ""));
                        super.onError(e);
                    }

                    @Override
                    protected void _onError(ApiException error) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.ANMENT_LIST_ERROR, error.getMsg()));
                        super._onError(error);
                    }
                });

    }

}
