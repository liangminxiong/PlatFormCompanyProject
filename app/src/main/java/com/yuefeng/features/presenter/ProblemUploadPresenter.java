package com.yuefeng.features.presenter;

import com.common.base.codereview.BasePresenterImpl;
import com.common.network.ApiException;
import com.common.network.HttpObservable;
import com.common.network.HttpResultObserver;
import com.common.utils.Constans;
import com.yuefeng.features.contract.ProblemUploadContract;
import com.yuefeng.features.event.ProblemEvent;
import com.yuefeng.features.modle.SubmitBean;
import com.yuefeng.features.ui.activity.ProblemUpdateActivity;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;

/**
 * Created  on 2018-05-26.
 * author:seven
 * email:seven2016s@163.com
 */

public class ProblemUploadPresenter extends BasePresenterImpl<ProblemUploadContract.View, ProblemUpdateActivity> implements ProblemUploadContract.Presenter {
    public ProblemUploadPresenter(ProblemUploadContract.View view, ProblemUpdateActivity activity) {
        super(view, activity);
    }

    @Override
    public void uploadRubbishEvent(String function, String userid, String pid, String problem, String address,
                                   String lng, String lat, String type, String imageArrays) {

        HttpObservable.getObservable(apiRetrofit.uploadRubbishEvent(function, userid, pid, problem, address, lng, lat, type, imageArrays))
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
                                EventBus.getDefault().post(new ProblemEvent(Constans.UPLOADSUCESS, o.getMsg()));
                            } else {
                                EventBus.getDefault().post(new ProblemEvent(Constans.USERERROR, o.getMsg()));
                            }
                        }
                    }

                    @Override
                    protected void onFail(ApiException e) {
                        dismissLoadingDialog();
                        EventBus.getDefault().post(new ProblemEvent(Constans.USERERROR, e.getMsg()));
                    }
                });
    }

}
