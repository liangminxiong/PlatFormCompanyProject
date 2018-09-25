package com.yuefeng.features.contract;


import com.common.base.codereview.BaseView;


public interface QualityDetailContract {
    interface View extends BaseView<Object> {
    }

    interface Presenter {
        void GetEventdetail( String function, String problemid);
    }
}
