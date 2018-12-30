package com.yuefeng.personaltree;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuefeng.cartreeList.adapter.ChangeTreeRecyclerAdapter;
import com.yuefeng.cartreeList.common.Node;
import com.yuefeng.commondemo.R;

import java.util.List;

public class ChangePersonalTreeRecyclerAdapter extends ChangeTreeRecyclerAdapter {
    private int icon_type;

    public ChangePersonalTreeRecyclerAdapter(RecyclerView mTree, Context context, List<Node> datas,
                                             int defaultExpandLevel, int iconExpand, int iconNoExpand
            , boolean isAllSelect, boolean isSetPadding) {
        super(mTree, context, datas, defaultExpandLevel, iconExpand, iconNoExpand, isAllSelect, isSetPadding);
    }

    public ChangePersonalTreeRecyclerAdapter(RecyclerView mTree, Context context, List<Node> datas,
                                             int defaultExpandLevel, boolean isAllSelect, boolean isSetPadding) {
        super(mTree, context, datas, defaultExpandLevel, isAllSelect, isSetPadding);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHoder(View.inflate(mContext, R.layout.tree_list_rv_item, null));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final Node node, RecyclerView.ViewHolder holder, final int position) {

        final MyHoder viewHolder = (MyHoder) holder;

        viewHolder.cb.setSelected(node.isChecked());//“CheckBox”
        //todo do something
        viewHolder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//实现单选，第一种方法，十分简单， Lv Rv通用,因为它们都有notifyDataSetChanged()方法
                // 每次点击时，先将所有的selected设为false，并且将当前点击的item 设为true， 刷新整个视图
                if (isAllSelect) {
                    count++;
                    if (count > 1) {
//                        ToastUtils.showToast("只支持单选");
                    }
                    for (Node data : mAllNodes) {
                        data.setChecked(false);
                    }
                } else {
                    node.setChecked(true);
                }

                /*多选*/
                boolean checked = viewHolder.cb.isChecked();
                setChecked(node, checked);
                notifyDataSetChanged();
            }
        });

        if (node.isChecked()) {
            viewHolder.cb.setChecked(true);
        } else {
            viewHolder.cb.setChecked(false);
        }
        String getpId = (String) node.getpId();
        int icon = node.getIcon();
        String stateType = node.getStateType();
        String count = node.getCount();

        if (count.equals("0")) {
            viewHolder.iv_next.setVisibility(View.INVISIBLE);
            viewHolder.cb.setVisibility(View.VISIBLE);
            viewHolder.label.setText(node.getName());
            viewHolder.cb.setVisibility(View.VISIBLE);
//            0  离线  1 停止 2 行驶 3 等待
            if (node.getStateType().contains("0")) {
                icon_type = R.drawable.list_renyuan_tingzhi;
            } else if (node.getStateType().contains("1")) {
                icon_type = R.drawable.list_renyuan_xingshi;
            } else if (node.getStateType().contains("2")) {
                icon_type = R.drawable.list_renyuan_xingshi;
            } else {
                icon_type = R.drawable.list_renyuan_tingche;
            }


        } else {
            viewHolder.cb.setVisibility(View.VISIBLE);

            viewHolder.label.setText(node.getName() + "(" + node.getCount() + ")");
            if (getpId.equals("first")) {
                icon_type = R.drawable.list_fold;
                viewHolder.iv_next.setVisibility(View.INVISIBLE);
            } else {
                icon_type = R.drawable.list_fold;
                viewHolder.iv_next.setVisibility(View.INVISIBLE);
            }
        }
        viewHolder.icon.setImageResource(icon_type);

    }

    class MyHoder extends RecyclerView.ViewHolder {

        CheckBox cb;

        public TextView label;

        public ImageView icon, iv_next;

        MyHoder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.iv_icon_type);
            iv_next = (ImageView) itemView.findViewById(R.id.iv_image_type);
            label = (TextView) itemView.findViewById(R.id.tv_item_title);
            cb = (CheckBox) itemView.findViewById(R.id.iv_select_tree);
        }
    }
}
