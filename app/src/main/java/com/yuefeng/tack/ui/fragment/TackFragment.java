package com.yuefeng.tack.ui.fragment;

import android.view.View;
import android.widget.TextView;

import com.common.base.BaseMvpFragment;
import com.common.event.CommonEvent;
import com.common.utils.Constans;
import com.yuefeng.commondemo.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * Created by Administrator on 2018/7/11.
 * 任务
 */

public class TackFragment extends BaseMvpFragment {
    private TextView tv_txt;

    @Override
    protected int getLayoutId() {
        return R.layout.module_fragment_home;
    }


    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        tv_txt = rootView.findViewById(R.id.tv_txt);
    }

    @Override
    protected void initData() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void disposeCommonEvent(CommonEvent commonEvent) {
        switch (commonEvent.getWhat()) {
            case Constans.COMMON:
                String data = (String)commonEvent.getData();
                showSuccessToast( data);
                tv_txt.setText(data);
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
