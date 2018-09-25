package com.yuefeng.features.contract;


import com.common.base.codereview.BaseView;


public interface QualityGetCountContract {
    interface View extends BaseView<Object> {
    }

    interface Presenter {
        /*问题类型总数*/
        void getQuestionCount(String function, String pid, String userid,boolean isFirst);
    }
}
