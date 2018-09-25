package com.yuefeng.features.contract;


import com.common.base.codereview.BaseView;


public interface ProblemUploadContract {
    interface View extends BaseView<Object> {
    }

    interface Presenter {
        void uploadRubbishEvent(String function, String userid, String pid, String problem, String address,
                                String lng, String lat, String type, String imageArrays);
    }
}
