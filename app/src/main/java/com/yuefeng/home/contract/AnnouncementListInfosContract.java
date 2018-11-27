package com.yuefeng.home.contract;


import com.common.base.codereview.BaseView;

/*公告列表*/
public interface AnnouncementListInfosContract {
    interface View extends BaseView<Object> {
    }

    interface Presenter {

        void getAnnouncementByuserid(String userpid, String timestart, String timeend, String check);
    }
}
