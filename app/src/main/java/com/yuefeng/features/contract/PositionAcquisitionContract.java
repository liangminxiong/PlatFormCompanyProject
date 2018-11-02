package com.yuefeng.features.contract;


import com.common.base.codereview.BaseView;

/*信息采集*/
public interface PositionAcquisitionContract {
    interface View extends BaseView<Object> {
    }

    interface Presenter {
        /*信息采集*/
        void upLoadmapInfo(String function, String pid, String userid, String typeid,
                           String typename, String name, String lnglat, String area,String imageArrays);

        /*信息采集类型*/
        void getCaijiType(String function);
    }
}
