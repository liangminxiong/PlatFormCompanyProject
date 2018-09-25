package com.yuefeng.features.presenter;

import com.common.base.codereview.BasePresenterImpl;
import com.common.network.ApiException;
import com.common.network.HttpObservable;
import com.common.network.HttpResultObserver;
import com.common.utils.Constans;
import com.yuefeng.features.contract.QualityDetailContract;
import com.yuefeng.features.event.QualityDetailEvent;
import com.yuefeng.features.modle.GetEventdetailBean;
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
                });
    }

}
