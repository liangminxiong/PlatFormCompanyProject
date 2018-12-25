package com.yuefeng.login_splash.presenter;

import com.common.base.codereview.BasePresenterImpl;
import com.common.network.ApiException;
import com.common.network.HttpObservable;
import com.common.network.HttpResultObserver;
import com.common.utils.Constans;
import com.yuefeng.contacts.modle.TokenBean;
import com.yuefeng.contacts.modle.contacts.OrganPersonalBean;
import com.yuefeng.contacts.modle.groupchat.GroupCreateBean;
import com.yuefeng.features.modle.SubmitBean;
import com.yuefeng.home.modle.NewMsgDataBean;
import com.yuefeng.login_splash.contract.SignInContract;
import com.yuefeng.login_splash.event.SignInEvent;
import com.yuefeng.ui.MainActivity;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;

/**
 * 签到
 */

public class SignInPresenter extends BasePresenterImpl<SignInContract.View, MainActivity> implements SignInContract.Presenter {
    public SignInPresenter(SignInContract.View view, MainActivity activity) {
        super(view, activity);
    }

    @Override
    public void signIn(String function, String userid, String terflag, String useridflag,
                       String lon, String lat, String address, String type) {

        HttpObservable.getObservable(apiRetrofit.signIn(function, userid, terflag, useridflag,
                lon, lat, address, type))
//                .subscribe(new HttpResultObserver<ResponseCustom<LoginDataBean>>() {
                .subscribe(new HttpResultObserver<SubmitBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
                    }

                    @Override
                    protected void onSuccess(SubmitBean o) {
                        if (getView() != null) {
                            if (o.isSuccess()) {
                                EventBus.getDefault().postSticky(new SignInEvent(Constans.LOGIN, o.getMsg()));
                            } else {
                                EventBus.getDefault().postSticky(new SignInEvent(Constans.USERERROR, o.getMsgTitle()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        EventBus.getDefault().postSticky(new SignInEvent(Constans.USERERROR, e.getMsg()));
                    }
                });

    }

    @Override
    public void getAnnouncementByuserid(String function, String pid, String timestart, String timeend) {

        HttpObservable.getObservable(apiRetrofit.getAnnouncementByuserid(function, pid, timestart, timeend))
                .subscribe(new HttpResultObserver<NewMsgDataBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
//                        showLoadingDialog("加载中...");
                    }

                    @Override
                    protected void onSuccess(NewMsgDataBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.isSuccess()) {
                                EventBus.getDefault().postSticky(new SignInEvent(Constans.NEW_MSG_SUCCESS, o.getMsg()));
                            } else {
                                EventBus.getDefault().postSticky(new SignInEvent(Constans.NEW_MSG_ERROR, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().postSticky(new SignInEvent(Constans.NEW_MSG_ERROR, e.getMsg()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().postSticky(new SignInEvent(Constans.NEW_MSG_ERROR, ""));
                        super.onError(e);
                    }

                    @Override
                    protected void _onError(ApiException error) {
                        dismissLoadingDialog();
                        EventBus.getDefault().postSticky(new SignInEvent(Constans.NEW_MSG_ERROR, error.getMsg()));
                        super._onError(error);
                    }
                });

    }

    /*获取token*/
    @Override
    public void getToken(String userid, String username, String usericon) {

        HttpObservable.getObservable(apiRetrofit.getToken(userid, username, usericon))
                .subscribe(new HttpResultObserver<TokenBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
//                        showLoadingDialog("加载中...");
                    }

                    @Override
                    protected void onSuccess(TokenBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.isSuccess()) {
                                if (o.getCode() == 200) {
                                    EventBus.getDefault().postSticky(new SignInEvent(Constans.RONGIM_SUCCESS, o));
                                } else {
                                    EventBus.getDefault().postSticky(new SignInEvent(Constans.RONGIM_ERROR, o.getData()));
                                }
                            } else {
                                EventBus.getDefault().postSticky(new SignInEvent(Constans.RONGIM_ERROR, o.getData()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().postSticky(new SignInEvent(Constans.RONGIM_ERROR, e.getMsg()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().postSticky(new SignInEvent(Constans.RONGIM_ERROR, ""));
                        super.onError(e);
                    }

                    @Override
                    protected void _onError(ApiException error) {
                        dismissLoadingDialog();
                        EventBus.getDefault().postSticky(new SignInEvent(Constans.RONGIM_ERROR, error.getMsg()));
                        super._onError(error);
                    }
                });
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
                                    EventBus.getDefault().postSticky(new SignInEvent(Constans.GROUPCREATE_SUCCESS, o));
                                } else {
                                    EventBus.getDefault().postSticky(new SignInEvent(Constans.GROUPCREATE_SUCCESS, o.getData()));
                                }
                            } else {
                                EventBus.getDefault().postSticky(new SignInEvent(Constans.GROUPCREATE_ERROR, o.getData()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().postSticky(new SignInEvent(Constans.GROUPCREATE_ERROR, e.getMsg()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().postSticky(new SignInEvent(Constans.GROUPCREATE_ERROR, ""));
                        super.onError(e);
                    }

                    @Override
                    protected void _onError(ApiException error) {
                        dismissLoadingDialog();
                        EventBus.getDefault().postSticky(new SignInEvent(Constans.GROUPCREATE_ERROR, error.getMsg()));
                        super._onError(error);
                    }
                });
    }

    /*删群*/
    @Override
    public void groupDismiss(String userid, String groupid) {
        HttpObservable.getObservable(apiRetrofit.groupDismiss(userid, groupid))
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
                                    EventBus.getDefault().postSticky(new SignInEvent(Constans.GROUPDISMISS_SUCCESS, o));
                                } else {
                                    EventBus.getDefault().postSticky(new SignInEvent(Constans.GROUPDISMISS_ERROR, o.getData()));
                                }
                            } else {
                                EventBus.getDefault().postSticky(new SignInEvent(Constans.GROUPDISMISS_ERROR, o.getData()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().postSticky(new SignInEvent(Constans.GROUPDISMISS_ERROR, e.getMsg()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().postSticky(new SignInEvent(Constans.GROUPDISMISS_ERROR, ""));
                        super.onError(e);
                    }

                    @Override
                    protected void _onError(ApiException error) {
                        dismissLoadingDialog();
                        EventBus.getDefault().postSticky(new SignInEvent(Constans.GROUPDISMISS_ERROR, error.getMsg()));
                        super._onError(error);
                    }
                });
    }

    /*入群*/
    @Override
    public void groupJoin(String userids, String groupid) {
        HttpObservable.getObservable(apiRetrofit.groupJoin(userids, groupid))
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
                                    EventBus.getDefault().postSticky(new SignInEvent(Constans.GROUPJOIN_SUCCESS, o));
                                } else {
                                    EventBus.getDefault().postSticky(new SignInEvent(Constans.GROUPJOIN_ERROR, o.getData()));
                                }
                            } else {
                                EventBus.getDefault().postSticky(new SignInEvent(Constans.GROUPJOIN_ERROR, o.getData()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().postSticky(new SignInEvent(Constans.GROUPJOIN_ERROR, e.getMsg()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().postSticky(new SignInEvent(Constans.GROUPJOIN_ERROR, ""));
                        super.onError(e);
                    }

                    @Override
                    protected void _onError(ApiException error) {
                        dismissLoadingDialog();
                        EventBus.getDefault().postSticky(new SignInEvent(Constans.GROUPJOIN_ERROR, error.getMsg()));
                        super._onError(error);
                    }
                });
    }

    /*通讯录*/
    @Override
    public void findOrganWithID(String id, String name, Integer type) {
        HttpObservable.getObservable(apiRetrofit.findOrganWithID(id, name, type))
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
                                    EventBus.getDefault().postSticky(new SignInEvent(Constans.CONTACTS_SUCCESS, o.getData()));
                                } else {
                                    EventBus.getDefault().postSticky(new SignInEvent(Constans.CONTACTS_ERROR, o.getMsg()));
                                }
                            } else {
                                EventBus.getDefault().postSticky(new SignInEvent(Constans.CONTACTS_ERROR, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().postSticky(new SignInEvent(Constans.CONTACTS_ERROR, e.getMsg()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().postSticky(new SignInEvent(Constans.CONTACTS_ERROR, ""));
                        super.onError(e);
                    }

                    @Override
                    protected void _onError(ApiException error) {
                        dismissLoadingDialog();
                        EventBus.getDefault().postSticky(new SignInEvent(Constans.CONTACTS_ERROR, error.getMsg()));
                        super._onError(error);
                    }
                });
    }


    /*实时上传经纬度*/
    @Override
    public void uploadLnglat(String function, String type, String lng, String lat, String id, String phone, String address) {
        HttpObservable.getObservable(apiRetrofit.uploadLnglat(function, type, lng, lat, id, phone, address))
                .subscribe(new HttpResultObserver<SubmitBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
//                        showLoadingDialog("加载中...");
                    }

                    @Override
                    protected void onSuccess(SubmitBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.isSuccess()) {
                                EventBus.getDefault().postSticky(new SignInEvent(Constans.UPLOADLNGLAT_SSUCESS, o.getMsg()));
                            } else {
                                EventBus.getDefault().postSticky(new SignInEvent(Constans.UPLOADLNGLAT_ERROR, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().postSticky(new SignInEvent(Constans.UPLOADLNGLAT_ERROR, e.getMsg()));
                    }


                    @Override
                    protected void _onError(ApiException error) {
                        dismissLoadingDialog();
                        super._onError(error);
                        EventBus.getDefault().postSticky(new SignInEvent(Constans.UPLOADLNGLAT_ERROR, ""));
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        dismissLoadingDialog();
                        EventBus.getDefault().postSticky(new SignInEvent(Constans.UPLOADLNGLAT_ERROR, ""));
                    }
                });
    }
}
