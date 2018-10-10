package com.yuefeng.treesList.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuefeng.commondemo.R;
import com.yuefeng.treesList.Node;

import java.util.List;


/**
 * Created by zhangke on 2017-1-14.
 */
public class SimpleTreeRecyclerAdapter extends TreeRecyclerAdapter {
    private static final String TAG = "tag";
    private List<Node> datas;

    public SimpleTreeRecyclerAdapter(RecyclerView mTree, Context context, List<Node> datas,
                                     int defaultExpandLevel, int iconExpand, int iconNoExpand, boolean isAllSelect) {
        super(mTree, context, datas, defaultExpandLevel, iconExpand, iconNoExpand, isAllSelect);
    }

    public SimpleTreeRecyclerAdapter(RecyclerView mTree, Context context,
                                     List<Node> datas, int defaultExpandLevel, boolean isAllSelect) {
        super(mTree, context, datas, defaultExpandLevel,isAllSelect);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHoder(View.inflate(mContext, R.layout.list_item, null));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final Node node, RecyclerView.ViewHolder holder, int position) {

        final MyHoder viewHolder = (MyHoder) holder;
        //todo do something
        viewHolder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                setChecked(node, viewHolder.cb.isChecked());
                if (isAllSelect) {
                    count++;
                    if (count > 1) {
//                        Global.showToast("只支持单选");
                    }
                    for (Node data : mAllNodes) {
                        data.setChecked(false);
                    }
                }
//                node.setChecked(true);

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
            count = 0;
        }

        if (node.getIcon() == -1) {
            viewHolder.icon.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.icon.setVisibility(View.VISIBLE);
            viewHolder.icon.setImageResource(node.getIcon());
        }

        if (TextUtils.isEmpty(node.getCount())) {
            viewHolder.label.setText(node.getName() + "  ( " + node.getIsOnline() + " )");
            viewHolder.icon_state.setVisibility(View.GONE);
        } else {
            viewHolder.icon_state.setVisibility(View.VISIBLE);
            viewHolder.icon_state.setImageResource(R.drawable.video);
            viewHolder.label.setText(node.getName());
        }
    }

    class MyHoder extends RecyclerView.ViewHolder {

        CheckBox cb;

        public TextView label;

        public ImageView icon;
        public ImageView icon_state;

        MyHoder(View itemView) {
            super(itemView);

            cb = (CheckBox) itemView
                    .findViewById(R.id.cb_select_tree);
            label = (TextView) itemView.findViewById(R.id.id_treenode_label);
            icon = (ImageView) itemView.findViewById(R.id.icon);
            icon_state = (ImageView) itemView.findViewById(R.id.icon_state);

        }
    }
}
