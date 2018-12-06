package com.yuefeng.features.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.common.utils.GlideUtils;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.modle.HistorySngnInListDataBean;
import com.yuefeng.photo.utils.ImageHelper;

import java.util.List;

public class HistoryMonitoringSngnInAdapter extends BaseItemDraggableAdapter<HistorySngnInListDataBean, BaseViewHolder> {

    private String name;
    private String time;
    private String detail;
    private String address;
    private int imageId;
    private String mImageUrl;

    public HistoryMonitoringSngnInAdapter(int layoutResId, @Nullable List<HistorySngnInListDataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HistorySngnInListDataBean item) {
        int position = helper.getLayoutPosition();
        if (position == 0) {
            helper.setBackgroundRes(R.id.ll_layout_item, R.drawable.shape_cricle_bg_gray_top);
        } else {
            helper.setBackgroundRes(R.id.ll_layout_item, R.drawable.shape_cricle_bg_gray_top0);
        }
        if (item != null && helper != null) {
            name = item.getMemo();
            time = item.getTime();
            detail = item.getPersonalName();
            address = item.getAddress();
            name = TextUtils.isEmpty(name) ? " " : name;
            time = TextUtils.isEmpty(time) ? " " : time;
            detail = TextUtils.isEmpty(detail) ? " " : detail;
            address = TextUtils.isEmpty(address) ? " " : address;
            TextView tv_item_title = helper.getView(R.id.tv_item_title);
            TextView tv_item_type = helper.getView(R.id.tv_item_type);
            TextView tv_item_address = helper.getView(R.id.tv_item_address);
            TextView tv_item_time = helper.getView(R.id.tv_item_time);
            tv_item_title.setTextSize(13);
            tv_item_time.setTextSize(12);
            tv_item_type.setTextSize(12);
            tv_item_address.setTextSize(12);
            if (time.length() > 16) {
                time = time.substring(11, 16);
            }
            helper.setText(R.id.tv_item_title, name)
                    .setText(R.id.tv_item_time, time)
//                    .setText(R.id.tv_item_type, "合照人:" + detail)
                    .setText(R.id.tv_item_address, "地点:" + address);
            ImageView iv_item_image = helper.getView(R.id.iv_item_image);
            mImageUrl = item.getImgurl();
            if (!TextUtils.isEmpty(mImageUrl)) {
                String imageUrl = ImageHelper.getFirstImageUrl(mImageUrl);
                GlideUtils.loadImageViewLoading(iv_item_image, imageUrl, R.drawable.picture, R.drawable.picture);
            } else {
                helper.setImageResource(R.id.iv_item_image, R.mipmap.zanwutupian);
            }

        }
    }
}
