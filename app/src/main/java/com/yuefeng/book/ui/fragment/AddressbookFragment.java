package com.yuefeng.book.ui.fragment;

import android.view.View;

import com.common.base.BaseMvpFragment;
import com.common.event.CommonEvent;
import com.common.utils.Constans;
import com.yuefeng.commondemo.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * Created by Administrator on 2018/7/11.
 * 通讯录
 */

public class AddressbookFragment extends BaseMvpFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.module_fragment_home;
    }


    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initData() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void disposeCommonEvent(CommonEvent commonEvent) {
        switch (commonEvent.getWhat()) {
            case Constans.COMMON:
                break;
        }
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
        EventBus.getDefault().unregister(this);
    }
}
