package com.yuefeng.home.contract;


import com.common.base.codereview.BaseView;

/*消息列表*/
public interface NewRemindNorDetailContract {
    interface View extends BaseView<Object> {
    }

    interface Presenter {
        void getIsReadDetail(String id);
        void getAlarmedit(String id, String timestart, String timeend);
    }
}
