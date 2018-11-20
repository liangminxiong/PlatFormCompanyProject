package com.yuefeng.home.contract;


import com.common.base.codereview.BaseView;

/*消息回复*/
public interface ReplyContract {
    interface View extends BaseView<Object> {
    }

    interface Presenter {
        /*轨迹*/
        void getGpsDatasByTer(String function, String terminal, String startTime, String endTime);
        /*车辆列表*/
        void getCarListInfos(String function, String organid, String userid, String isreg);
    }
}
