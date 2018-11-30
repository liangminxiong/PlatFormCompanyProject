package com.yuefeng.home.contract;


import com.common.base.codereview.BaseView;

/*消息列表*/
public interface MsgListInfosContract {
    interface View extends BaseView<Object> {
    }

    interface Presenter {

        void getAnMentDataList(String function, String pid,String timestart, String timeend,
                               int page, int limit, boolean isShowLoad);
    }
}
