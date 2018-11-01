package com.yuefeng.features.presenter;

import com.common.base.codereview.BasePresenterImpl;
import com.common.network.ApiException;
import com.common.network.HttpObservable;
import com.common.network.HttpResultObserver;
import com.common.utils.Constans;
import com.yuefeng.features.contract.VideoListContract;
import com.yuefeng.features.event.VideoListEvent;
import com.yuefeng.features.modle.video.VideoEquipmentBean;
import com.yuefeng.features.ui.activity.video.VideoSytemListActivity;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;

/**
 * 视频监控
 */

public class VideoListPresenter extends BasePresenterImpl<VideoListContract.View,
        VideoSytemListActivity> implements VideoListContract.Presenter {
    public VideoListPresenter(VideoListContract.View view, VideoSytemListActivity activity) {
        super(view, activity);
    }

    @Override
    public void getVideoList( String pid, String type) {

        HttpObservable.getObservable(apiRetrofit.getVideoList(pid, type))
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
                                EventBus.getDefault().post(new VideoListEvent(Constans.VIDEO_SSUCESS, o.getData()));
                            } else {
                                EventBus.getDefault().post(new VideoListEvent(Constans.VIDEO_ERROR, o.getData()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new VideoListEvent(Constans.VIDEO_ERROR, e.getMsg()));
                    }
                });

    }

}
