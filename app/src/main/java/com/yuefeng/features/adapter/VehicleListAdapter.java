package com.yuefeng.features.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.baidu.mapapi.model.LatLng;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.modle.VehicleinfoListBean;
import com.yuefeng.features.ui.activity.JobMonitoringActivity;
import com.yuefeng.utils.BdLocationUtil;

import java.util.List;

public class VehicleListAdapter extends BaseItemDraggableAdapter<VehicleinfoListBean, BaseViewHolder> {

    private String name;
    private String latitude;
    private String longitude;
    private String address;
    private LatLng latLng;

    public VehicleListAdapter(int layoutResId, @Nullable List<VehicleinfoListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, VehicleinfoListBean item) {
        if (item != null && helper != null) {
            name = item.getRegistrationNO();
            name = TextUtils.isEmpty(name) ? " " : name;

            latitude = item.getLatitude();
            longitude = item.getLongitude();
            if (!TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longitude)) {
                latLng = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        address = BdLocationUtil.returnAddress(JobMonitoringActivity.geoCoder, latLng);
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            } else {
                address = "暂无地址";
            }
            if (TextUtils.isEmpty(address)) {
                address = "检索当前地址失败!";
            }

            helper.setText(R.id.tv_item_name, name)
                    .setTextColor(R.id.tv_item_name, mContext.getResources().getColor(R.color.black))
                    .setText(R.id.tv_item_other, address)
                    .setTextColor(R.id.tv_item_other, mContext.getResources().getColor(R.color.gray_99));
        }
    }

}
