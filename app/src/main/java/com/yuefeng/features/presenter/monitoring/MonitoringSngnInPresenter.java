package com.yuefeng.features.presenter.monitoring;

import com.common.base.codereview.BasePresenterImpl;
import com.common.network.ApiException;
import com.common.network.HttpObservable;
import com.common.network.HttpResultObserver;
import com.common.utils.Constans;
import com.yuefeng.features.contract.MonitoringContract;
import com.yuefeng.features.event.ProblemEvent;
import com.yuefeng.features.modle.SubmitBean;
import com.yuefeng.features.ui.activity.monitoring.MonitoringSngnInActivity;
import com.yuefeng.personaltree.model.PersoanlTreeListBean;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;

/**
 * 作业监察签到
 */

public class MonitoringSngnInPresenter extends BasePresenterImpl<MonitoringContract.View,
        MonitoringSngnInActivity> implements MonitoringContract.Presenter {
    public MonitoringSngnInPresenter(MonitoringContract.View view, MonitoringSngnInActivity activity) {
        super(view, activity);
    }

    /*获取人员*/
    @Override
    public void getPersontree(String function, String userid, String pid) {

        HttpObservable.getObservable(apiRetrofit.getPersontree(function, userid, pid))
//                .subscribe(new HttpResultObserver<ResponseCustom<String>>() {
                .subscribe(new HttpResultObserver<PersoanlTreeListBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
                        showLoadingDialog("签到中...");
                    }

                    @Override
                    protected void onSuccess(PersoanlTreeListBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.isSuccess()) {
                                EventBus.getDefault().post(new ProblemEvent(Constans.PERSONALLIST_SSUCESS, o.getMsg()));
                            } else {
                                EventBus.getDefault().post(new ProblemEvent(Constans.PERSONALLIST_ERROR, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new ProblemEvent(Constans.PERSONALLIST_ERROR, e.getMsg()));
                    }

                    @Override
                    protected void _onError(ApiException error) {
                        dismissLoadingDialog();
                        super._onError(error);
                        EventBus.getDefault().post(new ProblemEvent(Constans.PERSONALLIST_ERROR, ""));
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new ProblemEvent(Constans.PERSONALLIST_ERROR, ""));
                    }
                });

    }


    @Override
    public void uploadWorkSign(String function, String pid, String userid, String address, String lat,
                               String lon, String personids, String imageArrays, String memo) {
        HttpObservable.getObservable(apiRetrofit.uploadWorkSign(function, pid, userid, address,
                lat, lon, personids, imageArrays, memo))
                .subscribe(new HttpResultObserver<SubmitBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
                        showLoadingDialog("加载中...");
                    }

                    @Override
                    protected void onSuccess(SubmitBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.isSuccess()) {
                                EventBus.getDefault().post(new ProblemEvent(Constans.MONITORINGSIGNIN_SSUCESS, o.getMsg()));
                            } else {
                                EventBus.getDefault().post(new ProblemEvent(Constans.MONITORINGSIGNIN_ERROR, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new ProblemEvent(Constans.MONITORINGSIGNIN_ERROR, e.getMsg()));
                    }


                    @Override
                    protected void _onError(ApiException error) {
                        dismissLoadingDialog();
                        super._onError(error);
                        EventBus.getDefault().post(new ProblemEvent(Constans.MONITORINGSIGNIN_ERROR, ""));
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new ProblemEvent(Constans.MONITORINGSIGNIN_ERROR, ""));
                    }
                });

    }


}
