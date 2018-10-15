package com.yuefeng.features.contract;


import com.common.base.codereview.BaseView;

/*车辆列表*/
public interface CarListContract {
    interface View extends BaseView<Object> {
    }

    interface Presenter {
        /*轨迹*/
        void getGpsDatasByTer(String function, String terminal, String startTime, String endTime);
        /*车辆列表*/
        void getCarListInfos(String function, String organid, String userid, String isreg);
    }
}
