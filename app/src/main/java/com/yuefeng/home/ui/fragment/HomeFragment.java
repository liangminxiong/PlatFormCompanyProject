package com.yuefeng.home.ui.fragment;

import android.view.View;

import com.common.base.BaseMvpFragment;
import com.yuefeng.commondemo.R;


/**
 * Created by Administrator on 2018/7/11.
 */

public class HomeFragment extends BaseMvpFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.module_fragment_home;
    }

    @Override
    protected void initView() {
//        EventBus.getDefault().register(getContext());
    }

    @Override
    protected void fetchData() {
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setLisenter() {

    }

    @Override
    protected void widgetClick(View v) {

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(getContext());
    }
}
