package com.yuefeng.contacts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.utils.StringUtils;
import com.yuefeng.commondemo.R;
import com.yuefeng.contacts.modle.contacts.ContactsBean;

import java.util.ArrayList;
import java.util.List;

/*通讯录*/
public class SortBookAdapter extends BaseAdapter {

    private List<ContactsBean> list = null;
    private List<String> listSelect = new ArrayList<>();
    private Context mContext;

    // 用来控制CheckBox的选中状况

    public SortBookAdapter(Context mContext, List<ContactsBean> list) {
        this.mContext = mContext;
        this.list = list;
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder viewHolder;
        final ContactsBean user = list.get(position);

        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_sort, null);
            viewHolder.relat = (RelativeLayout) view.findViewById(R.id.relat);
            viewHolder.name = (TextView) view.findViewById(R.id.tv_item_name);
            viewHolder.logo = (TextView) view.findViewById(R.id.iv_item_logo);
            viewHolder.cb_tiem = (CheckBox) view.findViewById(R.id.cb_tiem);
            viewHolder.catalog = (TextView) view.findViewById(R.id.catalog);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        String name = list.get(position).getName();
        //根据position获取首字母作为目录catalog
        String catalog = list.get(position).getFirstLetter();
        viewHolder.logo.setText(StringUtils.returnUserTwoLenght(name));
        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(catalog)) {
            viewHolder.catalog.setVisibility(View.VISIBLE);
            viewHolder.catalog.setText(user.getFirstLetter().toUpperCase());
        } else {
            viewHolder.catalog.setVisibility(View.GONE);
        }

        viewHolder.name.setText(name);
        // 监听checkBox并根据原来的状态来设置新的状态
        viewHolder.relat.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (list.get(position).isChecked()) {
                    viewHolder.cb_tiem.setBackgroundResource(R.drawable.list_select_nor);
                    list.get(position).setChecked(false);
                } else {
                    viewHolder.cb_tiem.setBackgroundResource(R.drawable.list_select_sel);
                    list.get(position).setChecked(true);
                }
                notifyDataSetChanged();
            }
        });

        // 根据isSelected来设置checkbox的选中状况
        viewHolder.cb_tiem.setChecked(list.get(position).isChecked());

        return view;
    }

    public  List<String> getIsSelected() {
        listSelect.clear();
        int size = list.size();
        if (size > 0) {
            for (ContactsBean bean : list) {
                if (bean.isChecked()) {
                    listSelect.add(bean.getUserId());
                }
            }
        }


        return listSelect;
    }


    class ViewHolder {
        RelativeLayout relat;
        TextView catalog;
        TextView name;
        TextView logo;
        CheckBox cb_tiem;
    }

    /**
     * 获取catalog首次出现位置
     */
    public int getPositionForSection(String catalog) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getFirstLetter();
            if (catalog.equalsIgnoreCase(sortStr)) {
                return i;
            }
        }
        return -1;
    }

}

