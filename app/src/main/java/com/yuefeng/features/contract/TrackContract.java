package com.yuefeng.features.contract;


import com.common.base.codereview.BaseView;


public interface TrackContract {
    interface View extends BaseView<Object> {
    }

    interface Presenter {
        /*轨迹*/
        void getGpsDatasByTer(String function, String terminal, String startTime, String endTime);
    }
}
