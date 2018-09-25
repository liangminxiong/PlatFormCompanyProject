package com.yuefeng.features.presenter;

import com.common.base.codereview.BasePresenterImpl;
import com.common.network.ApiException;
import com.common.network.HttpObservable;
import com.common.network.HttpResultObserver;
import com.common.utils.Constans;
import com.yuefeng.features.contract.ForWardContract;
import com.yuefeng.features.event.AllPersoanlEvent;
import com.yuefeng.features.modle.GetAllPersonalBean;
import com.yuefeng.features.modle.SubmitBean;
import com.yuefeng.features.ui.activity.ForwardProblemActivity;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;

/**
转发获取主管问题
 */

public class ForWardPresenter extends BasePresenterImpl<ForWardContract.View,
        ForwardProblemActivity> implements ForWardContract.Presenter {
    public ForWardPresenter(ForWardContract.View view, ForwardProblemActivity activity) {
        super(view, activity);
    }

    /*总数*/
    @Override
    public void getAllPersonal(String function, String pid) {

        HttpObservable.getObservable(apiRetrofit.getAllpersonal(function, pid))
//                .subscribe(new HttpResultObserver<ResponseCustom<String>>() {
                .subscribe(new HttpResultObserver<GetAllPersonalBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
                        showLoadingDialog("加载中...");
                    }

                    @Override
                    protected void onSuccess(GetAllPersonalBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.isSuccess()) {
                                EventBus.getDefault().postSticky(new AllPersoanlEvent(Constans.GETPERSONAL_SSUCESS, o.getMsg()));
                            } else {
                                EventBus.getDefault().postSticky(new AllPersoanlEvent(Constans.GETPERSONAL_ERROR, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().postSticky(new AllPersoanlEvent(Constans.GETPERSONAL_ERROR, e.getMsg()));
                    }
                });
    }

    /*派发*/
    @Override
    public void updatequestions(String function, String userid, String problemid, String type,
                                String imageArrays, String detail, String pinjia, String paifaid) {
        HttpObservable.getObservable(apiRetrofit.updatequestions(function, userid, problemid, type,
                imageArrays, detail, pinjia,paifaid))
//                .subscribe(new HttpResultObserver<ResponseCustom<String>>() {
                .subscribe(new HttpResultObserver<SubmitBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
                    }

                    @Override
                    protected void onSuccess(SubmitBean o) {
                        if (getView() != null) {
                            if (o.isSuccess()) {
                                EventBus.getDefault().postSticky(new AllPersoanlEvent(Constans.CLAIM_SUCESS, o.getMsg()));
                            } else {
                                EventBus.getDefault().postSticky(new AllPersoanlEvent(Constans.SUBMITERROR, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        EventBus.getDefault().postSticky(new AllPersoanlEvent(Constans.SUBMITERROR, e.getMsg()));
                    }
                });
    }

}
