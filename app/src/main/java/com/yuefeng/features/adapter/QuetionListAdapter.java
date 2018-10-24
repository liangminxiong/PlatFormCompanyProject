package com.yuefeng.features.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.modle.QuestionListBean;

import java.util.List;

public class QuetionListAdapter extends BaseItemDraggableAdapter<QuestionListBean, BaseViewHolder> {

    private String name;
    private String address;

    public QuetionListAdapter(int layoutResId, @Nullable List<QuestionListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, QuestionListBean item) {
        if (item != null && helper != null) {
            name = item.getProblem();
            address = item.getAddress();
            name = TextUtils.isEmpty(name) ? " " : "内容：" + name;
            address = TextUtils.isEmpty(address) ? "" : address;
            helper.setText(R.id.tv_item_name, name)
                    .setGone(R.id.tv_item_type,false)
                    .setTextColor(R.id.tv_item_name, mContext.getResources().getColor(R.color.black_32))
                    .setVisible(R.id.tv_item_other, true)
                    .setText(R.id.tv_item_other, address)
                    .setTextColor(R.id.tv_item_other, mContext.getResources().getColor(R.color.gray_21));
        }
    }
}
