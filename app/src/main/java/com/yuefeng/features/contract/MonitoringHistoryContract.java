package com.yuefeng.features.contract;


import com.common.base.codereview.BaseView;

/*历史作业监察*/
public interface MonitoringHistoryContract {
    interface View extends BaseView<Object> {
    }

    interface Presenter {
        /*历史*/
        void getWorkJianCha(String function, String userid, String timestart, String timeend);

    }
}
