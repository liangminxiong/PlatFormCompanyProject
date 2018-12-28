package com.yuefeng.features.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.common.base.codereview.BaseActivity;
import com.common.utils.Constans;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.modle.VehicleinfoListBean;

/*车辆详情*/
public class CarDetailInfosActivity extends BaseActivity {


    private VehicleinfoListBean mBean;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_cardetailinfos;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        mBean = (VehicleinfoListBean) bundle.getSerializable(Constans.GROUPID);
        if (mBean != null) {
            showUIData(mBean);
        }
    }

    private void showUIData(VehicleinfoListBean bean) {
    }

    @Override
    protected void setLisenter() {

    }

    @Override
    protected void widgetClick(View v) {

    }
}
