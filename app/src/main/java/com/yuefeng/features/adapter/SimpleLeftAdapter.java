package com.yuefeng.features.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.common.utils.ResourcesUtils;
import com.yuefeng.commondemo.R;

import java.util.List;

/**
 * 二级联动的 listview的 适配器
 */
public class SimpleLeftAdapter extends BaseAdapter {

    private Context context;
    private List<String> listDatas;
    //        一级联动选中的位置
    private int selectedPosition = -1;

    public SimpleLeftAdapter(@NonNull Context context, @NonNull List<String> list) {
        this.context = context;
        this.listDatas = list;
    }

    @Override
    public int getCount() {
        return listDatas == null ? 0 : listDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return listDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView =ResourcesUtils.inflate(R.layout.simple_list_item);
            holder = new ViewHolder();

            holder.nameTV = (TextView) convertView.findViewById(R.id.tv_item_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //选中和没选中时，设置不同的颜色
//        if (position == selectedPosition) {
//            convertView.setBackgroundColor(ResourcesUtils.getColor(R.color.list_divider));
//        } else {
//            convertView.setBackgroundColor(ResourcesUtils.getColor(R.color.white));
//        }

        holder.nameTV.setText(listDatas.get(position));

        return convertView;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    private class ViewHolder {
        TextView nameTV;
    }
}
