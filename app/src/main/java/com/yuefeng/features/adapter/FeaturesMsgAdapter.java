package com.yuefeng.features.adapter;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.common.utils.StringUtils;
import com.common.utils.TimeUtils;
import com.yuefeng.commondemo.R;
import com.yuefeng.home.ui.modle.AnnouncementDataMsgBean;

import java.util.List;

/*消息*/
public class FeaturesMsgAdapter extends BaseItemDraggableAdapter<AnnouncementDataMsgBean, BaseViewHolder> {


    private String imageUrl;
    private String title;
    private String time;
    private String detail;
    //    private String count;
    private String name;
    private int icon;

    public FeaturesMsgAdapter(int layoutResId, @Nullable List<AnnouncementDataMsgBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AnnouncementDataMsgBean item) {
        /*
         * iv_item_image
         * tv_item_title
         * tv_item_time
         * tv_item_detail
         * tv_item_count
         * */
        if (item != null && helper != null) {
            imageUrl = item.getGenre();
            name = item.getOrganname();
            title = item.getSubject();
            time = item.getIssuedate();
            detail = item.getContent();
            title = StringUtils.isEntryStrWu(title);
            time = TimeUtils.formatHourMin(time);
//            count = TextUtils.isEmpty(count) ? " " : count;
            if (imageUrl.equals("1")) {//公告
                icon = R.drawable.work;
            } else if (imageUrl.equals("2")) {//信息
                icon = R.drawable.xiangmutongzhi;
            } else {//更新
                icon = R.drawable.upgrade;
            }


            TextView tv_item_title = helper.getView(R.id.tv_item_title);
            TextView tv_item_time = helper.getView(R.id.tv_item_time);
            TextView tv_item_detail = helper.getView(R.id.tv_item_detail);
            tv_item_title.setTextSize(13);
            tv_item_time.setTextSize(12);
            tv_item_detail.setTextSize(12);
            helper.setText(R.id.tv_item_title, title)
                    .setVisible(R.id.iv_item_unread, true)
                    .setText(R.id.tv_item_time, time)
                    .setText(R.id.tv_item_detail, detail)
                    .setImageResource(R.id.iv_item_image, icon);
        }
    }
}
