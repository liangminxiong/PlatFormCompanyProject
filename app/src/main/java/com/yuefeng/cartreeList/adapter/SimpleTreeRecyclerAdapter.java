package com.yuefeng.cartreeList.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yuefeng.cartreeList.common.Node;
import com.yuefeng.commondemo.R;
import com.yuefeng.utils.StringUtils;

import java.util.List;

public class SimpleTreeRecyclerAdapter extends TreeRecyclerAdapter {
    private static final String TAG = "tag";
    private int icon_car_image;


    public SimpleTreeRecyclerAdapter(RecyclerView mTree, Context context, List<Node> datas,
                                     int defaultExpandLevel, int iconExpand, int iconNoExpand, boolean isAllSelect) {
        super(mTree, context, datas, defaultExpandLevel, iconExpand, iconNoExpand, isAllSelect);
    }

    public SimpleTreeRecyclerAdapter(RecyclerView mTree, Context context, List<Node> datas,
                                     int defaultExpandLevel, boolean isAllSelect) {
        super(mTree, context, datas, defaultExpandLevel, isAllSelect);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHoder(View.inflate(mContext, R.layout.list_item_change, null));
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
                }
//                node.setChecked(true);

                /*多选*/
                boolean checked = viewHolder.cb.isChecked();
                setChecked(node, checked);
//                notifyDataSetChanged();
            }
        });

        if (node.isChecked()) {
            viewHolder.cb.setChecked(true);
        } else {
            viewHolder.cb.setChecked(false);
        }

        if (node.getIcon() == -1) {
            viewHolder.cb.setVisibility(View.VISIBLE);
            viewHolder.icon_state.setVisibility(View.VISIBLE);
            if (node.getStateType().contains("0")) {
                icon_car_image = R.drawable.icon_car_offline;
            } else if (node.getStateType().contains("1")) {
                icon_car_image = R.drawable.icon_car_online;
            } else if (node.getStateType().contains("2")) {
                icon_car_image = R.drawable.icon_car_green;
            } else {
                icon_car_image = R.drawable.icon_car_red;
            }
            viewHolder.icon_state.setImageResource(icon_car_image);
        } else {
            viewHolder.cb.setVisibility(View.VISIBLE);
            viewHolder.icon_state.setVisibility(View.VISIBLE);
        }

        if (node.getIcon() == -1) {
            viewHolder.icon.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.icon.setVisibility(View.VISIBLE);
            viewHolder.icon.setImageResource(node.getIcon());
        }
        if (node.getCount().equals("0")) {
            viewHolder.rel.setVisibility(View.GONE);
            viewHolder.label.setText(node.getName());

            viewHolder.tv_gpstime.setText("定位时间：" + StringUtils.returnStrTxt(node.getGt()));
            viewHolder.tv_acc.setText("设备ACC:" + node.getObd());
            viewHolder.tv_speed.setText("速度：" + node.getSpeed() + "km/h");
//            viewHolder.tv_address.setText("地址：" + node.getAddress());
        } else {
            viewHolder.rel.setVisibility(View.GONE);
            viewHolder.label.setText(node.getName() + "(" + node.getCount() + ")");
        }
    }

    class MyHoder extends RecyclerView.ViewHolder {

        CheckBox cb;

        public TextView label;
        public TextView tv_gpstime;
        public TextView tv_acc;
        public TextView tv_address;
        public TextView tv_speed;

        public ImageView icon, icon_state;

        public RelativeLayout rel;

        MyHoder(View itemView) {
            super(itemView);

            cb = (CheckBox) itemView
                    .findViewById(R.id.cb_select_tree);
            label = (TextView) itemView
                    .findViewById(R.id.id_treenode_label);
            icon = (ImageView) itemView.findViewById(R.id.icon);
            icon_state = (ImageView) itemView.findViewById(R.id.icon_state);

            rel = (RelativeLayout) itemView.findViewById(R.id.rel);
            tv_gpstime = (TextView) itemView.findViewById(R.id.tv_gpstime);
            tv_acc = (TextView) itemView.findViewById(R.id.tv_acc);
            tv_address = (TextView) itemView.findViewById(R.id.tv_address);
            tv_speed = (TextView) itemView.findViewById(R.id.tv_speed);
        }
    }
}
