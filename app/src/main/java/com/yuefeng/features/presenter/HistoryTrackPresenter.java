package com.yuefeng.features.presenter;

import com.common.base.codereview.BasePresenterImpl;
import com.common.network.ApiException;
import com.common.network.HttpObservable;
import com.common.network.HttpResultObserver;
import com.common.utils.Constans;
import com.yuefeng.features.contract.TrackContract;
import com.yuefeng.features.event.TrackEvent;
import com.yuefeng.features.modle.WheelPathBean;
import com.yuefeng.features.ui.activity.track.HistoryTrackActivity;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;

/**
 * 历史轨迹
 */

public class HistoryTrackPresenter extends BasePresenterImpl<TrackContract.View,
        HistoryTrackActivity> implements TrackContract.Presenter {
    public HistoryTrackPresenter(TrackContract.View view, HistoryTrackActivity activity) {
        super(view, activity);
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
                                EventBus.getDefault().postSticky(new TrackEvent(Constans.TRACK_SSUCESS, o.getMsg()));
                            } else {
                                EventBus.getDefault().postSticky(new TrackEvent(Constans.TRACK_ERROR, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().postSticky(new TrackEvent(Constans.TRACK_ERROR, e.getMsg()));
                    }
                });

    }

}
