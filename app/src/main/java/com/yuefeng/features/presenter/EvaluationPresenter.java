package com.yuefeng.features.presenter;

import com.common.base.codereview.BasePresenterImpl;
import com.common.network.ApiException;
import com.common.network.HttpObservable;
import com.common.network.HttpResultObserver;
import com.common.utils.Constans;
import com.yuefeng.features.contract.EvaluationContract;
import com.yuefeng.features.event.EvaluationEvent;
import com.yuefeng.features.modle.SubmitBean;
import com.yuefeng.features.ui.activity.EvaluationEventActivity;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;

/*问题完成*/
public class EvaluationPresenter extends BasePresenterImpl<EvaluationContract.View, EvaluationEventActivity>
        implements EvaluationContract.Presenter {

    public EvaluationPresenter(EvaluationContract.View view, EvaluationEventActivity activity) {
        super(view, activity);
    }

    @Override
    public void updatequestions(String function, String userid, String problemid, String type,
                                String imageArrays, String detail, String pinjia,String paifaid) {

        HttpObservable.getObservable(apiRetrofit.updatequestions(function, userid, problemid, type,
                imageArrays, detail, pinjia,paifaid))
//                .subscribe(new HttpResultObserver<ResponseCustom<String>>() {
                .subscribe(new HttpResultObserver<SubmitBean>() {
                    @Override
                    protected void onLoading(Disposable d) {
                        showLoadingDialog("请稍等...");
                    }

                    @Override
                    protected void onSuccess(SubmitBean o) {
                        dismissLoadingDialog();
                        if (getView() != null) {
                            if (o.isSuccess()) {
                                EventBus.getDefault().post(new EvaluationEvent(Constans.CARRY_SUCESS, o.getMsg()));
                            } else {
                                EventBus.getDefault().post(new EvaluationEvent(Constans.CARRY_ERROR, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new EvaluationEvent(Constans.CARRY_ERROR, e.getMsg()));
                    }
                });
    }

}
