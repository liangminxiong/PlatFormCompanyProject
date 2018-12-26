package com.yuefeng.contacts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.utils.StringUtils;
import com.yuefeng.commondemo.R;
import com.yuefeng.contacts.modle.contacts.ContactsBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/*通讯录*/
public class SortBookAdapter extends BaseAdapter {

    private List<ContactsBean> list,copyList;
    private List<String> listSelect = new ArrayList<>();
    private Context mContext;

    private NameFilter mNameFilter;
    private List<ContactsBean> mFilteredArrayList;
    public static String searchContent;

    // 用来控制CheckBox的选中状况
    // 用来控制CheckBox的选中状况
    private static HashMap<Integer, Boolean> isSelected;

    public SortBookAdapter(Context mContext, List<ContactsBean> list) {
        this.mContext = mContext;
        this.list = list;


        isSelected = new HashMap<Integer, Boolean>();
        // 初始化数据
        initDate();

        mFilteredArrayList = new ArrayList<>();
        //copyList是暂存原来所用的数据，当筛选内容为空时，显示所有数据，并且必须new 一个对象，
        //而不能copyList=arrayList,这样的话当arrayList改变时copyList也就改变了
        copyList = new ArrayList<>();
        copyList.addAll(list);
    }

    // 初始化isSelected的数据
    private void initDate() {
        for (int i = 0; i < list.size(); i++) {
            getIsSelected().put(i, false);
        }
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

        // 根据isSelected来设置checkbox的选中状况
        viewHolder.cb_tiem.setChecked(getIsSelected().get(position));

        return view;
    }

    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        SortBookAdapter.isSelected = isSelected;
    }

    public class ViewHolder {
        RelativeLayout relat;
        TextView catalog;
        TextView name;
        TextView logo;
        public CheckBox cb_tiem;
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

    public Filter getFilter() {
        if (mNameFilter == null) {
            mNameFilter = new SortBookAdapter.NameFilter();
        }
        return mNameFilter;
    }

    // 异步过滤数据，避免数据多耗时长堵塞主线程
    class NameFilter extends Filter {
        // 执行筛选
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            searchContent = charSequence.toString();
            FilterResults filterResults = new FilterResults();
            if (charSequence == null|| charSequence.length() == 0) {
                mFilteredArrayList = copyList;
            } else {
                mFilteredArrayList.clear();
                for (Iterator<ContactsBean> iterator = copyList.iterator(); iterator
                        .hasNext();) {
                    String name = iterator.next().getName();

                    if (name.contains(charSequence)) {
                        mFilteredArrayList.add(iterator.next());
                    }
                }
            }
            filterResults.values = mFilteredArrayList;
            return filterResults;
        }

        // 筛选结果
        @Override
        protected void publishResults(CharSequence arg0, FilterResults results) {
            list = (List<ContactsBean>) results.values;
            if (list.size() > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }

        }
    }

}

