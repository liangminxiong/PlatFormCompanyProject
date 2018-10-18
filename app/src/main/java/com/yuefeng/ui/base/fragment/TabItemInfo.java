package com.yuefeng.ui.base.fragment;

import android.support.v4.app.Fragment;

import java.io.Serializable;

/**
 * Created by seven
 * on 2018/4/6
 * email:seven2016s@163.com
 */

public class TabItemInfo implements Serializable {
    public Fragment fragment;
    public int iconResId;
    public int nameResId;
    private String tabCount;

    public TabItemInfo(Fragment fragment, int iconResId, int nameResId) {
        this.fragment = fragment;
        this.iconResId = iconResId;
        this.nameResId = nameResId;
    }

    public TabItemInfo(Fragment fragment, int iconResId) {
        this.fragment = fragment;
        this.iconResId = iconResId;
    }

    public String getTabCount() {
        return tabCount;
    }

    public void setTabCount(String tabCount) {
        this.tabCount = tabCount;
    }
}
