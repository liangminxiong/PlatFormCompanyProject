package com.yuefeng.features.presenter;

import com.common.base.codereview.BasePresenterImpl;
import com.common.network.ApiException;
import com.common.network.HttpObservable;
import com.common.network.HttpResultObserver;
import com.common.utils.Constans;
import com.yuefeng.features.contract.JobAttendanceContract;
import com.yuefeng.features.event.JobAttendanceEvent;
import com.yuefeng.features.modle.SubmitBean;
import com.yuefeng.features.modle.carlist.CarListInfosBean;
import com.yuefeng.features.ui.activity.sngnin.JobAttendanceActivity;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;

/**
 * 作业考勤
 */

public class JobAttendancePresenter extends BasePresenterImpl<JobAttendanceContract.View,
        JobAttendanceActivity> implements JobAttendanceContract.Presenter {
    public JobAttendancePresenter(JobAttendanceContract.View view, JobAttendanceActivity activity) {
        super(view, activity);
    }

    /*签到信息*/
    @Override
    public void getAttendanceInfos(String function, String organid, String userid, String isreg) {

        HttpObservable.getObservable(apiRetrofit.getCarListInfos(function, organid, userid, isreg))
//                .subscribe(new HttpResultObserver<ResponseCustom<String>>() {
                .subscribe(new HttpResultObserver<CarListInfosBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
                        showLoadingDialog("加载中...");
                    }

                    @Override
                    protected void onSuccess(CarListInfosBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.isSuccess()) {
                                EventBus.getDefault().post(new JobAttendanceEvent(Constans.SNGNIN_SSUCESS, o.getMsg()));
                            } else {
                                EventBus.getDefault().post(new JobAttendanceEvent(Constans.SNGNIN_ERROR, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new JobAttendanceEvent(Constans.SNGNIN_ERROR, e.getMsg()));
                    }

                    @Override
                    public void onNext(CarListInfosBean carListInfosBean) {
                        super.onNext(carListInfosBean);
                    }
                });
    }


    /*个人签到*/
    @Override
    public void signIn(String function, String userid, String terflag, String useridflag,
                       String lon, String lat, String address, String type) {

        HttpObservable.getObservable(apiRetrofit.signIn(function, userid, terflag, useridflag,
                lon, lat, address, type))
//                .subscribe(new HttpResultObserver<ResponseCustom<LoginDataBean>>() {
                .subscribe(new HttpResultObserver<SubmitBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
                        showLoadingDialog("签到中...");
                    }

                    @Override
                    protected void onSuccess(SubmitBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.isSuccess()) {
                                EventBus.getDefault().post(new JobAttendanceEvent(Constans.LOGIN, o.getMsg()));
                            } else {
                                EventBus.getDefault().post(new JobAttendanceEvent(Constans.USERERROR, o.getMsgTitle()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new JobAttendanceEvent(Constans.USERERROR, e.getMsg()));
                    }
                });

    }

}