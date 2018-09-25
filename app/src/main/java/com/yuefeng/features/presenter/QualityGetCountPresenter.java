package com.yuefeng.features.presenter;

import com.common.base.codereview.BasePresenterImpl;
import com.common.event.CommonEvent;
import com.common.network.ApiException;
import com.common.network.HttpObservable;
import com.common.network.HttpResultObserver;
import com.common.utils.Constans;
import com.yuefeng.features.contract.QualityGetCountContract;
import com.yuefeng.features.modle.GetQuestionCountBean;
import com.yuefeng.features.ui.activity.QualityInspectionActivity;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;


public class QualityGetCountPresenter extends BasePresenterImpl<QualityGetCountContract.View,
        QualityInspectionActivity> implements QualityGetCountContract.Presenter {
    public QualityGetCountPresenter(QualityGetCountContract.View view, QualityInspectionActivity activity) {
        super(view, activity);
    }

    /*总数*/
    @Override
    public void getQuestionCount(String function, String pid, String userid, final boolean isFirst) {

        HttpObservable.getObservable(apiRetrofit.getQuestionCount(function, pid, userid))
//                .subscribe(new HttpResultObserver<ResponseCustom<String>>() {
                .subscribe(new HttpResultObserver<GetQuestionCountBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
                        if (isFirst) {
                            showLoadingDialog("加载中...");
                        }
                    }

                    @Override
                    protected void onSuccess(GetQuestionCountBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.isSuccess()) {
                                EventBus.getDefault().postSticky(new CommonEvent(Constans.COUNT_SUCESS, o.getMsg()));
                            } else {
                                EventBus.getDefault().postSticky(new CommonEvent(Constans.GETCOUNT_ERROR, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().postSticky(new CommonEvent(Constans.GETCOUNT_ERROR, e.getMsg()));
                    }
                });
    }
}
