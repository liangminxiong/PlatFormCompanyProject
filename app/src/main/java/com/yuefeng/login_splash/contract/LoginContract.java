package com.yuefeng.login_splash.contract;


import com.common.base.codereview.BaseView;

/**
 * Created  on 2018-05-26.
 * author:seven
 * email:seven2016s@163.com
 */

public interface LoginContract {
    interface View extends BaseView<Object> {

    }

    interface Presenter {

        //获取token
        void getToken(String userid, String username, String usericon);

        void login(String funciont, String username, String password, String client);
    }
}
