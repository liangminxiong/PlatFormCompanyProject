package com.yuefeng.features.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.modle.LllegalworMsgBean;

import java.util.List;

public class HistoryPositionAdapter extends BaseQuickAdapter<LllegalworMsgBean, BaseViewHolder> {

    private String name;
    private String time;
    private String detail;
    private String address;
    private int imageId;

    public HistoryPositionAdapter(int layoutResId, @Nullable List<LllegalworMsgBean> data) {
        super(layoutResId, data);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    protected void convert(BaseViewHolder helper, LllegalworMsgBean item) {
        int position = helper.getLayoutPosition();
        if (position == 0) {
            helper.setBackgroundRes(R.id.ll_layout_item, R.drawable.shape_cricle_bg_gray_top);
        } else {
            helper.setBackgroundRes(R.id.ll_layout_item, R.drawable.shape_cricle_bg_gray_top0);
        }
        if (item != null) {
            name = item.getName();
            time = item.getTime();
            detail = item.getContents();
            address = item.getAddress();
            name = TextUtils.isEmpty(name) ? " " : name;
            time = TextUtils.isEmpty(time) ? " " : time;
            detail = TextUtils.isEmpty(detail) ? " " : detail;
            address = TextUtils.isEmpty(address) ? " " : address;

//            if (time.length() > 16) {
//                time = time.substring(11, 16);
//            }
            helper.setText(R.id.tv_item_title, name)
                    .setText(R.id.tv_item_time, time)
                    .setText(R.id.tv_item_type, address + "\n(" + detail + ")");
            imageId = R.drawable.caiji_wutupian;
            helper.setImageResource(R.id.iv_item_image, imageId);
        }
    }
}
