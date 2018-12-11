package com.yuefeng.login_splash.contract;


import com.common.base.codereview.BaseView;

/**
 * Created  on 2018-05-26.
 * author:seven
 * email:seven2016s@163.com
 */

public interface SignInContract {
    interface View extends BaseView<Object> {

    }

    interface Presenter {
        void signIn(String function, String userid, String terflag, String useridflag,
                    String lon, String lat, String address, String type);

        void getAnnouncementByuserid(String function, String pid, String timestart, String timeend);
    }
}
