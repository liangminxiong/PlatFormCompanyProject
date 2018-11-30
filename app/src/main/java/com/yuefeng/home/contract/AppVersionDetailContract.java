package com.yuefeng.home.contract;


import com.common.base.codereview.BaseView;

/*App历史版本详情*/
public interface AppVersionDetailContract {
    interface View extends BaseView<Object> {
    }

    interface Presenter {
        void getAppVersionDetail(String id);
    }
}
