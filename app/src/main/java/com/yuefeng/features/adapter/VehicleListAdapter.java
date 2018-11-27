package com.yuefeng.features.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.modle.VehicleinfoListBean;

import java.util.List;

public class VehicleListAdapter extends BaseItemDraggableAdapter<VehicleinfoListBean, BaseViewHolder> {

    private String name;
    private String address;

    public VehicleListAdapter(int layoutResId, @Nullable List<VehicleinfoListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, VehicleinfoListBean item) {
        if (item != null && helper != null) {
            name = item.getRegistrationNO();
            name = TextUtils.isEmpty(name) ? " " : name;
            address = TextUtils.isEmpty(item.getAddress()) ? "加载中..." : item.getAddress();
            helper.setText(R.id.tv_item_title, name)
                    .setGone(R.id.tv_item_type, false)
                    .setTextColor(R.id.tv_item_title, mContext.getResources().getColor(R.color.black_32))
                    .setText(R.id.tv_item_other, address)
                    .setTextColor(R.id.tv_item_other, mContext.getResources().getColor(R.color.gray_21));
        }
    }

}
