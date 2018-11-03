package com.yuefeng.features.presenter.monitoring;

import com.common.base.codereview.BasePresenterImpl;
import com.yuefeng.features.contract.MonitoringContract;
import com.yuefeng.features.ui.activity.monitoring.MonitoringActivity;

/**
 * 作业监察
 */

public class MonitoringPresenter extends BasePresenterImpl<MonitoringContract.View,
        MonitoringActivity> implements MonitoringContract.Presenter {
    public MonitoringPresenter(MonitoringContract.View view, MonitoringActivity activity) {
        super(view, activity);
    }


    /*获取违规*/
    @Override
    public void getCarListInfos(String function, String organid, String userid, String isreg) {
       /* HttpObservable.getObservable(apiRetrofit.getWeigui(function, pid, timestatr, timeend, vid, type))
                .subscribe(new HttpResultObserver<LllegalworkBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
                        showLoadingDialog("加载中...");
                    }

                    @Override
                    protected void onSuccess(LllegalworkBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.isSuccess()) {
                                EventBus.getDefault().postSticky(new MonitoringEvent(Constans.CARLLEGAL_SSUCESS, o.getMsg()));
                            } else {
                                EventBus.getDefault().postSticky(new MonitoringEvent(Constans.CARLLEGAL_ERROR, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().postSticky(new MonitoringEvent(Constans.CARLLEGAL_ERROR, e.getMsg()));
                    }


                    @Override
                    protected void _onError(ApiException error) {
                        dismissLoadingDialog();
                        super._onError(error);
                        EventBus.getDefault().postSticky(new MonitoringEvent(Constans.CARLLEGAL_ERROR, ""));
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        dismissLoadingDialog();
                        EventBus.getDefault().postSticky(new MonitoringEvent(Constans.CARLLEGAL_ERROR, ""));
                    }
                });*/

    }


}
