package com.yuefeng.features.contract;


import com.common.base.codereview.BaseView;

/*主管打卡*/
public interface SupervisorSngnInContract {
    interface View extends BaseView<Object> {
    }

    interface Presenter {
        /*人员列表*/
        void getPersontree(String function, String userid, String pid);

        /*签到*/
        void spSignIn(String function, String userid, String terflag, String useridflag,
                      String lon, String lat, String address, String type, String memo, String imageArrays);
    }
}
