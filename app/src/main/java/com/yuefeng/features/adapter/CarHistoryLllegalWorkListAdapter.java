package com.yuefeng.features.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.modle.LllegalworMsgBean;

import java.util.List;

public class CarHistoryLllegalWorkListAdapter extends BaseItemDraggableAdapter<LllegalworMsgBean, BaseViewHolder> {

    private String name;
    private String time;
    private String detail;
    private String address;
    private int imageId;
    private String type;

    public CarHistoryLllegalWorkListAdapter(int layoutResId, @Nullable List<LllegalworMsgBean> data, String type) {
        super(layoutResId, data);
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, LllegalworMsgBean item) {
        int position = helper.getLayoutPosition();
        if (position == 0) {
            helper.setBackgroundRes(R.id.ll_layout_item, R.drawable.shape_cricle_bg_gray_top);
        } else {
            helper.setBackgroundRes(R.id.ll_layout_item, R.drawable.shape_cricle_bg_gray_top0);
        }
        if (item != null && helper != null) {
            name = item.getName();
            time = item.getTime();
            detail = item.getContents();
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
                    .setText(R.id.tv_item_type, "违规类型:" + detail)
                    .setText(R.id.tv_item_address, "违规地点:" + address);
//            ImageView iv_item_image = helper.getView(R.id.iv_item_image);
//            if (!TextUtils.isEmpty(imageUrl)) {
//                GlideUtils.loadImageViewCircle(iv_item_image, imageUrl, R.drawable.picture, R.drawable.picture);
//            } else {
//            }
            if (type.equals("car")) {
                imageId = R.drawable.truck;
            } else {
                imageId = R.drawable.staff;
            }
            helper.setImageResource(R.id.iv_item_image, imageId);
        }
    }
}
