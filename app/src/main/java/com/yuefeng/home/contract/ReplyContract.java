package com.yuefeng.home.contract;


import com.common.base.codereview.BaseView;

/*消息回复*/
public interface ReplyContract {
    interface View extends BaseView<Object> {
    }

    interface Presenter {
        void doReview(String pid, String reviewid, String reviewpersonel, String reviewcontent, String imageurls);
    }
}
