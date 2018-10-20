package com.yuefeng.features.ui.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.base.codereview.BaseActivity;
import com.common.event.CommonEvent;
import com.common.network.ApiService;
import com.common.utils.Constans;
import com.common.utils.PreferencesUtils;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.adapter.MyViewPagerAdapter;
import com.yuefeng.features.contract.QualityGetCountContract;
import com.yuefeng.features.modle.GetQuestionCountMsgBean;
import com.yuefeng.features.presenter.QualityGetCountPresenter;
import com.yuefeng.features.ui.fragment.AllProblemFragment;
import com.yuefeng.features.ui.fragment.PendingProblemFragment;
import com.yuefeng.features.ui.fragment.ProcessingProblemFragment;
import com.yuefeng.features.ui.fragment.ToClosedProblemFragment;
import com.yuefeng.ui.base.fragment.TabItemInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

/*问题巡检*/

public class QualityInspectionActivity extends BaseActivity implements
        TabLayout.OnTabSelectedListener, QualityGetCountContract.View {

    @BindView(R.id.iv_back)
    RelativeLayout iv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindColor(R.color.titel_color)
    int coloeWhite;
    @BindColor(R.color.titel_color)
    int coloeGray;
    @BindView(R.id.tablayout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    private List<TabItemInfo> tabItemInfos;
    //TabLayout标签
    private MyViewPagerAdapter viewPagerAdapter;
    List<String> stringList = new ArrayList<>();
    private TabItemInfo tabItemInfoA;
    private TabItemInfo tabItemInfoB;
    private TabItemInfo tabItemInfoC;
    private TabItemInfo tabItemInfoD;
    private QualityGetCountPresenter presenter;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_qualityinspection;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        ButterKnife.bind(this);
        presenter = new QualityGetCountPresenter(this, this);

//        View view = findViewById(R.id.space);
//
//        view.setBackground(mActivity.getResources().getDrawable(R.drawable.title_toolbar_bg_blue));
//        StatusBarUtil.setFadeStatusBarHeight(mActivity, view);
        tv_title.setText("问题处理");
        initViewPager();
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }

    @SuppressLint("InflateParams")
    private void initViewPager() {
        //设置TabLayout点击事件
        tabLayout.addOnTabSelectedListener(this);
        tabItemInfos = new ArrayList<>();
        tabItemInfoA = new TabItemInfo(new AllProblemFragment(), R.drawable.tab_button_selector, R.string.all_txt);
        tabItemInfos.add(tabItemInfoA);
        tabItemInfoB = new TabItemInfo(new PendingProblemFragment(), R.drawable.tab_button_selector, R.string.pending_txt);
        tabItemInfos.add(tabItemInfoB);
        tabItemInfoC = new TabItemInfo(new ProcessingProblemFragment(), R.drawable.tab_button_selector, R.string.processing_txt);
        tabItemInfos.add(tabItemInfoC);
        tabItemInfoD = new TabItemInfo(new ToClosedProblemFragment(), R.drawable.tab_button_selector, R.string.toclosed_txt);
        tabItemInfos.add(tabItemInfoD);

        viewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), tabItemInfos, QualityInspectionActivity.this);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(tabItemInfos.size());
        tabLayout.setupWithViewPager(viewPager);

    }

    private void initTabViewCount(List<String> stringList) {
        if (stringList.size() < 4) {
            return;
        }
        tabItemInfoA.setTabCount(stringList.get(0));
        tabItemInfoB.setTabCount(stringList.get(1));
        tabItemInfoC.setTabCount(stringList.get(2));
        tabItemInfoD.setTabCount(stringList.get(3));
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                if (tab.getCustomView() != null) {
                    TextView countView = (TextView) tab.getCustomView().findViewById(R.id.tab_count);
                    countView.setText(stringList.get(i));
                } else {
                    tab.setCustomView(viewPagerAdapter.getTabView(i));
                }
            }
        }
    }

    @Override
    protected void setLisenter() {

    }

    @Override
    protected void widgetClick(View v) {

    }

    @Override
    public void initData() {
        getQuestingCount(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void disposeCommonEvent(CommonEvent event) {
        switch (event.getWhat()) {
            case Constans.COUNT_SUCESS://展示
                GetQuestionCountMsgBean bean = (GetQuestionCountMsgBean) event.getData();
                if (bean != null) {
                    showCountData(bean);
                }
                break;

            case Constans.COUNT_AGAIN_SUCESS:
                getQuestingCount(false);
                break;

            case Constans.GETCOUNT_ERROR:
                initListdatas("0", "0", "0", "0");
                break;

        }

    }

    /*获取数量*/
    private void getQuestingCount(boolean isFirst) {
        String pid = PreferencesUtils.getString(this, "orgId", "");
        String userid = PreferencesUtils.getString(this, "id", "");

        presenter.getQuestionCount(ApiService.GETQUESTIONCOUNT, pid, userid,isFirst);
    }


    /**/
    private void showCountData(GetQuestionCountMsgBean msg) {
        String allcount = msg.getAllcount();
        String waitcount = msg.getWaitcount();
        String doingcount = msg.getDoingcount();
        String waitclosecount = msg.getWaitclosecount();
        initListdatas(allcount, waitcount, doingcount, waitclosecount);
    }

    private void initListdatas(String allcount, String waitcount, String doingcount, String waitclosecount) {
        stringList.clear();
        stringList.add(allcount);
        stringList.add(waitcount);
        stringList.add(doingcount);
        stringList.add(waitclosecount);

        initTabViewCount(stringList);
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (viewPagerAdapter != null) {
            viewPagerAdapter.destroy();
            viewPagerAdapter = null;
        }
        tabItemInfos = null;
        tabLayout = null;
    }
}

