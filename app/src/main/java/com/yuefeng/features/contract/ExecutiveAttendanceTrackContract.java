package com.yuefeng.features.contract;


import com.common.base.codereview.BaseView;

/*主管轨迹*/
public interface ExecutiveAttendanceTrackContract {
    interface View extends BaseView<Object> {
    }

    interface Presenter {
        void getPersonidTrack(String function, String userid, String timestart, String timeend);
    }
}
