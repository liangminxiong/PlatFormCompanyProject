package com.yuefeng.features.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.modle.carlist.CarListSelectBean;

import java.util.List;

public class CarListSelectAdapter extends BaseItemDraggableAdapter<CarListSelectBean, BaseViewHolder> {

    private String name;
    private String type;
    private int icon_car_image;

    public CarListSelectAdapter(int layoutResId, @Nullable List<CarListSelectBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CarListSelectBean item) {
        if (item != null && helper != null) {
            name = item.getName();
            type = item.getType();
            name = TextUtils.isEmpty(name) ? " " : name;
            type = TextUtils.isEmpty(type) ? "2" : type;
            helper.setText(R.id.id_treenode_label, name);
            if (type.contains("0")) {
                icon_car_image = R.drawable.icon_car_offline;
            } else if (type.contains("1")) {
                icon_car_image = R.drawable.icon_car_online;
            } else if (type.contains("2")) {
                icon_car_image = R.drawable.icon_car_green;
            } else {
                icon_car_image = R.drawable.icon_car_red;
            }
            helper.setImageResource(R.id.icon, icon_car_image);
        }
    }
}
