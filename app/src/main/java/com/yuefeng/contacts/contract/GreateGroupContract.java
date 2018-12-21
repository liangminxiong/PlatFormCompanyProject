package com.yuefeng.contacts.contract;

import com.common.base.codereview.BaseView;

/*获取所有用户*/
public interface GreateGroupContract {

    interface View extends BaseView<Object> {
    }

    interface Presenter {
        //创建
        void groupCreate(String userids, String createuserid, String groupName);
    }


}
