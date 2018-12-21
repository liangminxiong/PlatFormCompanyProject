package com.yuefeng.contacts.presenter;

import com.common.base.codereview.BasePresenterImpl;
import com.common.event.CommonEvent;
import com.common.network.ApiException;
import com.common.network.HttpObservable;
import com.common.network.HttpResultObserver;
import com.common.utils.Constans;
import com.yuefeng.contacts.contract.GreateGroupContract;
import com.yuefeng.contacts.modle.groupchat.GroupCreateBean;
import com.yuefeng.contacts.ui.activity.GreateGroupChatActivity;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;

/**
 *群聊
 */

public class GreateGroupPresenter extends BasePresenterImpl<GreateGroupContract.View,
        GreateGroupChatActivity> implements GreateGroupContract.Presenter {
    public GreateGroupPresenter(GreateGroupContract.View view, GreateGroupChatActivity activity) {
        super(view, activity);
    }

    @Override
    public void groupCreate(String userids, String createuserid, String groupName) {
        HttpObservable.getObservable(apiRetrofit.groupCreate(userids, createuserid, groupName))
                .subscribe(new HttpResultObserver<GroupCreateBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
//                        showLoadingDialog("加载中...");
                    }

                    @Override
                    protected void onSuccess(GroupCreateBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.isSuccess()) {
                                if (o.getCode() == 200) {
                                    EventBus.getDefault().post(new CommonEvent(Constans.GROUPCREATE_SUCCESS, o));
                                } else {
                                    EventBus.getDefault().post(new CommonEvent(Constans.GROUPCREATE_SUCCESS, o.getData()));
                                }
                            } else {
                                EventBus.getDefault().post(new CommonEvent(Constans.GROUPCREATE_ERROR, o.getData()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.GROUPCREATE_ERROR, e.getMsg()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.GROUPCREATE_ERROR, ""));
                        super.onError(e);
                    }

                    @Override
                    protected void _onError(ApiException error) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.GROUPCREATE_ERROR, error.getMsg()));
                        super._onError(error);
                    }
                });
    }

}
