package com.yuefeng.features.ui.activity.Lllegalwork;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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
import com.common.view.popuwindow.PersonalListPopupWindow;
import com.common.view.popuwindow.TreesListsPopupWindow;
import com.yuefeng.cartreeList.adapter.SimpleTreeRecyclerAdapter;
import com.yuefeng.cartreeList.common.Node;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.adapter.MyLllegalPageAdapter;
import com.yuefeng.features.contract.LllegalWorkContract;
import com.yuefeng.features.event.LllegalWorkEvent;
import com.yuefeng.features.modle.carlist.CarListInfosMsgBean;
import com.yuefeng.features.presenter.LllegalWorkPresenter;
import com.yuefeng.features.ui.fragment.Lllegal.CarLllegalWorkListFragment;
import com.yuefeng.features.ui.fragment.Lllegal.PersonalLllegalWorkListFragment;
import com.yuefeng.personaltree.model.PersonalParentBean;
import com.yuefeng.utils.DatasUtils;
import com.yuefeng.utils.PersonalDatasUtils;

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
    @BindView(R.id.tv_back)
    TextView tv_back;
    @BindView(R.id.ll_problem)
    LinearLayout ll_problem;
    @BindView(R.id.tablayout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
//    @BindView(R.id.space)
//    View view;

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
    private List<PersonalParentBean> treeListData = new ArrayList<>();
    private List<Node> nodeList = new ArrayList<>();
    private PersonalListPopupWindow popupWindowTree;

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
//        view.setBackground(mActivity.getResources().getDrawable(R.drawable.title_toolbar_bg_blue));
//        StatusBarUtil.setFadeStatusBarHeight(mActivity, view);
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
//            pid = "ea9b4033ffffee0101ed1860a1febcfb";
//            userid = "eab2ffacffffffc976ce7286d4054823";
            presenter.getCarListInfos(ApiService.LOADVEHICLELIST, pid, userid, "0");
//            presenter.getCarListInfosNew(ApiService.GETVEHICLETREE, pid, userid, "0");
            getTreeListData();
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

    /*人员列表*/
    private void getTreeListData() {
        if (presenter != null) {
            String pid = PreferencesUtils.getString(this, Constans.ORGID, "");
            String userid = PreferencesUtils.getString(this, "id", "");
//            pid = "dg1954";
//            userid = "19f66fabffffffc975d0e8f475995ee6";
            presenter.getPersontree(ApiService.GETPERSONTREE, userid, pid);
        }
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

            case Constans.PERSONALLIST_SSUCESS://人员列表成功
                treeListData = (List<PersonalParentBean>) event.getData();
                if (treeListData.size() > 0) {
                    showPersonallistDatas(treeListData);
                }
                break;
            case Constans.CARLIST_ERROR://车辆列表失败

                break;
            default:
                break;

        }
    }

    /*人员列表展示数据*/
    private void showPersonallistDatas(List<PersonalParentBean> organs) {
        try {
            nodeList.clear();
            nodeList = PersonalDatasUtils.ReturnPersonalTreesDatas(organs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*展示数据*/
    private void showCarlistDatas(List<CarListInfosMsgBean> organs) {
        try {
            carDatas.clear();
            carDatas = DatasUtils.ReturnTreesDatas(organs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private int count = 0;

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        isCarOrPersonalList = position == 0;
        viewPager.setCurrentItem(position);
        if (count < 10) {
            count++;
        }
        if (count < 3) {
            if (isCarOrPersonalList) {
                EventBus.getDefault().postSticky(new LllegalWorkEvent(Constans.VECHIL_ID, ""));
            } else {
                EventBus.getDefault().postSticky(new LllegalWorkEvent(Constans.PERSONAL_ID, ""));
            }
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @OnClick({R.id.tv_title, R.id.tv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_title:
                if (isCarOrPersonalList) {
                    initCarlistPopupView();
                } else {
                    initTreeListPopupView();
                }
                break;
        }
    }

    /*车辆列表*/
    private void initCarlistPopupView() {
        if (carDatas.size() > 0) {
            carListPopupWindow = new TreesListsPopupWindow(this, carDatas, true);
            carListPopupWindow.setTitleText("车辆列表");
            carListPopupWindow.setSettingText(ResourcesUtils.getString(R.string.sure));
            carListPopupWindow.setOnItemClickListener(new TreesListsPopupWindow.OnItemClickListener() {
                @Override
                public void onGoBack(String name, String terminal, String id) {
                    if (!TextUtils.isEmpty(id)) {
                        EventBus.getDefault().postSticky(new LllegalWorkEvent(Constans.VECHIL_ID, id));
                    }
                }

                @Override
                public void onSure(String name, String terminal, String id) {
                    if (!TextUtils.isEmpty(id)) {
                        EventBus.getDefault().postSticky(new LllegalWorkEvent(Constans.VECHIL_ID, id));
                    }
                }

                @Override
                public void onSelectCar(String carNumber, String terminal, String id) {
//                    if (!TextUtils.isEmpty(id)) {
//                        EventBus.getDefault().postSticky(new LllegalWorkEvent(Constans.VECHIL_ID, id));
//                    }
                }
            });
            carListPopupWindow.showAtLocation(ll_problem, Gravity.BOTTOM | Gravity.CENTER, 0, 0);
        }
    }

    /*人员列表*/
    private void initTreeListPopupView() {
        try {
            if (nodeList.size() > 0) {
                popupWindowTree = new PersonalListPopupWindow(this, nodeList, true);
                popupWindowTree.setTitleText("选择人员");
                popupWindowTree.setSettingText("确定");
                popupWindowTree.setOnItemClickListener(new PersonalListPopupWindow.OnItemClickListener() {
                    @Override
                    public void onGoBack(String listName, String userId, String terminal) {
                        if (!TextUtils.isEmpty(userId)) {
                            EventBus.getDefault().postSticky(new LllegalWorkEvent(Constans.PERSONAL_ID, userId));
                        }
                    }

                    @Override
                    public void onSure(String listName, String userId, String terminal) {
                        if (!TextUtils.isEmpty(userId)) {
                            EventBus.getDefault().postSticky(new LllegalWorkEvent(Constans.PERSONAL_ID, userId));
                        }
                    }

                    @Override
                    public void onSelectCar(String carNumber, String userId, String terminal) {
//                        if (!TextUtils.isEmpty(userId)) {
//                            EventBus.getDefault().postSticky(new LllegalWorkEvent(Constans.PERSONAL_ID, userId));
//                        }

                    }
                });
                popupWindowTree.showAtLocation(ll_problem, Gravity.BOTTOM | Gravity.CENTER, 0, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
