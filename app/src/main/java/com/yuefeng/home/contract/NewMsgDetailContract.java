package com.yuefeng.home.contract;


import com.common.base.codereview.BaseView;

/*最新*/
public interface NewMsgDetailContract {
    interface View extends BaseView<Object> {
    }

    interface Presenter {

        void getAnnouncementDetail(String function, String pid);
    }
}
