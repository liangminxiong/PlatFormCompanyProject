package com.yuefeng.features.contract;


import com.common.base.codereview.BaseView;

/*车辆详情*/
public interface CarDetailInfosContract {
    interface View extends BaseView<Object> {
    }

    interface Presenter {
        void getVehicleDetail(String function, String vid);
    }
}
