package com.yuefeng.home.contract;


import com.common.base.codereview.BaseView;

/*公告详情*/
public interface AnnouncementDetailContract {
    interface View extends BaseView<Object> {
    }

    interface Presenter {

        void getAnnouncementDetail(String function, String pid);
    }
}
