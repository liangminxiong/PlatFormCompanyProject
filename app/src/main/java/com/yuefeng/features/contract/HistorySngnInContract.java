package com.yuefeng.features.contract;


import com.common.base.codereview.BaseView;

/*历史签到*/
public interface HistorySngnInContract {
    interface View extends BaseView<Object> {
    }

    interface Presenter {

        /*获取*/
        void getAppWorkSign(String function, String userid, String timestart, String timeend);
    }
}
