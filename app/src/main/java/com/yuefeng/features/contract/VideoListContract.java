package com.yuefeng.features.contract;


import com.common.base.codereview.BaseView;


public interface VideoListContract {
    interface View extends BaseView<Object> {
    }

    interface Presenter {
        void getVideoList(String pid, String type);
    }
}
