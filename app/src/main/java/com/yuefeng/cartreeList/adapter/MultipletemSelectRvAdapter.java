package com.yuefeng.cartreeList.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.common.utils.StringUtils;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.modle.EventQuestionMsgBean;

import java.util.List;

/*树形列表适配器*/
public class MultipletemSelectRvAdapter extends BaseQuickAdapter<EventQuestionMsgBean, BaseViewHolder> {

    private String name;
    private int icon_type;
    private int icon_type_selected;
    private ImageView mIcIcon;
    private String state;
    private String mPid;

    private int mPosition = -1;//保存当前选中的position 重点！
    private boolean isSingle;

    public void setSelection(int position) {
        this.mPosition = position;
        notifyDataSetChanged();
    }


    public MultipletemSelectRvAdapter(int layoutResId, @Nullable List<EventQuestionMsgBean> data) {
        super(layoutResId, data);
    }

    public MultipletemSelectRvAdapter(int layoutResId, @Nullable List<EventQuestionMsgBean> data,boolean isSingle) {
        super(layoutResId, data);
        this.isSingle = isSingle;
    }

    @Override
    protected void convert(BaseViewHolder helper, EventQuestionMsgBean item) {

        if (item != null) {
            int position = helper.getAdapterPosition();
            mIcIcon = (ImageView) helper.getView(R.id.iv_icon_type);
            name = StringUtils.isEntryStrNull(item.getProblem());
            state = StringUtils.isEntryStrNull(item.getState());
            mPid = item.getPid();

            singleOrAllSelect(helper, position);

            if (position == 0) {
                helper.setVisible(R.id.iv_select_tree, false);
                if (TextUtils.isEmpty(mPid)) {
                    icon_type = R.drawable.list_fold;
                } else {
                    icon_type = iconType(state);
                }
                mIcIcon.setPadding(15, 0, 0, 0);
            } else {
                if (!TextUtils.isEmpty(mPid)) {
                    mIcIcon.setPadding(45, 0, 0, 0);
                }
                icon_type = iconType(state);
            }

            helper.setText(R.id.tv_item_title, name)
                    .addOnClickListener(R.id.iv_select_tree)
                    .setImageResource(R.id.iv_icon_type, icon_type)
                    .setImageResource(R.id.iv_select_tree, icon_type_selected);
        }
    }


    private void singleOrAllSelect(BaseViewHolder helper, int position) {
        if (isSingle) {
            if (mPosition == position) {
                icon_type_selected = R.drawable.list_select_sel;
                helper.itemView.setSelected(true);
            } else {
                icon_type_selected = R.drawable.list_select_nor;
                helper.itemView.setSelected(false);
            }
        }
    }

    private int iconType(String state) {
        if (Integer.valueOf(state) % 2 == 0) {
            icon_type = R.drawable.list_qiche_tingche;
        } else if (Integer.valueOf(state) % 3 == 0) {
            icon_type = R.drawable.list_qiche_ting10;
        } else {
            icon_type = R.drawable.list_qiche_xingshi;
        }
        return icon_type;
    }
}
