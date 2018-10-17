package com.yuefeng.features.contract;


import com.common.base.codereview.BaseView;


public interface QualityDetailContract {
    interface View extends BaseView<Object> {
    }

    interface Presenter {
        /*获取详细*/
        void GetEventdetail( String function, String problemid);

        /*认领问题*/
        void updatequestions(String function, String userid, String problemid, String type,
                             String imageArrays, String detail, String pinjia, String paifaid);
    }
}
