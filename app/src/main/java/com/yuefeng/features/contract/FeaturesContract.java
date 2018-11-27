package com.yuefeng.features.contract;


import com.common.base.codereview.BaseView;

/*消息列表*/
public interface FeaturesContract {
    interface View extends BaseView<Object> {
    }

    interface Presenter {

        void getAnnouncementByuserid(String userpid, String timestart, String timeend, String check);
    }
}
