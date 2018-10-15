package com.yuefeng.features.presenter;

import com.common.base.codereview.BasePresenterImpl;
import com.common.network.ApiException;
import com.common.network.HttpObservable;
import com.common.network.HttpResultObserver;
import com.common.utils.Constans;
import com.yuefeng.features.contract.LllegalWorkContract;
import com.yuefeng.features.event.LllegalWorkEvent;
import com.yuefeng.features.modle.carlist.CarListInfosBean;
import com.yuefeng.features.ui.activity.Lllegalwork.LllegalWorkActivity;

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

    @Override
    public void getPersonalListInfos(String function, String organid, String userid, String isreg) {

    }

    @Override
    public void getCarLllegalListInfos(String function, String organid, String userid, String isreg) {

    }

    @Override
    public void getPersonalLllegalListInfos(String function, String organid, String userid, String isreg) {

    }

}
