package com.yuefeng.contacts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.common.utils.StringUtils;
import com.yuefeng.commondemo.R;
import com.yuefeng.contacts.modle.contacts.ContactsBean;

import java.util.ArrayList;
import java.util.List;

/*通讯录*/
public class SelectSortBookAdapter extends BaseAdapter implements Filterable {

    private List<ContactsBean> list, copyList;
    private Context mContext;

    private final Object mLock = new Object();
    private ArrayList<ContactsBean> mOriginalValues;
    private ArrayFilter mFilter;

    public SelectSortBookAdapter(Context mContext, List<ContactsBean> list) {
        this.mContext = mContext;
        this.list = list;
    }

    public int getCount() {
        return list == null ? 0 : this.list.size();
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


    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    private class ArrayFilter extends Filter {
        //执行刷选
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();//过滤的结果
            //原始数据备份为空时，上锁，同步复制原始数据
            if (mOriginalValues == null) {
                synchronized (mLock) {
                    mOriginalValues = new ArrayList<>(list);
                }
            }
            //当首字母为空时
            if (prefix == null || prefix.length() == 0) {
                ArrayList<ContactsBean> list;
                synchronized (mLock) {//同步复制一个原始备份数据
                    list = new ArrayList<>(mOriginalValues);
                }
                results.values = list;
                results.count = list.size();//此时返回的results就是原始的数据，不进行过滤
            } else {
                String prefixString = prefix.toString().toLowerCase();//转化为小写

                ArrayList<ContactsBean> values;
                synchronized (mLock) {//同步复制一个原始备份数据
                    values = new ArrayList<>(mOriginalValues);
                }
                final int count = values.size();
                final ArrayList<ContactsBean> newValues = new ArrayList<>();

                for (int i = 0; i < count; i++) {
                    ContactsBean value = values.get(i);//从List<User>中拿到User对象
//                    final String valueText = value.toString().toLowerCase();
                    final String valueText = value.getName().toString().toLowerCase();//User对象的name属性作为过滤的参数
                    // First match against the whole, non-splitted value
                    if (valueText.startsWith(prefixString) || valueText.indexOf(prefixString.toString()) != -1) {//第一个字符是否匹配
                        newValues.add(value);//将这个item加入到数组对象中
                    } else {//处理首字符是空格
                        final String[] words = valueText.split(" ");
                        final int wordCount = words.length;

                        // Start at index 0, in case valueText starts with space(s)
                        for (int k = 0; k < wordCount; k++) {
                            if (words[k].startsWith(prefixString)) {//一旦找到匹配的就break，跳出for循环
                                newValues.add(value);
                                break;
                            }
                        }
                    }
                }
                results.values = newValues;//此时的results就是过滤后的List<User>数组
                results.count = newValues.size();
            }
            return results;
        }

        //刷选结果
        @Override
        protected void publishResults(CharSequence prefix, FilterResults results) {
            //noinspection unchecked
            list = (List<ContactsBean>) results.values;//此时，Adapter数据源就是过滤后的Results
            if (results.count > 0) {
                notifyDataSetChanged();//这个相当于从mDatas中删除了一些数据，只是数据的变化，故使用notifyDataSetChanged()
            } else {
                /**
                 * 数据容器变化 ----> notifyDataSetInValidated
                 容器中的数据变化  ---->  notifyDataSetChanged
                 */
                notifyDataSetInvalidated();//当results.count<=0时，此时数据源就是重新new出来的，说明原始的数据源已经失效了
            }
        }
    }
}

