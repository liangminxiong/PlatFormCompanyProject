package com.yuefeng.ui;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.common.base.codereview.BaseActivity;
import com.common.utils.Constans;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.modle.EventQuestionMsgBean;
import com.yuefeng.login_splash.event.SignInEvent;
import com.yuefeng.ui.view.MultipletemPopupWindow;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DemoTestActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_title_setting)
    TextView mTvTitleSetting;
    @BindView(R.id.ll_parent)
    ConstraintLayout llParent;


    @Override
    protected int getContentViewResId() {
        return R.layout.activity_test;
    }

    @SuppressLint({"ClickableViewAccessibility"})
    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initUI();
    }

    @SuppressLint("SetTextI18n")
    private void initUI() {
        mTvTitle.setText("测试Demo");
        mTvTitleSetting.setBackgroundResource(R.drawable.list);
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


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void disposeSignInEvent(SignInEvent event) {
        switch (event.getWhat()) {
            case Constans.LOGIN:
//                String data = (String) event.getData();
//                showSuccessToast(data);
                break;
            default:
//                String dataa = (String) event.getData();
//                showErrorToast(dataa);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @OnClick({R.id.tv_title_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_title_setting:
                initPopuWindows();
                break;
        }
    }

    private void initPopuWindows() {
        List<EventQuestionMsgBean> listData = new ArrayList<>();
        MultipletemPopupWindow popupWindow = new MultipletemPopupWindow(this, listData, true, true);
        popupWindow.setTitleText("选择车辆");
        popupWindow.setSettingText(getString(R.string.submit));
        popupWindow.setOnItemClickListener(new MultipletemPopupWindow.OnItemClickListener() {
            @Override
            public void onGoBack(String name, String terminal, String id, boolean isGetDatas) {

            }

            @Override
            public void onSure(String name, String terminal, String id, boolean isGetDatas) {

            }

            @Override
            public void onSelectCar(String carNumber, String terminal, String id, boolean isGetDatas) {

            }
        });
        popupWindow.showAtLocation(llParent, Gravity.BOTTOM | Gravity.CENTER, 0, 0);
    }


}
