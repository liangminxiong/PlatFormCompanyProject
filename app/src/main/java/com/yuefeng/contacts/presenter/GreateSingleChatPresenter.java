package com.yuefeng.contacts.presenter;

import com.common.base.codereview.BasePresenterImpl;
import com.common.event.CommonEvent;
import com.common.network.ApiException;
import com.common.network.HttpObservable;
import com.common.network.HttpResultObserver;
import com.common.utils.Constans;
import com.yuefeng.contacts.contract.FindAllUserContract;
import com.yuefeng.contacts.modle.groupchat.AllUserContactsBean;
import com.yuefeng.contacts.modle.groupchat.GroupCreateBean;
import com.yuefeng.contacts.ui.activity.GreateSingleChatActivity;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;

/**
 *单聊
 */

public class GreateSingleChatPresenter extends BasePresenterImpl<FindAllUserContract.View,
        GreateSingleChatActivity> implements FindAllUserContract.Presenter {
    public GreateSingleChatPresenter(FindAllUserContract.View view, GreateSingleChatActivity activity) {
        super(view, activity);
    }

    @Override
    public void findAllUser(Integer page, Integer count, String name, Integer type,String userid) {

        HttpObservable.getObservable(apiRetrofit.findAllUser(page, count, name, type,userid))
//                .subscribe(new HttpResultObserver<ResponseCustom<String>>() {
                .subscribe(new HttpResultObserver<AllUserContactsBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
                        showLoadingDialog("加载中...");
                    }

                    @Override
                    protected void onSuccess(AllUserContactsBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.isSuccess()) {
                                if (o.getCode() == 200)
                                    EventBus.getDefault().post(new CommonEvent(Constans.ALLUSER_SUCCESS, o.getData()));
                            } else {
                                EventBus.getDefault().post(new CommonEvent(Constans.ALLUSER_ERROR, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.ALLUSER_ERROR, e.getMsg()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.ALLUSER_ERROR, ""));
                        super.onError(e);
                    }

                    @Override
                    protected void _onError(ApiException error) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.ALLUSER_ERROR, error.getMsg()));
                        super._onError(error);
                    }
                });

    }

    /*创*/
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
