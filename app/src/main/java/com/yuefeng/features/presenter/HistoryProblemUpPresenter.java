package com.yuefeng.features.presenter;

import com.common.base.codereview.BasePresenterImpl;
import com.common.event.CommonEvent;
import com.common.network.ApiException;
import com.common.network.HttpObservable;
import com.common.network.HttpResultObserver;
import com.common.utils.Constans;
import com.yuefeng.features.contract.HistoryProblemUpContract;
import com.yuefeng.features.modle.EventQuestionBean;
import com.yuefeng.features.ui.activity.HistoryProblemUpdataActivity;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;

/**
 * 历史问题上报
 */

public class HistoryProblemUpPresenter extends BasePresenterImpl<HistoryProblemUpContract.View,
        HistoryProblemUpdataActivity> implements HistoryProblemUpContract.Presenter {
    public HistoryProblemUpPresenter(HistoryProblemUpContract.View view, HistoryProblemUpdataActivity activity) {
        super(view, activity);
    }

    @Override
    public void getEveQuestionByuserid(String function, String userid, String timestart, String timeend) {
        HttpObservable.getObservable(apiRetrofit.getEveQuestionByuserid(function, userid, timestart, timeend))
                .subscribe(new HttpResultObserver<EventQuestionBean>() {

                    @Override
                    protected void onLoading(Disposable d) {
                        showLoadingDialog("加载中...");
                    }

                    @Override
                    protected void onSuccess(EventQuestionBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.isSuccess()) {
                                EventBus.getDefault().post(new CommonEvent(Constans.UPLOAD_HISTORY_SUCESS, o.getMsg()));
                            }
                        } else {
                            EventBus.getDefault().post(new CommonEvent(Constans.UPLOAD_HISTORY_ERROR, o.getMsgTitle()));
                        }
                    }


                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.UPLOAD_HISTORY_ERROR, e.getMsg()));
                    }


                    @Override
                    protected void _onError(ApiException error) {
                        dismissLoadingDialog();
                        super._onError(error);
                        EventBus.getDefault().post(new CommonEvent(Constans.UPLOAD_HISTORY_ERROR, ""));
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.UPLOAD_HISTORY_ERROR, ""));
                    }
                });
    }

}
