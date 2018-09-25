package com.yuefeng.features.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.modle.GetAllPersonalMsgBean;

import java.util.List;

public class AllPersonalAdapter extends BaseItemDraggableAdapter<GetAllPersonalMsgBean, BaseViewHolder> {

    private String name;

    public AllPersonalAdapter(int layoutResId, @Nullable List<GetAllPersonalMsgBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GetAllPersonalMsgBean item) {
        if (item != null && helper != null) {
            name = item.getName();
            name = TextUtils.isEmpty(name) ? " " : name;

            helper.setText(R.id.tv_item_name, name)
                    .setTextColor(R.id.tv_item_name, mContext.getResources().getColor(R.color.black));
//                    .addOnClickListener(R.id.ll_layout_item);
        }
    }
}
