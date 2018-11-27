package com.yuefeng.features.contract;


import com.common.base.codereview.BaseView;

/*历史问题上报*/
public interface HistoryProblemUpContract {
    interface View extends BaseView<Object> {
    }

    interface Presenter {

        /*获取*/
        void getEveQuestionByuserid(String function, String userid, String timestart, String timeend);
    }
}
