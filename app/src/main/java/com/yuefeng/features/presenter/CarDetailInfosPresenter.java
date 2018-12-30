package com.yuefeng.features.presenter;

import com.common.base.codereview.BasePresenterImpl;
import com.common.event.CommonEvent;
import com.common.network.ApiException;
import com.common.network.HttpObservable;
import com.common.network.HttpResultObserver;
import com.common.utils.Constans;
import com.yuefeng.features.contract.CarDetailInfosContract;
import com.yuefeng.features.modle.CarDetailInfosBean;
import com.yuefeng.features.ui.activity.CarDetailInfosActivity;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;

/**
 * 车辆详情
 */

public class CarDetailInfosPresenter extends BasePresenterImpl<CarDetailInfosContract.View,
        CarDetailInfosActivity> implements CarDetailInfosContract.Presenter {
    public CarDetailInfosPresenter(CarDetailInfosContract.View view, CarDetailInfosActivity activity) {
        super(view, activity);
    }

    @Override
    public void getVehicleDetail(String function, String vid) {
        HttpObservable.getObservable(apiRetrofit.getVehicleDetail(function, vid))
//                .subscribe(new HttpResultObserver<ResponseCustom<String>>() {
                .subscribe(new HttpResultObserver<CarDetailInfosBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
                        showLoadingDialog("加载中...");
                    }

                    @Override
                    protected void onSuccess(CarDetailInfosBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.isSuccess()) {
                                EventBus.getDefault().post(new CommonEvent(Constans.CARDETAIL_SUCCESS, o.getMsg()));
                            } else {
                                EventBus.getDefault().post(new CommonEvent(Constans.CARDETAIL_SUCCESS, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.CARDETAIL_SUCCESS, e.getMsg()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.CARDETAIL_SUCCESS, ""));
                    }

                    @Override
                    protected void _onError(ApiException error) {
                        super._onError(error);
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.CARDETAIL_SUCCESS, ""));
                    }
                });
    }
}
