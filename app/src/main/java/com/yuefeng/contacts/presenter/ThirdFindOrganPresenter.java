package com.yuefeng.contacts.presenter;

import com.common.base.codereview.BasePresenterImpl;
import com.common.event.CommonEvent;
import com.common.network.ApiException;
import com.common.network.HttpObservable;
import com.common.network.HttpResultObserver;
import com.common.utils.Constans;
import com.yuefeng.contacts.contract.FindOrganUserContract;
import com.yuefeng.contacts.modle.contacts.OrganPersonalBean;
import com.yuefeng.contacts.ui.activity.ThirdGroupNameActivity;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;

/**
 * 所有用户列表
 */

public class ThirdFindOrganPresenter extends BasePresenterImpl<FindOrganUserContract.View,
        ThirdGroupNameActivity> implements FindOrganUserContract.Presenter {
    public ThirdFindOrganPresenter(FindOrganUserContract.View view, ThirdGroupNameActivity activity) {
        super(view, activity);
    }

    /*通讯录*/
    @Override
    public void findOrganWithID(String id, String name, Integer type) {
        HttpObservable.getObservable(apiRetrofit.findOrganWithID(id,  name, type))
                .subscribe(new HttpResultObserver<OrganPersonalBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
                        showLoadingDialog("加载中...");
                    }

                    @Override
                    protected void onSuccess(OrganPersonalBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.isSuccess()) {
                                if (o.getCode() == 200) {
                                    EventBus.getDefault().post(new CommonEvent(Constans.CONTACTS_SUCCESS, o.getData()));
                                } else {
                                    EventBus.getDefault().post(new CommonEvent(Constans.CONTACTS_ERROR, o.getMsg()));
                                }
                            } else {
                                EventBus.getDefault().post(new CommonEvent(Constans.CONTACTS_ERROR, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.CONTACTS_ERROR, e.getMsg()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.CONTACTS_ERROR, ""));
                        super.onError(e);
                    }

                    @Override
                    protected void _onError(ApiException error) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.CONTACTS_ERROR, error.getMsg()));
                        super._onError(error);
                    }
                });
    }

}
