package com.yuefeng.home.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.common.utils.StringUtils;
import com.common.utils.TimeUtils;
import com.yuefeng.commondemo.R;
import com.yuefeng.home.ui.modle.MsgListDataBean;

import java.util.List;

/*消息详情*/
public class MsgListsInfosAdapter extends BaseQuickAdapter<MsgListDataBean, BaseViewHolder> {


    private String imageUrl;
    private String title;
    private String time;
    private String content;
    private String name;
    private String reviewPersonel;

    public MsgListsInfosAdapter(int layoutResId, @Nullable List<MsgListDataBean> data, Context context) {
        super(layoutResId, data);
        this.mContext = context;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    protected void convert(BaseViewHolder helper, MsgListDataBean item) {
        /*
         * state : 0
         * id : 39757dbd0a00001f561956441c00ac97
         * reviewcontent : 不合格
         * reviewdate : 2018-11-22 11:27:40
         * pid : dg1168
         * reviewpersonel : 陈伟
         * type : CONTENT
         * reviewtitle : 【车审】粤ADV861(驳回)
         * imageurls :
         * reviewid : 11af05590a00001f20a1326282df5581
         * org : 侨银环保科技股份有限公司
         * */
        if (item != null && helper != null) {
            imageUrl = item.getImageurls();
            name = item.getOrg();
            title = item.getReviewtitle();
            time = item.getReviewdate();
            content = item.getReviewcontent();
            title = StringUtils.isEntryStrWu(title);
            reviewPersonel = item.getReviewpersonel();
            reviewPersonel = StringUtils.isEntryStrWu(reviewPersonel);
            time = TimeUtils.formatHourMin(time);


            content = StringUtils.isEntryStrWu(content);
            name = StringUtils.isEntryStrWu(name);
            helper.setText(R.id.tv_item_time, time)
                    .setText(R.id.tv_item_name, name)
                    .setText(R.id.tv_item_title, title)
                    .setText(R.id.tv_item_content, "回复内容: " + content)
//                    .addOnClickListener(R.id.tv_item_name)
//                    .addOnClickListener(R.id.tv_item_content)
//                    .addOnClickListener(R.id.tv_item_detail);
                    .addOnClickListener(R.id.rl_item);
//            ImageView iv_item_image = helper.getView(R.id.iv_item_image);
//            if (!TextUtils.isEmpty(imageUrl)) {
//                GlideUtils.loadImageViewCircle(iv_item_image, imageUrl, R.drawable.picture, R.drawable.picture);
//            } else {
//            }
            helper.setImageResource(R.id.iv_user_logo, R.drawable.item);
        }
    }
}
