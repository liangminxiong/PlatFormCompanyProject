package com.yuefeng.login_splash.presenter;

import com.common.base.codereview.BasePresenterImpl;
import com.common.network.ApiException;
import com.common.network.HttpObservable;
import com.common.network.HttpResultObserver;
import com.common.utils.Constans;
import com.yuefeng.contacts.modle.TokenBean;
import com.yuefeng.login_splash.contract.LoginContract;
import com.yuefeng.login_splash.event.LoginEvent;
import com.yuefeng.login_splash.event.SignInEvent;
import com.yuefeng.login_splash.model.LoginBean;
import com.yuefeng.login_splash.ui.LoginActivity;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;

/**
 * Created  on 2018-05-26.
 * author:seven
 * email:seven2016s@163.com
 */

public class LoginPresenter extends BasePresenterImpl<LoginContract.View, LoginActivity> implements LoginContract.Presenter {
    public LoginPresenter(LoginContract.View view, LoginActivity activity) {
        super(view, activity);
    }

    @Override
    public void login(String function, String username, String password, String client) {

        HttpObservable.getObservable(apiRetrofit.login(function, username, password, client))
//                .subscribe(new HttpResultObserver<ResponseCustom<LoginDataBean>>() {
                .subscribe(new HttpResultObserver<LoginBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
                    }

                    @Override
                    protected void onSuccess(LoginBean o) {
                        if (getView() != null) {
                            if (o.isSuccess()) {
                                EventBus.getDefault().post(new LoginEvent(Constans.LOGIN, o.getMsg()));
                            } else {
                                EventBus.getDefault().post(new LoginEvent(Constans.USERERROR, o.getMsgTitle()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        int code = e.getCode();
                        if (code == 1) {
                            EventBus.getDefault().post(new LoginEvent(Constans.USERERROR, e.getMsg()));
                        } else {
                            EventBus.getDefault().post(new LoginEvent(Constans.USERERROR, "请检查账号密码,网络状态!"));
                        }
                    }
                });
    }

    /*获取token*/
    @Override
    public void getToken(String userid, String username, String usericon) {

        HttpObservable.getObservable(apiRetrofit.getToken(userid, username, usericon))
                .subscribe(new HttpResultObserver<TokenBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
//                        showLoadingDialog("加载中...");
                    }

                    @Override
                    protected void onSuccess(TokenBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.isSuccess()) {
                                if (o.getCode() == 200) {
                                    EventBus.getDefault().post(new LoginEvent(Constans.RONGIM_SUCCESS, o));
                                } else {
                                    EventBus.getDefault().post(new LoginEvent(Constans.RONGIM_ERROR, o.getData()));
                                }
                            } else {
                                EventBus.getDefault().post(new LoginEvent(Constans.RONGIM_ERROR, o.getData()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new LoginEvent(Constans.RONGIM_ERROR, e.getMsg()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().postSticky(new SignInEvent(Constans.RONGIM_ERROR, ""));
                        super.onError(e);
                    }

                    @Override
                    protected void _onError(ApiException error) {
                        dismissLoadingDialog();
                        EventBus.getDefault().postSticky(new SignInEvent(Constans.RONGIM_ERROR, error.getMsg()));
                        super._onError(error);
                    }
                });
    }

}
