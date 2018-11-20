package com.yuefeng.features.presenter.monitoring;

import com.common.base.codereview.BasePresenterImpl;
import com.common.network.ApiException;
import com.common.network.HttpObservable;
import com.common.network.HttpResultObserver;
import com.common.utils.Constans;
import com.yuefeng.features.contract.MonitoringHistoryContract;
import com.yuefeng.features.event.ProblemEvent;
import com.yuefeng.features.modle.GetMonitoringHistoryBean;
import com.yuefeng.features.ui.activity.monitoring.MonitoringHistoryOfJobActivity;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;

/**
 * 历史作业监察
 */

public class MonitoringHistoryPresenter extends BasePresenterImpl<MonitoringHistoryContract.View,
        MonitoringHistoryOfJobActivity> implements MonitoringHistoryContract.Presenter {
    public MonitoringHistoryPresenter(MonitoringHistoryContract.View view, MonitoringHistoryOfJobActivity activity) {
        super(view, activity);
    }

    @Override
    public void getWorkJianCha(String function, String userid, String timestart, String timeend) {
        HttpObservable.getObservable(apiRetrofit.getWorkJianCha(function, userid, timestart, timeend))
                .subscribe(new HttpResultObserver<GetMonitoringHistoryBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
                        showLoadingDialog("加载中...");
                    }

                    @Override
                    protected void onSuccess(GetMonitoringHistoryBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.isSuccess()) {
                                EventBus.getDefault().post(new ProblemEvent(Constans.MSGCOLECTION_SSUCESS, o.getMsg()));
                            } else {
                                EventBus.getDefault().post(new ProblemEvent(Constans.MSGCOLECTION_ERROR, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new ProblemEvent(Constans.MSGCOLECTION_ERROR, e.getMsg()));
                    }


                    @Override
                    protected void _onError(ApiException error) {
                        dismissLoadingDialog();
                        super._onError(error);
                        EventBus.getDefault().post(new ProblemEvent(Constans.MSGCOLECTION_ERROR, ""));
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new ProblemEvent(Constans.MSGCOLECTION_ERROR, ""));
                    }
                });
    }

}
