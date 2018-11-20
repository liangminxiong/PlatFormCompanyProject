package com.yuefeng.cartreeList.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuefeng.cartreeList.common.Node;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.modle.PersonalinfoListBean;
import com.yuefeng.features.modle.QuestionListBean;
import com.yuefeng.features.modle.VehicleinfoListBean;

import java.util.List;

public class PersonalVechicleTreeRecyclerAdapter extends ChangeTreeRecyclerAdapter {
    private int icon_type;
    private int icon_type_single;

    public PersonalVechicleTreeRecyclerAdapter(RecyclerView mTree, Context context, List<Node> datas,
                                               int defaultExpandLevel, int iconExpand, int iconNoExpand
            , boolean isAllSelect, boolean isSetPadding) {
        super(mTree, context, datas, defaultExpandLevel, iconExpand, iconNoExpand, isAllSelect, isSetPadding);
    }

    public PersonalVechicleTreeRecyclerAdapter(RecyclerView mTree, Context context, List<Node> datas,
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
                if (mOnCheckBoxInterface != null) {
                    mOnCheckBoxInterface.onCheckBoxClick(node, position);
                }
                notifyDataSetChanged();
            }
        });


        if (node.isChecked()) {
            viewHolder.cb.setChecked(true);
        } else {
            viewHolder.cb.setChecked(false);
        }

        String getpId = (String) node.getpId();
        String id = (String) node.getId();
        String name = node.getName();

        if (getpId.equals("first") || getpId.equals("parentId")) {
            icon_type = R.drawable.list_fold;
        } else {
            if (getpId.equals("personalId")) {
                PersonalinfoListBean bean = (PersonalinfoListBean) node.getBean();
                String stateType = bean.getStateType();
                if (stateType.contains("0")) {
                    icon_type = R.drawable.list_renyuan_tingzhi;
                } else if (stateType.contains("1")) {
                    icon_type = R.drawable.list_renyuan_xingshi;
                } else if (stateType.contains("2")) {
                    icon_type = R.drawable.list_renyuan_xingshi;
                } else {
                    icon_type = R.drawable.list_renyuan_tingche;
                }
                name = bean.getName();
            } else if (getpId.equals("vehicleId")) {
                VehicleinfoListBean bean = (VehicleinfoListBean) node.getBean();
                String stateType = bean.getStateType();
                if (stateType.contains("0")) {
                    icon_type = R.drawable.list_qiche_lixian;
                } else if (stateType.contains("1")) {
                    icon_type = R.drawable.list_qiche_tingche;
                } else if (stateType.contains("2")) {
                    icon_type = R.drawable.list_qiche_xingshi;
                } else {
                    icon_type = R.drawable.list_qiche_ting10;
                }
                name = bean.getRegistrationNO();
            } else {
                QuestionListBean bean = (QuestionListBean) node.getBean();
                String stateType = bean.getState();
                name = bean.getProblem();
                if (stateType.equals("1")) {
                    icon_type = R.drawable.list_problem_ting;

                } else if (stateType.equals("2")) {
                    icon_type = R.drawable.list_problem_zaixian;
                } else if (stateType.equals("3")) {
                    icon_type = R.drawable.list_problem_ting10;
                } else {
                    icon_type = R.drawable.list_problem_lixian;
                }
            }
        }
        viewHolder.iv_next.setVisibility(View.INVISIBLE);
        viewHolder.label.setText(name);

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

    private OnCheckBoxInterface mOnCheckBoxInterface;

    public void setOnCheckBoxInterface(OnCheckBoxInterface onTreeNodeClickListener) {
        this.mOnCheckBoxInterface = onTreeNodeClickListener;
    }

    public interface OnCheckBoxInterface {
        void onCheckBoxClick(Node node, int position);
    }
}
