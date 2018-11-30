package com.yuefeng.home.contract;


import com.common.base.codereview.BaseView;

/*App历史版本*/
public interface AppVersionContract {
    interface View extends BaseView<Object> {
    }

    interface Presenter {
        void getAppHistoryVersion(int page, int limit, String timestart, String timeend, boolean isLoad);
    }
}
