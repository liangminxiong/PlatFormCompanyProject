package com.yuefeng.contacts.presenter;

import com.common.base.codereview.BasePresenterImpl;
import com.common.event.CommonEvent;
import com.common.network.ApiException;
import com.common.network.HttpObservable;
import com.common.network.HttpResultObserver;
import com.common.utils.Constans;
import com.yuefeng.contacts.contract.GroupQueryContract;
import com.yuefeng.contacts.modle.UserDeatailInfosBean;
import com.yuefeng.home.ui.imActivity.ConversationActivity;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;

/**
 * 用户详情
 */

public class GroupQueryPresenter extends BasePresenterImpl<GroupQueryContract.View,
        ConversationActivity> implements GroupQueryContract.Presenter {
    public GroupQueryPresenter(GroupQueryContract.View view, ConversationActivity activity) {
        super(view, activity);
    }

    /*通讯录*/
    @Override
    public void groupQueryWithUser(String userid) {
        HttpObservable.getObservable(apiRetrofit.findUserWithID(userid))
                .subscribe(new HttpResultObserver<UserDeatailInfosBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
//                        showLoadingDialog("加载中...");
                    }

                    @Override
                    protected void onSuccess(UserDeatailInfosBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.isSuccess()) {
                                if (o.getCode() == 200) {
                                    EventBus.getDefault().post(new CommonEvent(Constans.USERDETAIL_SUCCESS, o.getData()));
                                } else {
                                    EventBus.getDefault().post(new CommonEvent(Constans.USERDETAIL_ERROR, o.getMsg()));
                                }
                            } else {
                                EventBus.getDefault().post(new CommonEvent(Constans.USERDETAIL_ERROR, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.USERDETAIL_ERROR, e.getMsg()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.USERDETAIL_ERROR, ""));
                        super.onError(e);
                    }

                    @Override
                    protected void _onError(ApiException error) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.USERDETAIL_ERROR, error.getMsg()));
                        super._onError(error);
                    }
                });
    }

}
