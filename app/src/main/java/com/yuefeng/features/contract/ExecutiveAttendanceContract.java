package com.yuefeng.features.contract;


import com.common.base.codereview.BaseView;

/*主管人员树*/
public interface ExecutiveAttendanceContract {
    interface View extends BaseView<Object> {
    }

    interface Presenter {

        void getSignTree(String function, String pid);
        void getPersonalMonitor(String function, String idflags);
    }
}
