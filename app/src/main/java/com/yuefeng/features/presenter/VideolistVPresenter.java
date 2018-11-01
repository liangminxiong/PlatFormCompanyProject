package com.yuefeng.features.presenter;

import com.common.base.codereview.BasePresenterImpl;
import com.common.network.ApiException;
import com.common.network.HttpObservable;
import com.common.network.HttpResultObserver;
import com.common.utils.Constans;
import com.yuefeng.features.contract.VideolistVContract;
import com.yuefeng.features.event.LllegalWorkEvent;
import com.yuefeng.features.modle.carlist.CarListInfosBean;
import com.yuefeng.features.modle.video.VideoEquipmentBean;
import com.yuefeng.features.ui.activity.video.VideoCameraActivity;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;

/**
 * 视频
 */

public class VideolistVPresenter extends BasePresenterImpl<VideolistVContract.View,
        VideoCameraActivity> implements VideolistVContract.Presenter {
    public VideolistVPresenter(VideolistVContract.View view, VideoCameraActivity activity) {
        super(view, activity);
    }

    /*车辆列表*/
    @Override
    public void getCarListInfos(String function, String organid, String userid, String isreg) {

        HttpObservable.getObservable(apiRetrofit.getVideoTree(function, organid, userid, isreg))
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

    /*视频列表*/
    @Override
    public void getVideoTree(String function, String organid, String userid, String isreg) {

        HttpObservable.getObservable(apiRetrofit.getVideoTree(function, organid, userid, isreg))
//                .subscribe(new HttpResultObserver<ResponseCustom<String>>() {
                .subscribe(new HttpResultObserver<VideoEquipmentBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
                        showLoadingDialog("加载中...");
                    }

                    @Override
                    protected void onSuccess(VideoEquipmentBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.isSuccess()) {
                                EventBus.getDefault().postSticky(new LllegalWorkEvent(Constans.CARLIST_SSUCESS, o.getData()));
                            } else {
                                EventBus.getDefault().postSticky(new LllegalWorkEvent(Constans.CARLIST_ERROR, o.getData()));
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


}
