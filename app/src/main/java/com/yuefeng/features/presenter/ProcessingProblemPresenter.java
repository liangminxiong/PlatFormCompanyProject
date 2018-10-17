package com.yuefeng.features.presenter;

import com.common.base.codereview.BasePresenterImpl;
import com.common.network.ApiException;
import com.common.network.HttpObservable;
import com.common.network.HttpResultObserver;
import com.common.utils.Constans;
import com.yuefeng.features.contract.QualityGetFragmentContract;
import com.yuefeng.features.event.ProcessingEvent;
import com.yuefeng.features.modle.EventQuestionBean;
import com.yuefeng.features.modle.SubmitBean;
import com.yuefeng.features.ui.activity.QualityInspectionActivity;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;


public class ProcessingProblemPresenter extends BasePresenterImpl<QualityGetFragmentContract.View,
        QualityInspectionActivity> implements QualityGetFragmentContract.Presenter {
    public ProcessingProblemPresenter(QualityGetFragmentContract.View view, QualityInspectionActivity activity) {
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
                                EventBus.getDefault().postSticky(new ProcessingEvent(Constans.CLAIM_SUCESS, o.getMsg()));
                            } else {
                                EventBus.getDefault().postSticky(new ProcessingEvent(Constans.CLAIM_ERROR, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        EventBus.getDefault().postSticky(new ProcessingEvent(Constans.CLAIM_ERROR, e.getMsg()));
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
                            showLoadingDialog("加载中...");
                    }

                    @Override
                    protected void onSuccess(EventQuestionBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.isSuccess()) {
                                int size = o.getMsg().size();
                                if (size == 0) {
                                    EventBus.getDefault().postSticky(new ProcessingEvent(Constans.CHULIZHONG_ERROR, o.getMsg()));
                                }else {
                                    EventBus.getDefault().postSticky(new ProcessingEvent(Constans.CHULIZHONG_SSUCESS, o.getMsg()));
                                }
                            } else {
                                EventBus.getDefault().postSticky(new ProcessingEvent(Constans.CHULIZHONG_ERROR, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().postSticky(new ProcessingEvent(Constans.CHULIZHONG_ERROR, e.getMsg()));
                    }

                    @Override
                    protected void _onError(ApiException error) {
                        dismissLoadingDialog();
                        super._onError(error);
                        EventBus.getDefault().postSticky(new ProcessingEvent(Constans.CHULIZHONG_ERROR, ""));
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        dismissLoadingDialog();
                        EventBus.getDefault().postSticky(new ProcessingEvent(Constans.CHULIZHONG_ERROR, ""));
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        dismissLoadingDialog();
                    }

                    @Override
                    protected void _onNext(EventQuestionBean responseCustom) {
                        super._onNext(responseCustom);
                        dismissLoadingDialog();
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        dismissLoadingDialog();
                    }

                    @Override
                    protected void onStart(Disposable d) {
                        super.onStart(d);
                        EventBus.getDefault().postSticky(new ProcessingEvent(Constans.CHULIZHONG_ERROR, ""));
                        dismissLoadingDialog();
                    }

                    @Override
                    public void onNext(EventQuestionBean eventQuestionBean) {
                        super.onNext(eventQuestionBean);
                        dismissLoadingDialog();
                    }

                });
    }

}
