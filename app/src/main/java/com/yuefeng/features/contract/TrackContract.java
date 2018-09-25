package com.yuefeng.features.contract;


import com.common.base.codereview.BaseView;


public interface TrackContract {
    interface View extends BaseView<Object> {
    }

    interface Presenter {
        /*轨迹*/
       /* @Query("function") String function,
        @Query("terminal") String terminal,
        @Query("startTime") String startTime,
        @Query("endTime") String endTime*/
        void getGpsDatasByTer(String function, String terminal, String startTime, String endTime);
    }
}
