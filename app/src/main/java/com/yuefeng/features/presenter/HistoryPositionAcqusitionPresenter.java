package com.yuefeng.features.presenter;

import com.common.base.codereview.BasePresenterImpl;
import com.common.event.CommonEvent;
import com.common.network.ApiException;
import com.common.network.HttpObservable;
import com.common.network.HttpResultObserver;
import com.common.utils.Constans;
import com.yuefeng.features.contract.HistoryPositionAcqusitionContract;
import com.yuefeng.features.modle.GetHistoryCaijiInfosBean;
import com.yuefeng.features.ui.activity.position.HistoryPositionAcqusitionActivity;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;

/**
 * 历史违规
 */

public class HistoryPositionAcqusitionPresenter extends BasePresenterImpl<HistoryPositionAcqusitionContract.View,
        HistoryPositionAcqusitionActivity> implements HistoryPositionAcqusitionContract.Presenter {
    public HistoryPositionAcqusitionPresenter(HistoryPositionAcqusitionContract.View view, HistoryPositionAcqusitionActivity activity) {
        super(view, activity);
    }

    /*获取违规*/
    @Override
    public void getCarListInfos(String function, String userid, String timestart, String timeend) {
        HttpObservable.getObservable(apiRetrofit.getHistoryCaijiInfo(function, userid, timestart, timeend))
                .subscribe(new HttpResultObserver<GetHistoryCaijiInfosBean>() {

                    @Override
                    protected void onLoading(Disposable d) {
                        showLoadingDialog("加载中...");
                    }

                    @Override
                    protected void onSuccess(GetHistoryCaijiInfosBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.isSuccess()) {
                                EventBus.getDefault().post(new CommonEvent(Constans.GETCAIJIHISTORY_SSUCESS, o.getMsg()));
                            }
                        } else {
                            EventBus.getDefault().post(new CommonEvent(Constans.GETCAIJIHISTORY_ERROR, o.getMsgTitle()));
                        }
                    }


                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.GETCAIJIHISTORY_ERROR, e.getMsg()));
                    }


                    @Override
                    protected void _onError(ApiException error) {
                        dismissLoadingDialog();
                        super._onError(error);
                        EventBus.getDefault().post(new CommonEvent(Constans.GETCAIJIHISTORY_ERROR, ""));
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.GETCAIJIHISTORY_ERROR, ""));
                    }
                });
    }

}
