package com.yuefeng.home.ui.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yuefeng.commondemo.R;
import com.yuefeng.home.ui.modle.MsgDataBean;

import java.util.List;

/*消息*/
public class HomeMsgInfosAdapter extends BaseItemDraggableAdapter<MsgDataBean, BaseViewHolder> {


    private int imageUrl;
    private String title;
    private String time;
    private String detail;
    private String count;

    public HomeMsgInfosAdapter(int layoutResId, @Nullable List<MsgDataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MsgDataBean item) {
        /*
         * iv_item_image
         * tv_item_title
         * tv_item_time
         * tv_item_detail
         * tv_item_count
         * */
        if (item != null && helper != null) {
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
            TextView tv_item_time = helper.getView(R.id.tv_item_time);
            TextView tv_item_detail = helper.getView(R.id.tv_item_detail);
            tv_item_title.setTextSize(13);
            tv_item_time.setTextSize(12);
            tv_item_detail.setTextSize(12);
            helper.setText(R.id.tv_item_title, title)
                    .setText(R.id.tv_item_time, time)
                    .setText(R.id.tv_item_detail, detail)
                    .setVisible(R.id.tv_item_count, true)
                    .setText(R.id.tv_item_count, count);
//            ImageView iv_item_image = helper.getView(R.id.iv_item_image);
//            if (!TextUtils.isEmpty(imageUrl)) {
//                GlideUtils.loadImageViewCircle(iv_item_image, imageUrl, R.drawable.picture, R.drawable.picture);
//            } else {
//            }
            helper.setImageResource(R.id.iv_item_image, imageUrl);
        }
    }
}
