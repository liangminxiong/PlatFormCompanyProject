package com.yuefeng.features.presenter.zhuguansign;

import com.common.base.codereview.BasePresenterImpl;
import com.common.event.CommonEvent;
import com.common.network.ApiException;
import com.common.network.HttpObservable;
import com.common.network.HttpResultObserver;
import com.common.utils.Constans;
import com.yuefeng.features.contract.ExecutiveAttendanceContract;
import com.yuefeng.features.modle.zhuguanSign.GetSignJsonBean;
import com.yuefeng.features.modle.zhuguanSign.ZhuGuanSignBean;
import com.yuefeng.features.ui.activity.sngnin.ExecutiveAttendanceActivity;
import com.yuefeng.personaltree.model.PersoanlTreeListBean;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;

/**
 * 主管考勤
 */

public class ExecutiveAttendancePresenter extends BasePresenterImpl<ExecutiveAttendanceContract.View,
        ExecutiveAttendanceActivity> implements ExecutiveAttendanceContract.Presenter {
    public ExecutiveAttendancePresenter(ExecutiveAttendanceContract.View view, ExecutiveAttendanceActivity activity) {
        super(view, activity);
    }

    @Override
    public void getSignTree(String function, String pid ){

        HttpObservable.getObservable(apiRetrofit.getSignTree(function, pid))
//                .subscribe(new HttpResultObserver<ResponseCustom<String>>() {
                .subscribe(new HttpResultObserver<PersoanlTreeListBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
                        showLoadingDialog("加载中...");
                    }

                    @Override
                    protected void onSuccess(PersoanlTreeListBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.isSuccess()) {
                                EventBus.getDefault().post(new CommonEvent(Constans.ATTENDANCETRESS_SUCCESS, o.getMsg()));
                            } else {
                                EventBus.getDefault().post(new CommonEvent(Constans.ATTENDANCETRESS_ERROR, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.ATTENDANCETRESS_ERROR, e.getMsg()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.ATTENDANCETRESS_ERROR, ""));
                    }

                    @Override
                    protected void _onError(ApiException error) {
                        super._onError(error);
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.ATTENDANCETRESS_ERROR, ""));
                    }
                });

    }

    /*获取主管id列表*/
    @Override
    public void getSignJson(String function, String pid) {
        HttpObservable.getObservable(apiRetrofit.getSignJson(function, pid))
//                .subscribe(new HttpResultObserver<ResponseCustom<String>>() {
                .subscribe(new HttpResultObserver<GetSignJsonBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
                        showLoadingDialog("加载中...");
                    }

                    @Override
                    protected void onSuccess(GetSignJsonBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.isSuccess()) {
                                EventBus.getDefault().post(new CommonEvent(Constans.ATTENDANCELIST_SUCCESS, o.getMsg()));
                            } else {
                                EventBus.getDefault().post(new CommonEvent(Constans.ATTENDANCELIST_ERROR, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.ATTENDANCELIST_ERROR, e.getMsg()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.ATTENDANCELIST_ERROR, ""));
                    }

                    @Override
                    protected void _onError(ApiException error) {
                        super._onError(error);
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.ATTENDANCELIST_ERROR, ""));
                    }
                });
    }

    @Override
    public void getPersonalMonitor(String function, String idflags, final boolean isFirstGetData) {
        HttpObservable.getObservable(apiRetrofit.getPersonalMonitor(function, idflags))
//                .subscribe(new HttpResultObserver<ResponseCustom<String>>() {
                .subscribe(new HttpResultObserver<ZhuGuanSignBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
                        if (isFirstGetData) {
                            showLoadingDialog("加载中...");
                        }
                    }

                    @Override
                    protected void onSuccess(ZhuGuanSignBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.isSuccess()) {
                                EventBus.getDefault().post(new CommonEvent(Constans.ATTENDANCELNGLAT_SUCCESS, o.getMsg()));
                            } else {
                                EventBus.getDefault().post(new CommonEvent(Constans.ATTENDANCELNGLAT_ERROR, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.ATTENDANCELNGLAT_ERROR, e.getMsg()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.ATTENDANCELNGLAT_ERROR, ""));
                    }

                    @Override
                    protected void _onError(ApiException error) {
                        super._onError(error);
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.ATTENDANCELNGLAT_ERROR, ""));
                    }
                });
    }

}
