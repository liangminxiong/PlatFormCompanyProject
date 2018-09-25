package com.yuefeng.features.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.baidu.mapapi.model.LatLng;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.modle.PersonalinfoListBean;
import com.yuefeng.features.ui.activity.JobMonitoringActivity;
import com.yuefeng.utils.BdLocationUtil;

import java.util.List;

public class PersonalAdapter extends BaseItemDraggableAdapter<PersonalinfoListBean, BaseViewHolder> {

    private String name;
    private String position;
    private String tel;
    private String latitude;
    private String longitude;
    private String address;
    private LatLng latLng;

    public PersonalAdapter(int layoutResId, @Nullable List<PersonalinfoListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PersonalinfoListBean item) {
        if (item != null && helper != null) {
            name = item.getName();
            position = item.getPosition();
            tel = item.getTel();
            name = TextUtils.isEmpty(name) ? " " : name;
            position = TextUtils.isEmpty(position) ? " " : position;
            tel = TextUtils.isEmpty(tel) ? " " : tel;

            latitude = item.getLatitude();
            longitude = item.getLongitude();
            if (!TextUtils.isEmpty(latitude)) {
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
            helper.setText(R.id.tv_item_name, name + "(" + position + ")")
                    .setTextColor(R.id.tv_item_name, mContext.getResources().getColor(R.color.black))
                    .setText(R.id.tv_item_other, address)
                    .setTextColor(R.id.tv_item_other, mContext.getResources().getColor(R.color.gray_99));
        }
    }

}
