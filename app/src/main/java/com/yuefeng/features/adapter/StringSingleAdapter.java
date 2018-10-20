package com.yuefeng.features.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yuefeng.commondemo.R;

import java.util.List;

public class StringSingleAdapter extends BaseItemDraggableAdapter<String, BaseViewHolder> {


    public StringSingleAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        if (item != null && helper != null) {
            helper.setText(R.id.tv_item_name, item);
        }
    }
}
