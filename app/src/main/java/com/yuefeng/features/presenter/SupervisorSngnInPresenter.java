package com.yuefeng.features.presenter;

import com.common.base.codereview.BasePresenterImpl;
import com.common.network.ApiException;
import com.common.network.HttpObservable;
import com.common.network.HttpResultObserver;
import com.common.utils.Constans;
import com.yuefeng.features.contract.SupervisorSngnInContract;
import com.yuefeng.features.event.SupervisorSngnInEvent;
import com.yuefeng.features.modle.SubmitBean;
import com.yuefeng.features.ui.activity.sngnin.SupervisorSngnInActivity;
import com.yuefeng.personaltree.model.PersoanlTreeListBean;

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

    /*获取人员*/
    @Override
    public void getPersontree(String function, String userid, String pid) {

        HttpObservable.getObservable(apiRetrofit.getPersontree(function, userid, pid))
//                .subscribe(new HttpResultObserver<ResponseCustom<String>>() {
                .subscribe(new HttpResultObserver<PersoanlTreeListBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
                        showLoadingDialog("加载中...");
                    }

                    @Override
                    protected void onSuccess(PersoanlTreeListBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.isSuccess()) {
                                EventBus.getDefault().postSticky(new SupervisorSngnInEvent(Constans.PERSONALLIST_SSUCESS, o.getMsg()));
                            } else {
                                EventBus.getDefault().postSticky(new SupervisorSngnInEvent(Constans.PERSONALLIST_ERROR, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().postSticky(new SupervisorSngnInEvent(Constans.PERSONALLIST_ERROR, e.getMsg()));
                    }
                });

    }


    /*签到*/
    @Override
    public void spSignIn(String function, String userid, String terflag, String useridflag,
                       String lon, String lat, String address, String type,String memo ,String imageArrays) {

        HttpObservable.getObservable(apiRetrofit.spSignIn(function, userid, terflag, useridflag,
                lon, lat, address, type,memo,imageArrays))
//                .subscribe(new HttpResultObserver<ResponseCustom<LoginDataBean>>() {
                .subscribe(new HttpResultObserver<SubmitBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
                        showLoadingDialog("签到中...");
                    }

                    @Override
                    protected void onSuccess(SubmitBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.isSuccess()) {
                                EventBus.getDefault().post(new SupervisorSngnInEvent(Constans.LOGIN, o.getMsg()));
                            } else {
                                EventBus.getDefault().post(new SupervisorSngnInEvent(Constans.USERERROR, o.getMsgTitle()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new SupervisorSngnInEvent(Constans.USERERROR, e.getMsg()));
                    }
                });

    }

}