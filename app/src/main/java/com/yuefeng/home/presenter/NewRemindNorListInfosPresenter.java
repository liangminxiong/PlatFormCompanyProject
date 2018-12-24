package com.yuefeng.home.presenter;

import com.common.base.codereview.BasePresenterImpl;
import com.common.event.CommonEvent;
import com.common.network.ApiException;
import com.common.network.HttpObservable;
import com.common.network.HttpResultObserver;
import com.common.utils.Constans;
import com.yuefeng.features.modle.AlarmListBean;
import com.yuefeng.home.contract.NewRemindNorListInfosContract;
import com.yuefeng.home.ui.activity.NewRemindNorListInfosActivtiy;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;

/**
 * 报警列表
 */

public class NewRemindNorListInfosPresenter extends BasePresenterImpl<NewRemindNorListInfosContract.View,
        NewRemindNorListInfosActivtiy> implements NewRemindNorListInfosContract.Presenter {
    public NewRemindNorListInfosPresenter(NewRemindNorListInfosContract.View view, NewRemindNorListInfosActivtiy activity) {
        super(view, activity);
    }

    @Override
    public void getalarmpage(String pid, String timestart, String timeend, String page, String limit, final boolean isLoad) {

        HttpObservable.getObservable(apiRetrofit.getalarmpage(pid, timestart, timeend, page, limit))
//                .subscribe(new HttpResultObserver<ResponseCustom<String>>() {
                .subscribe(new HttpResultObserver<AlarmListBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
                        if (isLoad) {
                            showLoadingDialog("加载中...");
                        }
                    }

                    @Override
                    protected void onSuccess(AlarmListBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.getCode() == 0) {
                                EventBus.getDefault().post(new CommonEvent(Constans.ANMENT_LIST_SSUCESS, o));
                            } else {
                                EventBus.getDefault().post(new CommonEvent(Constans.ANMENT_LIST_ERROR_NULL, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.ANMENT_LIST_ERROR_NULL, e.getMsg()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.ANMENT_LIST_ERROR_NULL, ""));
                        super.onError(e);
                    }

                    @Override
                    protected void _onError(ApiException error) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new CommonEvent(Constans.ANMENT_LIST_ERROR_NULL, error.getMsg()));
                        super._onError(error);
                    }
                });

    }

}
