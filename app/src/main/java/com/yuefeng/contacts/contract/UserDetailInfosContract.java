package com.yuefeng.contacts.contract;

import com.common.base.codereview.BaseView;

/*获取所有用户*/
public interface UserDetailInfosContract {

    interface View extends BaseView<Object> {
    }

    interface Presenter {
        void findUserWithID(String id);
    }
}
