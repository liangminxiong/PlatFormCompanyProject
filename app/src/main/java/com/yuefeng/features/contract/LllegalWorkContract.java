package com.yuefeng.features.contract;


import com.common.base.codereview.BaseView;


public interface LllegalWorkContract {
    interface View extends BaseView<Object> {
    }

    interface Presenter {
        /*车辆列表*/
        void getCarListInfos(String function, String organid, String userid, String isreg);

        /*人员列表*/
        void getPersontree(String function, String userid, String pid);

        /*违章列表*/
        void getWeigui(String function, String pid, String timestatr,
                       String timeend, String vid, String type,int typeWhat);

    }
}
