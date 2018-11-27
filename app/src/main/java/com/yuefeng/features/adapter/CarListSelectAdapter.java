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
    private int icon_type;
    private int carType;

    public CarListSelectAdapter(int layoutResId, @Nullable List<CarListSelectBean> data,int carType) {
        super(layoutResId, data);
        this.carType = carType;
    }

    @Override
    protected void convert(BaseViewHolder helper, CarListSelectBean item) {
        if (item != null && helper != null) {
            name = item.getName();
            type = item.getType();
            name = TextUtils.isEmpty(name) ? " " : name;
            type = TextUtils.isEmpty(type) ? "2" : type;
            helper.setText(R.id.id_treenode_label, name)
                    .addOnClickListener(R.id.iv_select_tree);
            if (carType == 0) {
                if (type.contains("0")) {
                    icon_type = R.drawable.list_qiche_tingche;
                } else if (type.contains("1")) {
                    icon_type = R.drawable.list_qiche_xingshi;
                } else if (type.contains("2")) {
                    icon_type = R.drawable.list_qiche_xingshi;
                } else {
                    icon_type = R.drawable.list_qiche_ting10;
                }
            }else {
                if (type.contains("0")) {
                    icon_type = R.drawable.list_renyuan_tingzhi;
                } else if (type.contains("1")) {
                    icon_type = R.drawable.list_renyuan_xingshi;
                } else if (type.contains("2")) {
                    icon_type = R.drawable.list_renyuan_xingshi;
                } else {
                    icon_type = R.drawable.list_renyuan_tingche;
                }
            }
            helper.setImageResource(R.id.icon, icon_type);
        }
    }
}
