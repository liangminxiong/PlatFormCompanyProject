package com.yuefeng.features.presenter;

import com.common.base.codereview.BasePresenterImpl;
import com.common.network.ApiException;
import com.common.network.HttpObservable;
import com.common.network.HttpResultObserver;
import com.common.utils.Constans;
import com.yuefeng.features.contract.JobMoniroringContract;
import com.yuefeng.features.event.JobMonitoringEvent;
import com.yuefeng.features.modle.GetJobMonitotingBean;
import com.yuefeng.features.ui.activity.JobMonitoringActivity;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;

/*作业监控*/
public class JobMonitoringPresenter extends BasePresenterImpl<JobMoniroringContract.View, JobMonitoringActivity>
        implements JobMoniroringContract.Presenter {

    public JobMonitoringPresenter(JobMoniroringContract.View view, JobMonitoringActivity activity) {
        super(view, activity);
    }

    @Override
    public void getmonitorinfo(String function, String userid, String pid, String isreg) {

        HttpObservable.getObservable(apiRetrofit.getmonitorinfo(function, userid, pid, isreg))
//                .subscribe(new HttpResultObserver<ResponseCustom<String>>() {
                .subscribe(new HttpResultObserver<GetJobMonitotingBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
                        showLoadingDialog("请稍等...");
                    }

                    @Override
                    protected void onSuccess(GetJobMonitotingBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.isSuccess()) {
                                EventBus.getDefault().postSticky(new JobMonitoringEvent(Constans.JOB_SSUCESS, o.getMsg()));
                            } else {
                                EventBus.getDefault().postSticky(new JobMonitoringEvent(Constans.JOB_ERROR, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().postSticky(new JobMonitoringEvent(Constans.JOB_ERROR, e.getMsg()));
                    }

                    @Override
                    public void onNext(GetJobMonitotingBean getJobMonitotingBean) {
                        super.onNext(getJobMonitotingBean);
//                        dismissLoadingDialog();
//                        EventBus.getDefault().postSticky(new JobMonitoringEvent(Constans.JOB_ERROR, ""));
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        dismissLoadingDialog();
                        EventBus.getDefault().postSticky(new JobMonitoringEvent(Constans.JOB_ERROR, ""));

                    }

                    @Override
                    protected void onStart(Disposable d) {
                        super.onStart(d);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                    }
                });
    }

}
