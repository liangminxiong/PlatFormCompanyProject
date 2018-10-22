package com.yuefeng.features.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuefeng.commondemo.R;
import com.yuefeng.ui.base.fragment.TabItemInfo;

import java.util.List;

public class MyJobViewPagerAdapter extends FragmentPagerAdapter {
    private List<TabItemInfo> tabItemInfos;
    private Context mContext;
    public TextView tabCount;

    public MyJobViewPagerAdapter(FragmentManager fm, List<TabItemInfo> tabItemInfos, Context context) {
        super(fm);
        this.tabItemInfos = tabItemInfos;
        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        return tabItemInfos == null ? null : tabItemInfos.get(position).fragment;
    }

    @Override
    public int getCount() {
        return tabItemInfos == null ? 0 : tabItemInfos.size();
    }

    public void destroy() {
        if (tabItemInfos != null) {
            tabItemInfos.clear();
            tabItemInfos = null;
        }
    }

    @SuppressLint("SetTextI18n")
    public View getTabView(int position) {
        TabItemInfo tabItemInfo = tabItemInfos.get(position);
        View view = LayoutInflater.from(mContext).inflate(R.layout.tab_wait_for_job, null);
        ImageView tabIcon = view.findViewById(R.id.tab_icon);
        TextView tabName = view.findViewById(R.id.tab_name);
//        tabCount = view.findViewById(R.id.tab_count);
//        tabCount.setText("(" + tabItemInfo.getTabCount() + ")");
        tabIcon.setImageResource(tabItemInfo.iconResId);
//        tabName.setText(tabItemInfo.nameResId);
        tabName.setText(tabItemInfo.getTabCount());
        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
