package com.yuefeng.features.presenter.monitoring;

import com.common.base.codereview.BasePresenterImpl;
import com.common.network.ApiException;
import com.common.network.HttpObservable;
import com.common.network.HttpResultObserver;
import com.common.utils.Constans;
import com.yuefeng.features.contract.HistorySngnInContract;
import com.yuefeng.features.event.ProblemEvent;
import com.yuefeng.features.modle.HistorySngnInDataBean;
import com.yuefeng.features.ui.activity.monitoring.HistoryMonitoringSngnInActivity;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;

/**
 * 作业监察签到历史
 */

public class HistorySngnInPresenter extends BasePresenterImpl<HistorySngnInContract.View,
        HistoryMonitoringSngnInActivity> implements HistorySngnInContract.Presenter {
    public HistorySngnInPresenter(HistorySngnInContract.View view, HistoryMonitoringSngnInActivity activity) {
        super(view, activity);
    }

    /*获取人员*/
    @Override
    public void getAppWorkSign(String function, String userid, String timestart, String timeend) {

        HttpObservable.getObservable(apiRetrofit.getAppWorkSign(function, userid, timestart, timeend))
//                .subscribe(new HttpResultObserver<ResponseCustom<String>>() {
                .subscribe(new HttpResultObserver<HistorySngnInDataBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
                        showLoadingDialog("签到中...");
                    }

                    @Override
                    protected void onSuccess(HistorySngnInDataBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.isSuccess()) {
                                EventBus.getDefault().post(new ProblemEvent(Constans.MONITORINGSIGNINHIS_SSUCESS, o.getMsg()));
                            } else {
                                EventBus.getDefault().post(new ProblemEvent(Constans.MONITORINGSIGNINHIS_ERROR, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new ProblemEvent(Constans.MONITORINGSIGNINHIS_ERROR, e.getMsg()));
                    }

                    @Override
                    protected void _onError(ApiException error) {
                        dismissLoadingDialog();
                        super._onError(error);
                        EventBus.getDefault().post(new ProblemEvent(Constans.MONITORINGSIGNINHIS_ERROR, ""));
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new ProblemEvent(Constans.MONITORINGSIGNINHIS_ERROR, ""));
                    }
                });

    }

}
