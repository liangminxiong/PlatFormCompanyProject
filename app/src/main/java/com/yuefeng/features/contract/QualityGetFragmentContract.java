package com.yuefeng.features.contract;


import com.common.base.codereview.BaseView;


public interface QualityGetFragmentContract {
    interface View extends BaseView<Object> {
    }

    interface Presenter {

        /*问题处理类型*/
        void updatequestions(String function, String userid, String problemid, String type,
                             String imageArrays, String detail, String pinjia, String paifaid);

        /*问题类型处理*/
        void getEventquestion(String function, String pid, String userid, String type, boolean isFirst);
    }
}
