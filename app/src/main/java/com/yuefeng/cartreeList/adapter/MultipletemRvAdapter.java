package com.yuefeng.cartreeList.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.common.utils.StringUtils;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.modle.EventQuestionMsgBean;

import java.util.List;

/*树形列表适配器*/
public class MultipletemRvAdapter extends BaseQuickAdapter<EventQuestionMsgBean, BaseViewHolder> {

    private String name;
    private int icon_type;
    private ImageView mIcIcon;

    public MultipletemRvAdapter(int layoutResId, @Nullable List<EventQuestionMsgBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EventQuestionMsgBean item) {
        if (item != null) {
            int position = helper.getAdapterPosition();
            mIcIcon = (ImageView) helper.getView(R.id.iv_icon_type);
            name = StringUtils.isEntryStrNull(item.getProblem());
            if (position == 0) {
                icon_type = R.drawable.list_fold;
                helper.setVisible(R.id.iv_image_type, false);
            } else {
                mIcIcon.setPadding(45, 0, 0, 0);
                helper.setVisible(R.id.iv_image_type, true);
                icon_type = R.drawable.bg_white_tree;
            }
            helper.setText(R.id.tv_item_title, name)
                    .setImageResource(R.id.iv_icon_type, icon_type);
        }

    }
}
