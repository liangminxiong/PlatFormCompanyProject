package com.yuefeng.features.contract;


import com.common.base.codereview.BaseView;


public interface ForWardContract {
    interface View extends BaseView<Object> {
    }

    interface Presenter {
        /*获取主管*/
        void getAllPersonal(String function, String pid);

        /*问题认领*/
        void updatequestions(String function, String userid, String problemid, String type,
                             String imageArrays, String detail, String pinjia,String paifaid);
    }
}
