package com.yuefeng.home.contract;


import com.common.base.codereview.BaseView;

/*报警列表*/
public interface NewRemindNorListInfosContract {
    interface View extends BaseView<Object> {
    }

    interface Presenter {

        void getalarmpage(String pid, String timestart, String timeend, String page, String limit, boolean isLoad);
    }
}
