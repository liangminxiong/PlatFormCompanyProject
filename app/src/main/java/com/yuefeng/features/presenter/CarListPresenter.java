package com.yuefeng.features.presenter;

import com.common.base.codereview.BasePresenterImpl;
import com.common.network.ApiException;
import com.common.network.HttpObservable;
import com.common.network.HttpResultObserver;
import com.common.utils.Constans;
import com.yuefeng.features.contract.CarListContract;
import com.yuefeng.features.event.CarListEvent;
import com.yuefeng.features.modle.WheelPathBean;
import com.yuefeng.features.modle.carlist.CarListInfosBean;
import com.yuefeng.features.ui.activity.track.HistoryTrackActivity;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;

/**
 * 轨迹
 */

public class CarListPresenter extends BasePresenterImpl<CarListContract.View,
        HistoryTrackActivity> implements CarListContract.Presenter {
    public CarListPresenter(CarListContract.View view, HistoryTrackActivity activity) {
        super(view, activity);
    }

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
                                EventBus.getDefault().post(new CarListEvent(Constans.CARLIST_SSUCESS, o.getMsg()));
                            } else {
                                EventBus.getDefault().post(new CarListEvent(Constans.CARLIST_ERROR, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CarListEvent(Constans.CARLIST_ERROR, e.getMsg()));
                    }
                });

    }

    @Override
    public void getGpsDatasByTer(String function, String terminal, String startTime, String endTime) {

        HttpObservable.getObservable(apiRetrofit.getGpsDatasByTer(function, terminal, startTime, endTime))
//                .subscribe(new HttpResultObserver<ResponseCustom<String>>() {
                .subscribe(new HttpResultObserver<WheelPathBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
                        showLoadingDialog("加载中...");
                    }

                    @Override
                    protected void onSuccess(WheelPathBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.isSuccess()) {
                                EventBus.getDefault().post(new CarListEvent(Constans.TRACK_SSUCESS, o.getMsg()));
                            } else {
                                EventBus.getDefault().post(new CarListEvent(Constans.TRACK_ERROR, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CarListEvent(Constans.TRACK_ERROR, e.getMsg()));
                    }
                });

    }

}
