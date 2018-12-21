package com.yuefeng.contacts.contract;

import com.common.base.codereview.BaseView;

/*获取所有用户,机构*/
public interface FindOrganUserContract {

    interface View extends BaseView<Object> {
    }

    interface Presenter {
        /*获取机构，用户*/
        void findOrganWithID(String id, String name, Integer type);
    }
}
