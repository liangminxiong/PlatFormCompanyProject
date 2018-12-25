package com.yuefeng.features.presenter.zhuguansign;

import com.common.base.codereview.BasePresenterImpl;
import com.common.event.CommonEvent;
import com.common.network.ApiException;
import com.common.network.HttpObservable;
import com.common.network.HttpResultObserver;
import com.common.utils.Constans;
import com.yuefeng.features.contract.ExecutiveAttendanceTrackContract;
import com.yuefeng.features.ui.activity.sngnin.HistoryExecuTrackActivity;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;

/**
 * 主管考勤查看轨迹
 */

public class ExecutiveAttendanceTrackPresenter extends BasePresenterImpl<ExecutiveAttendanceTrackContract.View,
        HistoryExecuTrackActivity> implements ExecutiveAttendanceTrackContract.Presenter {
    public ExecutiveAttendanceTrackPresenter(ExecutiveAttendanceTrackContract.View view, HistoryExecuTrackActivity activity) {
        super(view, activity);
    }

    @Override
    public void getPersonidTrack(String function, String userid, String timestart, String timeend) {

        HttpObservable.getObservable(apiRetrofit.getPersonidTrack(function, userid, timestart, timeend))
//                .subscribe(new HttpResultObserver<ResponseCustom<String>>() {
                .subscribe(new HttpResultObserver<ExecutiveAtteanTrackBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
                        showLoadingDialog("加载中...");
                    }

                    @Override
                    protected void onSuccess(ExecutiveAtteanTrackBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.isSuccess()) {
                                EventBus.getDefault().post(new CommonEvent(Constans.ATTENDANCETRACK_SUCCESS, o.getMsg()));
                            } else {
                                EventBus.getDefault().post(new CommonEvent(Constans.ATTENDANCETRACK_ERROR, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.ATTENDANCETRACK_ERROR, e.getMsg()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.ATTENDANCETRACK_ERROR, ""));
                    }

                    @Override
                    protected void _onError(ApiException error) {
                        super._onError(error);
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.ATTENDANCETRACK_ERROR, ""));
                    }
                });

    }

}
