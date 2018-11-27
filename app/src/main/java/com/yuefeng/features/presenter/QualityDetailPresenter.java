package com.yuefeng.features.presenter;

import com.common.base.codereview.BasePresenterImpl;
import com.common.event.CommonEvent;
import com.common.network.ApiException;
import com.common.network.HttpObservable;
import com.common.network.HttpResultObserver;
import com.common.utils.Constans;
import com.yuefeng.features.contract.QualityDetailContract;
import com.yuefeng.features.event.AllProblemEvent;
import com.yuefeng.features.event.PendingEvent;
import com.yuefeng.features.event.QualityDetailEvent;
import com.yuefeng.features.modle.GetEventdetailBean;
import com.yuefeng.features.modle.SubmitBean;
import com.yuefeng.features.ui.activity.QualityInspectionDetailActivity;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;

/*问题详情*/
public class QualityDetailPresenter extends BasePresenterImpl<QualityDetailContract.View, QualityInspectionDetailActivity>
        implements QualityDetailContract.Presenter {

    public QualityDetailPresenter(QualityDetailContract.View view, QualityInspectionDetailActivity activity) {
        super(view, activity);
    }

    @Override
    public void GetEventdetail(String function,String problemid) {

        HttpObservable.getObservable(apiRetrofit.GetEventdetail(function, problemid))
//                .subscribe(new HttpResultObserver<ResponseCustom<String>>() {
                .subscribe(new HttpResultObserver<GetEventdetailBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
                        showLoadingDialog("请稍等...");
                    }

                    @Override
                    protected void onSuccess(GetEventdetailBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.isSuccess()) {
                                EventBus.getDefault().post(new QualityDetailEvent(Constans.DETAIL_SSUCESS, o.getMsg()));
                            } else {
                                EventBus.getDefault().post(new QualityDetailEvent(Constans.USERERROR, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new QualityDetailEvent(Constans.USERERROR, e.getMsg()));
                    }

                    @Override
                    protected void _onError(ApiException error) {
                        dismissLoadingDialog();
                        super._onError(error);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        dismissLoadingDialog();
                    }
                });
    }

    /*问题认领*/
    @Override
    public void updatequestions(String function, String userid, String problemid,
                                String type, String imageArrays, String detail, String pinjia, String paifaid) {
        HttpObservable.getObservable(apiRetrofit.updatequestions(function, userid, problemid, type,
                imageArrays, detail, pinjia, paifaid))
//                .subscribe(new HttpResultObserver<ResponseCustom<String>>() {
                .subscribe(new HttpResultObserver<SubmitBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
                        showLoadingDialog("认领中...");
                    }

                    @Override
                    protected void onSuccess(SubmitBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.isSuccess()) {
                                EventBus.getDefault().postSticky(new PendingEvent(Constans.CLAIM_SUCESS, o.getMsg()));
                                EventBus.getDefault().postSticky(new CommonEvent(Constans.COUNT_AGAIN_SUCESS, o.getMsg()));
                                EventBus.getDefault().postSticky(new AllProblemEvent(Constans.CLAIM_SUCESS, o.getMsg()));
                                EventBus.getDefault().postSticky(new QualityDetailEvent(Constans.CLAIM_SUCESS, o.getMsg()));
                            } else {
                                EventBus.getDefault().postSticky(new QualityDetailEvent(Constans.CLAIM_ERROR, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().postSticky(new QualityDetailEvent(Constans.CLAIM_ERROR, e.getMsg()));
                    }

                    @Override
                    protected void _onError(ApiException error) {
                        dismissLoadingDialog();
                        super._onError(error);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        dismissLoadingDialog();
                    }
                });
    }

}
