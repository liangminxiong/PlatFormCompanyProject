package com.yuefeng.features.presenter;

import com.common.base.codereview.BasePresenterImpl;
import com.common.event.CommonEvent;
import com.common.network.ApiException;
import com.common.network.HttpObservable;
import com.common.network.HttpResultObserver;
import com.common.utils.Constans;
import com.yuefeng.features.contract.QualityGetFragmentContract;
import com.yuefeng.features.event.AllProblemEvent;
import com.yuefeng.features.event.PendingEvent;
import com.yuefeng.features.modle.EventQuestionBean;
import com.yuefeng.features.modle.SubmitBean;
import com.yuefeng.features.ui.activity.QualityInspectionActivity;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;


public class PendingProblemPresenter extends BasePresenterImpl<QualityGetFragmentContract.View,
        QualityInspectionActivity> implements QualityGetFragmentContract.Presenter {
    public PendingProblemPresenter(QualityGetFragmentContract.View view, QualityInspectionActivity activity) {
        super(view, activity);
    }


    /*问题认领*/
    @Override
    public void updatequestions(String function, String userid, String problemid,
                                String type, String imageArrays, String detail, String pinjia,String paifaid) {
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
                                EventBus.getDefault().postSticky(new PendingEvent(Constans.CLAIM_SUCESS, o.getMsg()));
                                EventBus.getDefault().postSticky(new CommonEvent(Constans.COUNT_AGAIN_SUCESS, o.getMsg()));
                                EventBus.getDefault().postSticky(new AllProblemEvent(Constans.CLAIM_SUCESS, o.getMsg()));
                            } else {
                                EventBus.getDefault().postSticky(new PendingEvent(Constans.CLAIM_ERROR, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        EventBus.getDefault().postSticky(new PendingEvent(Constans.CLAIM_ERROR, e.getMsg()));
                    }
                });
    }

    /*问题类型*/
    @Override
    public void getEventquestion(String function, String pid, String userid, String type, final boolean EventType) {
        HttpObservable.getObservable(apiRetrofit.getEventquestion(function, pid, userid, type))
//                .subscribe(new HttpResultObserver<ResponseCustom<String>>() {
                .subscribe(new HttpResultObserver<EventQuestionBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
                        if (EventType) {
                            showLoadingDialog("加载中...");
                        }
                    }

                    @Override
                    protected void onSuccess(EventQuestionBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.isSuccess()) {
                                EventBus.getDefault().postSticky(new PendingEvent(Constans.DAI_SSUCESS, o.getMsg()));
                            } else {
                                EventBus.getDefault().postSticky(new PendingEvent(Constans.DAI_ERROR, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().postSticky(new PendingEvent(Constans.DAI_ERROR, e.getMsg()));
                    }
                });
    }

}
