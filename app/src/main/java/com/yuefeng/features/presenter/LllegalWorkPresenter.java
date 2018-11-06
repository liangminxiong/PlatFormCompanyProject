package com.yuefeng.features.presenter;

import com.common.base.codereview.BasePresenterImpl;
import com.common.network.ApiException;
import com.common.network.HttpObservable;
import com.common.network.HttpResultObserver;
import com.common.utils.Constans;
import com.yuefeng.features.contract.LllegalWorkContract;
import com.yuefeng.features.event.LllegalWorkEvent;
import com.yuefeng.features.modle.LllegalworkBean;
import com.yuefeng.features.modle.carlist.CarListInfosBean;
import com.yuefeng.features.ui.activity.Lllegalwork.LllegalWorkActivity;
import com.yuefeng.personaltree.model.PersoanlTreeListBean;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;

/**
 * 轨迹
 */

public class LllegalWorkPresenter extends BasePresenterImpl<LllegalWorkContract.View,
        LllegalWorkActivity> implements LllegalWorkContract.Presenter {
    public LllegalWorkPresenter(LllegalWorkContract.View view, LllegalWorkActivity activity) {
        super(view, activity);
    }

    /*车辆列表*/
    @Override
    public void getCarListInfos(String function, String organid, String userid, String isreg) {

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
                                EventBus.getDefault().postSticky(new LllegalWorkEvent(Constans.CARLIST_SSUCESS, o.getMsg()));
                            } else {
                                EventBus.getDefault().postSticky(new LllegalWorkEvent(Constans.CARLIST_ERROR, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().postSticky(new LllegalWorkEvent(Constans.CARLIST_ERROR, e.getMsg()));
                    }
                });

    }

    /*车辆列表*/
    @Override
    public void getCarListInfosNew(String function, String organid, String userid, String isreg) {

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
                                EventBus.getDefault().postSticky(new LllegalWorkEvent(Constans.CARLIST_SSUCESS, o.getMsg()));
                            } else {
                                EventBus.getDefault().postSticky(new LllegalWorkEvent(Constans.CARLIST_ERROR, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().postSticky(new LllegalWorkEvent(Constans.CARLIST_ERROR, e.getMsg()));
                    }
                });

    }

    /*获取人员*/
    @Override
    public void getPersontree(String function, String userid, String pid) {

        HttpObservable.getObservable(apiRetrofit.getPersontree(function, userid, pid))
//                .subscribe(new HttpResultObserver<ResponseCustom<String>>() {
                .subscribe(new HttpResultObserver<PersoanlTreeListBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
//                        showLoadingDialog("加载中...");
                    }

                    @Override
                    protected void onSuccess(PersoanlTreeListBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.isSuccess()) {
                                EventBus.getDefault().postSticky(new LllegalWorkEvent(Constans.PERSONALLIST_SSUCESS, o.getMsg()));
                            } else {
                                EventBus.getDefault().postSticky(new LllegalWorkEvent(Constans.PERSONALLIST_ERROR, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().postSticky(new LllegalWorkEvent(Constans.PERSONALLIST_ERROR, e.getMsg()));
                    }
                });

    }

    /*获取违规*/
    @Override
    public void getWeigui(String function, String pid, String timestatr,
                          String timeend, String vid, final String type, final int typeWhat) {
        HttpObservable.getObservable(apiRetrofit.getWeigui(function, pid, timestatr, timeend, vid, type))
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
                                if (typeWhat == Constans.PERSONAL_ID) {
                                    EventBus.getDefault().postSticky(new LllegalWorkEvent(Constans.PERSONALLLEGAL_SSUCESS, o.getMsg()));
                                } else {
                                    EventBus.getDefault().postSticky(new LllegalWorkEvent(Constans.CARLLEGAL_SSUCESS, o.getMsg()));
                                }
                            } else {
                                if (typeWhat == Constans.PERSONAL_ID) {
                                    EventBus.getDefault().postSticky(new LllegalWorkEvent(Constans.PERSONALLLEGAL_ERROR, o.getMsgTitle()));
                                } else {
                                    EventBus.getDefault().postSticky(new LllegalWorkEvent(Constans.CARLLEGAL_ERROR, o.getMsgTitle()));
                                }
                            }
                        }
                    }


                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        if (typeWhat == Constans.PERSONAL_ID) {
                            EventBus.getDefault().postSticky(new LllegalWorkEvent(Constans.PERSONALLLEGAL_ERROR, e.getMsg()));
                        } else {
                            EventBus.getDefault().postSticky(new LllegalWorkEvent(Constans.CARLLEGAL_ERROR, e.getMsg()));
                        }
                    }


                    @Override
                    protected void _onError(ApiException error) {
                        dismissLoadingDialog();
                        super._onError(error);
                        if (typeWhat == Constans.PERSONAL_ID) {
                            EventBus.getDefault().postSticky(new LllegalWorkEvent(Constans.PERSONALLLEGAL_ERROR, ""));
                        } else {
                            EventBus.getDefault().postSticky(new LllegalWorkEvent(Constans.CARLLEGAL_ERROR, ""));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        dismissLoadingDialog();
                        if (typeWhat == Constans.PERSONAL_ID) {
                            EventBus.getDefault().postSticky(new LllegalWorkEvent(Constans.PERSONALLLEGAL_ERROR, ""));
                        } else {
                            EventBus.getDefault().postSticky(new LllegalWorkEvent(Constans.CARLLEGAL_ERROR, ""));
                        }
                    }
                });
    }
}