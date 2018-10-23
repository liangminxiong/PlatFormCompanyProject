package com.yuefeng.features.contract;


import com.common.base.codereview.BaseView;

/*作业考勤*/
public interface JobAttendanceContract {
    interface View extends BaseView<Object> {
    }

    interface Presenter {
        /*获取信息*/
        void getAttendanceInfos(String function, String organid, String userid, String isreg);
    }
}
