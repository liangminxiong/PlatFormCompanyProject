package com.yuefeng.home.contract;


import com.common.base.codereview.BaseView;

/*消息列表*/
public interface HomeContract {
    interface View extends BaseView<Object> {
    }

    interface Presenter {

        void getAnnouncementByuserid(String function, String pid, String timestart, String timeend);
    }
}
