package com.yuefeng.features.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.common.utils.LogUtils;
import com.yuefeng.commondemo.R;
import com.yuefeng.home.ui.modle.MsgDataBean;

import java.util.List;

public class CarHistoryLllegalWorkListAdapter extends BaseItemDraggableAdapter<MsgDataBean, BaseViewHolder> {

    private int imageUrl;
    private String title;
    private String time;
    private String detail;
    private String count;

    public CarHistoryLllegalWorkListAdapter(int layoutResId, @Nullable List<MsgDataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MsgDataBean item) {
        int position = helper.getLayoutPosition();
        LogUtils.d("position == " + position);
        if (position == 0) {
            helper.setBackgroundRes(R.id.ll_layout_item, R.drawable.shape_cricle_bg_gray_top);
        }else {
            helper.setBackgroundRes(R.id.ll_layout_item, R.drawable.shape_cricle_bg_gray_top0);
        }
        if (item != null) {
            imageUrl = item.getImageUrl();
            title = item.getTitle();
            time = item.getTime();
            detail = item.getDetail();
            count = item.getCount();
            title = TextUtils.isEmpty(title) ? " " : title;
            time = TextUtils.isEmpty(time) ? " " : time;
            detail = TextUtils.isEmpty(detail) ? " " : detail;
            count = TextUtils.isEmpty(count) ? " " : count;
            TextView tv_item_title = helper.getView(R.id.tv_item_title);
            TextView tv_item_type = helper.getView(R.id.tv_item_type);
            TextView tv_item_address = helper.getView(R.id.tv_item_address);
            TextView tv_item_time = helper.getView(R.id.tv_item_time);
            tv_item_title.setTextSize(13);
            tv_item_time.setTextSize(12);
            tv_item_type.setTextSize(12);
            tv_item_address.setTextSize(12);
            helper.setText(R.id.tv_item_title, title)
                    .setText(R.id.tv_item_time, time)
                    .setText(R.id.tv_item_type, "违规类型:" + detail)
                    .setText(R.id.tv_item_address, "违规地点:" + count);
//            ImageView iv_item_image = helper.getView(R.id.iv_item_image);
//            if (!TextUtils.isEmpty(imageUrl)) {
//                GlideUtils.loadImageViewCircle(iv_item_image, imageUrl, R.drawable.picture, R.drawable.picture);
//            } else {
//            }
            helper.setImageResource(R.id.iv_item_image, imageUrl);
        }
    }
}
