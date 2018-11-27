package com.yuefeng.home.contract;


import com.common.base.codereview.BaseView;

/*消息详情*/
public interface MsgDetailInfosContract {
    interface View extends BaseView<Object> {
    }

    interface Presenter {

        void getMsgDetail(String reviewid);
    }
}
