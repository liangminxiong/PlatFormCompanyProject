package com.yuefeng.features.contract;


import com.common.base.codereview.BaseView;

/*信息采集*/
public interface PositionAcquisitionContract {
    interface View extends BaseView<Object> {
    }

    interface Presenter {
        /*信息采集*/
        void msgColection(String function, String organid, String userid, String isreg);
    }
}
