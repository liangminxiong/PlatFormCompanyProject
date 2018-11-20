package com.yuefeng.features.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.modle.PersonalinfoListBean;

import java.util.List;

public class PersonalAdapter extends BaseItemDraggableAdapter<PersonalinfoListBean, BaseViewHolder> {

    private String name;
    private String position;
    private String tel;
    private String address;

    public PersonalAdapter(int layoutResId, @Nullable List<PersonalinfoListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PersonalinfoListBean item) {
        if (item != null && helper != null) {
            name = item.getName();
            position = item.getPosition();
            tel = item.getTel();
            name = TextUtils.isEmpty(name) ? "无" : name;
            position = TextUtils.isEmpty(position) ? "——" : position;
            tel = TextUtils.isEmpty(tel) ? "无" : tel;

            address = TextUtils.isEmpty(item.getAddress()) ? "暂无地址!" : item.getAddress();
            helper.setText(R.id.tv_item_title, name)//+ "(" + position + ")"
                    .setTextColor(R.id.tv_item_title, mContext.getResources().getColor(R.color.black_32))
                    .setGone(R.id.tv_item_type,true)
                    .setText(R.id.tv_item_type, position)
                    .setText(R.id.tv_item_other, address)
                    .setTextColor(R.id.tv_item_type, mContext.getResources().getColor(R.color.gray_21))
                    .setTextColor(R.id.tv_item_other, mContext.getResources().getColor(R.color.gray_21));
        }
    }
}
