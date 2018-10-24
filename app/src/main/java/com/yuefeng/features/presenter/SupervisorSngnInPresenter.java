package com.yuefeng.features.presenter;

import com.common.base.codereview.BasePresenterImpl;
import com.common.network.ApiException;
import com.common.network.HttpObservable;
import com.common.network.HttpResultObserver;
import com.common.utils.Constans;
import com.yuefeng.features.contract.SupervisorSngnInContract;
import com.yuefeng.features.event.SupervisorSngnInEvent;
import com.yuefeng.features.modle.carlist.CarListInfosBean;
import com.yuefeng.features.ui.activity.sngnin.SupervisorSngnInActivity;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;

/**
 * 主管打卡
 */

public class SupervisorSngnInPresenter extends BasePresenterImpl<SupervisorSngnInContract.View,
        SupervisorSngnInActivity> implements SupervisorSngnInContract.Presenter {
    public SupervisorSngnInPresenter(SupervisorSngnInContract.View view, SupervisorSngnInActivity activity) {
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
                                EventBus.getDefault().postSticky(new SupervisorSngnInEvent(Constans.CARLIST_SSUCESS, o.getMsg()));
                            } else {
                                EventBus.getDefault().postSticky(new SupervisorSngnInEvent(Constans.CARLIST_ERROR, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().postSticky(new SupervisorSngnInEvent(Constans.CARLIST_ERROR, e.getMsg()));
                    }
                });

    }

}