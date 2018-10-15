package com.yuefeng.features.contract;


import com.common.base.codereview.BaseView;


public interface HistoryLllegalWorkContract {
    interface View extends BaseView<Object> {
    }

    interface Presenter {
        /*违章列表*/
        void getHistoryLllegal(String function, String organid, String userid, String isreg);
    }
}
