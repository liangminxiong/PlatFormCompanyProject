package com.yuefeng.login_splash.presenter;

import com.common.base.codereview.BasePresenterImpl;
import com.common.network.ApiException;
import com.common.network.HttpObservable;
import com.common.network.HttpResultObserver;
import com.common.utils.Constans;
import com.yuefeng.login_splash.contract.LoginContract;
import com.yuefeng.login_splash.event.LoginEvent;
import com.yuefeng.login_splash.model.LoginBean;
import com.yuefeng.login_splash.ui.SplashActivity;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;

/**
 */

public class SplashPresenter extends BasePresenterImpl<LoginContract.View, SplashActivity> implements LoginContract.Presenter {
    public SplashPresenter(LoginContract.View view, SplashActivity activity) {
        super(view, activity);
    }

    @Override
    public void login(String function, String username, String password, String client) {

        HttpObservable.getObservable(apiRetrofit.login(function, username, password, client))
//                .subscribe(new HttpResultObserver<ResponseCustom<LoginDataBean>>() {
                .subscribe(new HttpResultObserver<LoginBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
                        showLoadingDialog("加载中...");
                    }

                    @Override
                    protected void onSuccess(LoginBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.isSuccess()) {
                                EventBus.getDefault().post(new LoginEvent(Constans.REGISTER_SUCCESS, o.getMsg()));
                            } else {
                                EventBus.getDefault().post(new LoginEvent(Constans.REGISTER_ERROR, o.getMsgTitle()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        int code = e.getCode();
                        if (code == 1) {
                            EventBus.getDefault().post(new LoginEvent(Constans.REGISTER_ERROR, e.getMsg()));
                        } else {
                            EventBus.getDefault().post(new LoginEvent(Constans.REGISTER_ERROR, "请检查账号密码,网络状态!"));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new LoginEvent(Constans.REGISTER_ERROR, "请检查账号密码,网络状态!"));
                    }

                    @Override
                    protected void _onError(ApiException error) {
                        super._onError(error);
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new LoginEvent(Constans.REGISTER_ERROR, "请检查账号密码,网络状态!"));
                    }
                });
    }

}
