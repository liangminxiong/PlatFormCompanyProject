package com.yuefeng.home.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.common.utils.StringUtils;
import com.common.utils.TimeUtils;
import com.yuefeng.commondemo.R;
import com.yuefeng.home.modle.AnnouncementDataMsgBean;

import java.util.List;

/*消息详情*/
public class AnnouncementListsInfosAdapter extends BaseQuickAdapter<AnnouncementDataMsgBean, BaseViewHolder> {


    private String title;
    private String time;
    private String content;
    private String name;
    private String mIsread;

    public AnnouncementListsInfosAdapter(int layoutResId, @Nullable List<AnnouncementDataMsgBean> data, Context context) {
        super(layoutResId, data);
        this.mContext = context;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    protected void convert(BaseViewHolder helper, AnnouncementDataMsgBean item) {
        /**
         * id : 4ee2fe120a00000b7df3d9460547ec50
         * organname : 淮北项目2
         * issuedate : 2018-11-27 00:00:00.0
         * subject : 11
         * content : 11
         */
        if (item != null && helper != null) {
            name = item.getOrgName();
            title = item.getSubject();
            time = item.getIssuedate();
            content = item.getContent();
            title = StringUtils.isEntryStrWu(title);
            time = TimeUtils.formatHourMin(time);
            mIsread = item.getIsread();
            if (mIsread.equals("0")) {
                helper.setVisible(R.id.iv_item_isread, true);
            }
            content = StringUtils.isEntryStrWu(content);
            name = StringUtils.isEntryStrWu(name);
            helper.setText(R.id.tv_item_time, time)
                    .setText(R.id.tv_item_name, name)
                    .setText(R.id.tv_item_title, title)
                    .setText(R.id.tv_item_content, "公告内容: " + content)
//                    .addOnClickListener(R.id.tv_item_name)
//                    .addOnClickListener(R.id.tv_item_content)
//                    .addOnClickListener(R.id.tv_item_detail);
                    .addOnClickListener(R.id.rl_item);
//            ImageView iv_item_image = helper.getView(R.id.iv_item_image);
//            if (!TextUtils.isEmpty(imageUrl)) {
//                GlideUtils.loadImageViewCircle(iv_item_image, imageUrl, R.drawable.picture, R.drawable.picture);
//            } else {
//            }
            helper.setImageResource(R.id.iv_user_logo, R.drawable.work);
        }
    }
}
