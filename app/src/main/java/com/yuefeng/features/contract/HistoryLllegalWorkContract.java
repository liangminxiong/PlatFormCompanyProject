package com.yuefeng.features.contract;


import com.common.base.codereview.BaseView;


public interface HistoryLllegalWorkContract {
    interface View extends BaseView<Object> {
    }

    interface Presenter {
        /*违章列表*/
        void getWeigui(String function, String pid, String timestatr,
                       String timeend, String vid, String type,int typeWhat);
    }
}
