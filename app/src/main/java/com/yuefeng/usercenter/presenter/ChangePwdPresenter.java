package com.yuefeng.usercenter.presenter;

import com.common.base.codereview.BasePresenterImpl;
import com.common.network.ApiException;
import com.common.network.HttpObservable;
import com.common.network.HttpResultObserver;
import com.common.utils.Constans;
import com.yuefeng.features.modle.SubmitBean;
import com.yuefeng.usercenter.contract.ChangePwdContract;
import com.yuefeng.usercenter.event.ChangePwdEvent;
import com.yuefeng.usercenter.ui.activity.ChangePassWordActivity;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;

/**
 * 修改密码
 */

public class ChangePwdPresenter extends BasePresenterImpl<ChangePwdContract.View,
        ChangePassWordActivity> implements ChangePwdContract.Presenter {

    private String hourStr;
    private String minuteStr;
    private String secondStr;

    public ChangePwdPresenter(ChangePwdContract.View view, ChangePassWordActivity activity) {
        super(view, activity);
    }


    /*修改密码*/
    @Override
    public void changePwd(String function, String id, String oldpassword, String newpassword) {
        HttpObservable.getObservable(apiRetrofit.changePwd(function, id, oldpassword, newpassword))
                .subscribe(new HttpResultObserver<SubmitBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
                        showLoadingDialog("修改中...");
                    }

                    @Override
                    protected void onSuccess(SubmitBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.isSuccess()) {
                                EventBus.getDefault().postSticky(new ChangePwdEvent(Constans.CHANGEPWD_SSUCESS, o.getMsg()));
                            } else {
                                EventBus.getDefault().postSticky(new ChangePwdEvent(Constans.CHANGEPWD_ERROR, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().postSticky(new ChangePwdEvent(Constans.CHANGEPWD_ERROR, e.getMsg()));
                    }


                    @Override
                    protected void _onError(ApiException error) {
                        dismissLoadingDialog();
                        super._onError(error);
                        EventBus.getDefault().postSticky(new ChangePwdEvent(Constans.CHANGEPWD_ERROR, ""));
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        dismissLoadingDialog();
                        EventBus.getDefault().postSticky(new ChangePwdEvent(Constans.CHANGEPWD_ERROR, ""));
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                    }

                    @Override
                    protected void _onNext(SubmitBean responseCustom) {
                        super._onNext(responseCustom);
                        dismissLoadingDialog();
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        dismissLoadingDialog();
                    }

                    @Override
                    protected void onStart(Disposable d) {
                        super.onStart(d);
                        dismissLoadingDialog();
                        EventBus.getDefault().postSticky(new ChangePwdEvent(Constans.CHANGEPWD_ERROR, ""));
                    }

                    @Override
                    public void onNext(SubmitBean eventQuestionBean) {
                        super.onNext(eventQuestionBean);
                    }
                });
    }

}
