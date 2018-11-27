package com.yuefeng.home.presenter;

import com.common.base.codereview.BasePresenterImpl;
import com.common.network.ApiException;
import com.common.network.HttpObservable;
import com.common.network.HttpResultObserver;
import com.common.utils.Constans;
import com.yuefeng.features.event.ProblemEvent;
import com.yuefeng.home.contract.ReplyContract;
import com.yuefeng.home.ui.activity.ReplyMsgDetailInfosActivtiy;
import com.yuefeng.home.ui.modle.ReplyContentBean;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;

/**
 * 消息回复
 */

public class ReplyPresenter extends BasePresenterImpl<ReplyContract.View,
        ReplyMsgDetailInfosActivtiy> implements ReplyContract.Presenter {
    public ReplyPresenter(ReplyContract.View view, ReplyMsgDetailInfosActivtiy activity) {
        super(view, activity);
    }

    @Override
    public void doReview(String pid, String reviewid, String reviewpersonel, String reviewcontent, String imageurls) {

        HttpObservable.getObservable(apiRetrofit.doReview(pid,reviewid,reviewpersonel,reviewcontent,imageurls))
//                .subscribe(new HttpResultObserver<ResponseCustom<String>>() {
                .subscribe(new HttpResultObserver<ReplyContentBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
                        showLoadingDialog("回复中...");
                    }

                    @Override
                    protected void onSuccess(ReplyContentBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.isSuccess()) {
                                EventBus.getDefault().post(new ProblemEvent(Constans.MSG_REPLY_SSUCESS, o.getResult()));
                            } else {
                                EventBus.getDefault().post(new ProblemEvent(Constans.MSG_REPLY_ERROR, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new ProblemEvent(Constans.MSG_REPLY_ERROR, e.getMsg()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new ProblemEvent(Constans.MSG_REPLY_ERROR, ""));
                        super.onError(e);
                    }

                    @Override
                    protected void _onError(ApiException error) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new ProblemEvent(Constans.MSG_REPLY_ERROR, error.getMsg()));
                        super._onError(error);
                    }
                });

    }

}
