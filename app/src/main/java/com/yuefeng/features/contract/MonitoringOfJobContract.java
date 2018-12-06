package com.yuefeng.features.contract;


import com.common.base.codereview.BaseView;

/*作业监察*/
public interface MonitoringOfJobContract {
    interface View extends BaseView<Object> {
    }

    interface Presenter {
        /*监察上报*/
        void uploadJianCcha(String function, String userid, String pid, String timestart,
                            String timeend, String timesum, String id,String startAddress,String endAddress);
        /*监察计划*/
        void getJianChaCount(String function, String userid, String timestart, String timeend);
       /*实时上传*/
        void uploadLnglat(String function, String type, String lng, String lat,String id);
    }
}
