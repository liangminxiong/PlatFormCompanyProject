package com.yuefeng.features.presenter;

import com.common.base.codereview.BasePresenterImpl;
import com.common.event.CommonEvent;
import com.common.network.ApiException;
import com.common.network.HttpObservable;
import com.common.network.HttpResultObserver;
import com.common.utils.Constans;
import com.yuefeng.features.contract.FeaturesContract;
import com.yuefeng.features.event.CarListEvent;
import com.yuefeng.home.ui.modle.AnnouncementDataBean;
import com.yuefeng.ui.MainActivity;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;

/**
 * 消息列表
 */

public class FeaturesPresenter extends BasePresenterImpl<FeaturesContract.View,
        MainActivity> implements FeaturesContract.Presenter {
    public FeaturesPresenter(FeaturesContract.View view, MainActivity activity) {
        super(view, activity);
    }

    @Override
    public void getAnnouncementByuserid(String userpid, String timestart, String timeend, String check) {

        HttpObservable.getObservable(apiRetrofit.getAnnouncementByuserid(userpid, timestart, timeend, check))
//                .subscribe(new HttpResultObserver<ResponseCustom<String>>() {
                .subscribe(new HttpResultObserver<AnnouncementDataBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
                        showLoadingDialog("加载中...");
                    }

                    @Override
                    protected void onSuccess(AnnouncementDataBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.isSuccess()) {
                                EventBus.getDefault().post(new CarListEvent(Constans.NEW_MSG_SUCCESS, o.getMsg()));
                            } else {
                                EventBus.getDefault().post(new CarListEvent(Constans.NEW_MSG_ERROR, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CarListEvent(Constans.NEW_MSG_ERROR, e.getMsg()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CarListEvent(Constans.NEW_MSG_ERROR, ""));
                        super.onError(e);
                    }

                    @Override
                    protected void _onError(ApiException error) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.NEW_MSG_ERROR, error.getMsg()));
                        super._onError(error);
                    }
                });

    }

}
