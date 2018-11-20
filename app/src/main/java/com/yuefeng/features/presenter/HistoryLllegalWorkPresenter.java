package com.yuefeng.features.presenter;

import com.common.base.codereview.BasePresenterImpl;
import com.common.event.CommonEvent;
import com.common.network.ApiException;
import com.common.network.HttpObservable;
import com.common.network.HttpResultObserver;
import com.common.utils.Constans;
import com.yuefeng.features.contract.HistoryLllegalWorkContract;
import com.yuefeng.features.modle.LllegalworkBean;
import com.yuefeng.features.ui.activity.Lllegalwork.HistoryLllegalWorkActivity;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;

/**
 * 历史违规
 */

public class HistoryLllegalWorkPresenter extends BasePresenterImpl<HistoryLllegalWorkContract.View,
        HistoryLllegalWorkActivity> implements HistoryLllegalWorkContract.Presenter {
    public HistoryLllegalWorkPresenter(HistoryLllegalWorkContract.View view, HistoryLllegalWorkActivity activity) {
        super(view, activity);
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
                                EventBus.getDefault().post(new CommonEvent(Constans.CARLIST_SSUCESS, o.getMsg()));
                            }
                        } else {
                            EventBus.getDefault().post(new CommonEvent(Constans.CARLIST_ERROR, o.getMsgTitle()));
                        }
                    }


                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.CARLIST_ERROR, e.getMsg()));
                    }


                    @Override
                    protected void _onError(ApiException error) {
                        dismissLoadingDialog();
                        super._onError(error);
                        EventBus.getDefault().post(new CommonEvent(Constans.CARLIST_ERROR, ""));
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.CARLIST_ERROR,""));
                    }
                });
    }

}
