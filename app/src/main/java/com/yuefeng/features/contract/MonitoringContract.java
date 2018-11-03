package com.yuefeng.features.contract;


import com.common.base.codereview.BaseView;

/*作业监察*/
public interface MonitoringContract {
    interface View extends BaseView<Object> {
    }

    interface Presenter {
        /*车辆列表*/
        void getCarListInfos(String function, String organid, String userid, String isreg);

    }
}
