package com.yuefeng.features.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.modle.GetCaijiTypeMsgBean;

import java.util.List;

public class StringSingleAdapter extends BaseItemDraggableAdapter<GetCaijiTypeMsgBean, BaseViewHolder> {


    public StringSingleAdapter(int layoutResId, @Nullable List<GetCaijiTypeMsgBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GetCaijiTypeMsgBean item) {
        if (item != null && helper != null) {
            String data = item.getData();
            data = TextUtils.isEmpty(data) ? "" : data;
            helper.setText(R.id.tv_item_title, data);
        }
    }
}
