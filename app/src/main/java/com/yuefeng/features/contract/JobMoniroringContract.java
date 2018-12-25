package com.yuefeng.features.contract;


import com.common.base.codereview.BaseView;

/*作业监控*/
public interface JobMoniroringContract {
    interface View extends BaseView<Object> {
    }

    interface Presenter {
        /*作业监控*/
        void getmonitorinfo(String function, String userid, String pid, String isreg);
        void getmonitorinfo(String function, String userid, String pid, String isreg,String isnew);
    }
}
