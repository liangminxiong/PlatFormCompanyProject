package com.yuefeng.home.presenter;

import com.common.base.codereview.BasePresenterImpl;
import com.common.network.ApiException;
import com.common.network.HttpObservable;
import com.common.network.HttpResultObserver;
import com.common.utils.Constans;
import com.yuefeng.home.contract.HomeContract;
import com.yuefeng.home.modle.NewMsgDataBean;
import com.yuefeng.login_splash.event.SignInEvent;
import com.yuefeng.ui.MainActivity;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;

/**
 * 消息列表
 */

public class HomePresenter extends BasePresenterImpl<HomeContract.View,
        MainActivity> implements HomeContract.Presenter {
    public HomePresenter(HomeContract.View view, MainActivity activity) {
        super(view, activity);
    }

    @Override
    public void getAnnouncementByuserid(String function, String pid, String timestart, String timeend) {

        HttpObservable.getObservable(apiRetrofit.getAnnouncementByuserid(function, pid, timestart, timeend))
//                .subscribe(new HttpResultObserver<ResponseCustom<String>>() {
                .subscribe(new HttpResultObserver<NewMsgDataBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
//                        showLoadingDialog("加载中...");
                    }

                    @Override
                    protected void onSuccess(NewMsgDataBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.isSuccess()) {
                                EventBus.getDefault().postSticky(new SignInEvent(Constans.NEW_MSG_SUCCESS, o.getMsg()));
                            } else {
                                EventBus.getDefault().postSticky(new SignInEvent(Constans.NEW_MSG_ERROR, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().postSticky(new SignInEvent(Constans.NEW_MSG_ERROR, e.getMsg()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().postSticky(new SignInEvent(Constans.NEW_MSG_ERROR, ""));
                        super.onError(e);
                    }

                    @Override
                    protected void _onError(ApiException error) {
                        dismissLoadingDialog();
                        EventBus.getDefault().postSticky(new SignInEvent(Constans.NEW_MSG_ERROR, error.getMsg()));
                        super._onError(error);
                    }
                });

    }

}
