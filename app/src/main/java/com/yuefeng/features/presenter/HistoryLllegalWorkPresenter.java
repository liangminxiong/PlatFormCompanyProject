package com.yuefeng.features.presenter;

import com.common.base.codereview.BasePresenterImpl;
import com.common.network.ApiException;
import com.common.network.HttpObservable;
import com.common.network.HttpResultObserver;
import com.common.utils.Constans;
import com.yuefeng.features.contract.HistoryLllegalWorkContract;
import com.yuefeng.features.event.LllegalWorkEvent;
import com.yuefeng.features.modle.carlist.CarListInfosBean;
import com.yuefeng.features.ui.activity.Lllegalwork.HistoryLllegalWorkActivity;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;

/**
 * 轨迹
 */

public class HistoryLllegalWorkPresenter extends BasePresenterImpl<HistoryLllegalWorkContract.View,
        HistoryLllegalWorkActivity> implements HistoryLllegalWorkContract.Presenter {
    public HistoryLllegalWorkPresenter(HistoryLllegalWorkContract.View view, HistoryLllegalWorkActivity activity) {
        super(view, activity);
    }

    /*查看历史违规*/
    @Override
    public void getHistoryLllegal(String function, String organid, String userid, String isreg) {

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

}
