package com.yuefeng.ui.base.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.common.base.codereview.BaseActivity;
import com.common.utils.AppManager;
import com.common.utils.StatusBarUtil;
import com.common.view.other.CustomCOnstantsView;
import com.yuefeng.commondemo.R;


/**
 * Created  on 2018-03-13.
 * author:seven
 * email:seven2016s@163.com
 */

public class ViewTestActivity extends BaseActivity {

    private CustomCOnstantsView customCOnstantsView;
    private View space;

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
    protected void initView(Bundle savedInstanceState) {
//        initStatusBar();
        space = findViewById(R.id.main_action_bar_space);
        customCOnstantsView = findViewById(R.id.custom_view);
        customCOnstantsView.setImageView(R.drawable.ic_launcher_background);
        customCOnstantsView.setToolbarTitle("帅帅帅帅帅");
        customCOnstantsView.setToolbarTitleColor(Color.BLACK);
//        StatusBarUtil.setTranslates(ViewTestActivity.this,true);

        customCOnstantsView.getCustomfinshPagerOnclickListener(new CustomCOnstantsView.CustomfinshPagerOnclickListener() {
            @Override
            public void customfinshPager() {
                AppManager.getAppManager().finishActivity(ViewTestActivity.this);
            }

            @Override
            public void layoutCollapsed() {
                customCOnstantsView.setToolbarCollapsingColor(Color.RED);
                StatusBarUtil.setTranslateByColor(ViewTestActivity.this, Color.RED);
//                space.setBackgroundResource(R.color.red);
//                StatusBarUtil.setTranslate(ViewTestActivity.this, true, space);
            }

            @Override
            public void layoutExpanded() {
                StatusBarUtil.setTranslate(ViewTestActivity.this,true);
            }
        });

//        customCOnstantsView.addMatchViews();
    }


    @Override
    protected int getContentViewResId() {
        return R.layout.activity_view;
    }
}
