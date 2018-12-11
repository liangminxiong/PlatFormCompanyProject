package com.yuefeng.login_splash.presenter;

import com.common.base.codereview.BasePresenterImpl;
import com.common.network.ApiException;
import com.common.network.HttpObservable;
import com.common.network.HttpResultObserver;
import com.common.utils.Constans;
import com.yuefeng.features.modle.SubmitBean;
import com.yuefeng.home.modle.NewMsgDataBean;
import com.yuefeng.login_splash.contract.SignInContract;
import com.yuefeng.login_splash.event.SignInEvent;
import com.yuefeng.ui.MainActivity;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;

/**
 * 签到
 */

public class SignInPresenter extends BasePresenterImpl<SignInContract.View, MainActivity> implements SignInContract.Presenter {
    public SignInPresenter(SignInContract.View view, MainActivity activity) {
        super(view, activity);
    }

    @Override
    public void signIn(String function, String userid, String terflag, String useridflag,
                       String lon, String lat, String address, String type) {

        HttpObservable.getObservable(apiRetrofit.signIn(function, userid, terflag, useridflag,
                lon, lat, address, type))
//                .subscribe(new HttpResultObserver<ResponseCustom<LoginDataBean>>() {
                .subscribe(new HttpResultObserver<SubmitBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
                    }

                    @Override
                    protected void onSuccess(SubmitBean o) {
                        if (getView() != null) {
                            if (o.isSuccess()) {
                                EventBus.getDefault().postSticky(new SignInEvent(Constans.LOGIN, o.getMsg()));
                            } else {
                                EventBus.getDefault().postSticky(new SignInEvent(Constans.USERERROR, o.getMsgTitle()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        EventBus.getDefault().postSticky(new SignInEvent(Constans.USERERROR, e.getMsg()));
                    }
                });

    }

    @Override
    public void getAnnouncementByuserid(String function,String pid, String timestart, String timeend) {

        HttpObservable.getObservable(apiRetrofit.getAnnouncementByuserid(function,pid, timestart, timeend))
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
