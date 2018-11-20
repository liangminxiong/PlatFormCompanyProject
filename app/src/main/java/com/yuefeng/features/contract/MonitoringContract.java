package com.yuefeng.features.contract;


import com.common.base.codereview.BaseView;

/*作业监察*/
public interface MonitoringContract {
    interface View extends BaseView<Object> {
    }

    interface Presenter {
        /*人员列表*/
        void getPersontree(String function, String userid, String pid);

        /*签到*/
        void uploadWorkSign(String function, String pid, String userid, String address,
                            String lat, String lon, String personids, String imageArrays, String memo);

    }
}
