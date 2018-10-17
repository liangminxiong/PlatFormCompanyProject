package com.yuefeng.features.ui.activity.Lllegalwork;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.base.codereview.BaseActivity;
import com.common.network.ApiService;
import com.common.utils.Constans;
import com.common.utils.PreferencesUtils;
import com.common.utils.ResourcesUtils;
import com.common.utils.StatusBarUtil;
import com.common.view.popuwindow.TreesListsPopupWindow;
import com.yuefeng.cartreeList.adapter.SimpleTreeRecyclerAdapter;
import com.yuefeng.cartreeList.common.Node;
import com.yuefeng.cartreeList.common.OnTreeNodeClickListener;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.adapter.MyLllegalPageAdapter;
import com.yuefeng.features.contract.LllegalWorkContract;
import com.yuefeng.features.event.LllegalWorkEvent;
import com.yuefeng.features.modle.carlist.CarListInfosMsgBean;
import com.yuefeng.features.presenter.LllegalWorkPresenter;
import com.yuefeng.features.ui.fragment.Lllegal.CarLllegalWorkListFragment;
import com.yuefeng.features.ui.fragment.Lllegal.PersonalLllegalWorkListFragment;
import com.yuefeng.utils.DatasUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/*违规作业*/
public class LllegalWorkActivity extends BaseActivity implements LllegalWorkContract.View, TabLayout.OnTabSelectedListener {

    private static final String TAG = "tag";
    @BindView(R.id.tv_title)
    TextView tv_title_setting;
    @BindView(R.id.ll_problem)
    LinearLayout ll_problem;
    @BindView(R.id.tablayout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.space)
    View view;

    private List<CarListInfosMsgBean> carListData = new ArrayList<>();
    private List<Node> carDatas = new ArrayList<>();
    private LllegalWorkPresenter presenter;
    private TreesListsPopupWindow carListPopupWindow;
    private SimpleTreeRecyclerAdapter carlistAdapter;
    private String carNumber;
    private String terminal;

    //TabLayout标签
    private String[] titles = new String[]{"车辆违规", "人员违规"};
    private List<Fragment> fragments = new ArrayList<>();
    private MyLllegalPageAdapter viewPagerAdapter;

    private boolean isCarOrPersonalList = true;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_lllegalwork;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        ButterKnife.bind(this);
        presenter = new LllegalWorkPresenter(this, this);
        view.setBackground(mActivity.getResources().getDrawable(R.drawable.title_toolbar_bg_blue));
        StatusBarUtil.setFadeStatusBarHeight(mActivity, view);
        initTabLayout();
        getCarList();
        isCarOrPersonalList = true;
    }

    private void initTabLayout() {
        //设置TabLayout标签的显示方式
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        //循环注入标签
        for (String tab : titles) {
            tabLayout.addTab(tabLayout.newTab().setText(tab));
        }
        //设置TabLayout点击事件
        tabLayout.addOnTabSelectedListener(this);

        fragments.add(new CarLllegalWorkListFragment());
        fragments.add(new PersonalLllegalWorkListFragment());
        viewPagerAdapter = new MyLllegalPageAdapter(getSupportFragmentManager(), titles, fragments);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    /*车辆列表*/
    private void getCarList() {
        if (presenter != null) {
            String pid = PreferencesUtils.getString(this, "orgId", "");
            String userid = PreferencesUtils.getString(this, "id", "");
            presenter.getCarListInfos(ApiService.LOADVEHICLELIST, pid, userid, "0");
        }
    }

    @Override
    public void initData() {
    }

    @Override
    protected void setLisenter() {

    }

    @Override
    protected void widgetClick(View v) {

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void disposeLllegalWorkEvent(LllegalWorkEvent event) {
        switch (event.getWhat()) {
            case Constans.CARLIST_SSUCESS:
                carListData = (List<CarListInfosMsgBean>) event.getData();
                if (carListData.size() > 0) {
                    showCarlistDatas(carListData);
                } else {
                    showSuccessToast("旗下无车辆");
                }
                break;
            default:
                break;

        }
    }

    /*展示数据*/
    private void showCarlistDatas(List<CarListInfosMsgBean> organs) {
        carDatas.clear();
        carDatas = DatasUtils.ReturnTreesDatas(organs);
    }

    /*车辆列表*/
    private void initCarlistPopupView() {
        carListPopupWindow = new TreesListsPopupWindow(this);
        carListPopupWindow.setTitleText("车辆列表");
        carListPopupWindow.setSettingText(ResourcesUtils.getString(R.string.sure));

        if (carDatas.size() > 0) {
            carListPopupWindow.recyclerview.setLayoutManager(new LinearLayoutManager(this));
            if (carlistAdapter == null) {
                carlistAdapter = new SimpleTreeRecyclerAdapter(carListPopupWindow.recyclerview, this,
                        carDatas, 1, R.drawable.tree_open, R.drawable.tree_close, true);
            } else {
                carlistAdapter.notifyDataSetChanged();
            }
            carListPopupWindow.recyclerview.setAdapter(carlistAdapter);
        }
        carlistAdapter.notifyDataSetChanged();
        carlistAdapter.setOnTreeNodeClickListener(new OnTreeNodeClickListener() {
            @Override
            public void onClick(Node node, int position) {
                showSelectItemDatas();
            }

        });

        carListPopupWindow.setOnItemClickListener(new TreesListsPopupWindow.OnItemClickListener() {
            @Override
            public void onGoBack() {
                carListPopupWindow.dismiss();
            }


            @Override
            public void onSure() {
                showSelectItemDatas();
                carListPopupWindow.dismiss();
            }
        });

        carListPopupWindow.showAtLocation(ll_problem, Gravity.BOTTOM | Gravity.CENTER, 0, 0);
    }

    /*点击车*/
    @SuppressLint("SetTextI18n")
    private void showSelectItemDatas() {
        if (carlistAdapter == null) {
            return;
        }
        final List<Node> allNodes = carlistAdapter.getAllNodes();
        for (int i = 0; i < allNodes.size(); i++) {
            if (allNodes.get(i).isChecked()) {
                carNumber = allNodes.get(i).getName();
                terminal = allNodes.get(i).getTerminalNO();
            }
        }
        if (!TextUtils.isEmpty(terminal)) {
            if (carListPopupWindow != null) {
                carListPopupWindow.dismiss();
            }
        }
//        showSuccessToast(carNumber);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        isCarOrPersonalList = position == 0;
        viewPager.setCurrentItem(position);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @OnClick(R.id.tv_title)
    public void onViewClicked() {
        if (isCarOrPersonalList) {
            initCarlistPopupView();
        } else {
            showSuccessToast("正在开发...");
        }
    }
}
