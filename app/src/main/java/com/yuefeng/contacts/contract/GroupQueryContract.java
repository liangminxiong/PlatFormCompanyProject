package com.yuefeng.contacts.contract;

import com.common.base.codereview.BaseView;

/*获取所有群组*/
public interface GroupQueryContract {

    interface View extends BaseView<Object> {
    }

    interface Presenter {
        void groupQueryWithUser(String userid);

        /*群组消息*/
        void groupQuery(String groupid);
    }
}
