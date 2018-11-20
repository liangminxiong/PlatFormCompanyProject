package com.yuefeng.features.contract;


import com.common.base.codereview.BaseView;

/*历史采集*/
public interface HistoryPositionAcqusitionContract {
    interface View extends BaseView<Object> {
    }

    interface Presenter {
        void getCarListInfos(String function, String userid, String timestart, String timeend);
    }
}
