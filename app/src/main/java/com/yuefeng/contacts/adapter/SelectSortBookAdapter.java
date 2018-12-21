package com.yuefeng.contacts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.common.utils.StringUtils;
import com.yuefeng.commondemo.R;
import com.yuefeng.contacts.modle.contacts.ContactsBean;

import java.util.List;

/*通讯录*/
public class SelectSortBookAdapter extends BaseAdapter {

    private List<ContactsBean> list = null;
    private Context mContext;


    public SelectSortBookAdapter(Context mContext, List<ContactsBean> list) {
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
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;
        final ContactsBean user = list.get(position);

        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_sort_nocb, null);
            viewHolder.name = (TextView) view.findViewById(R.id.tv_item_name);
            viewHolder.logo = (TextView) view.findViewById(R.id.iv_item_logo);
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

        return view;
    }


    class ViewHolder {
        TextView catalog;
        TextView name;
        TextView logo;
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

