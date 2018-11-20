package com.yuefeng.home.ui.fragment;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.common.base.BaseMvpFragment;
import com.common.utils.Constans;
import com.common.utils.TimeUtils;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.event.CarListEvent;
import com.yuefeng.home.ui.activity.MsgListDetailInfosActivtiy;
import com.yuefeng.home.ui.adapter.HomeMsgInfosAdapter;
import com.yuefeng.home.ui.modle.MsgDataBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Created by Administrator on 2018/7/11.
 */

public class HomeFragment extends BaseMvpFragment {
    @BindView(R.id.tv_tab_name)
    TextView tv_tab_name;
    @BindView(R.id.tv_search_txt)
    TextInputEditText tvSearchTxt;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.rl_search)
    RelativeLayout rlSearch;

    @BindString(R.string.tab_main_name)
    String msg_name;
    Unbinder unbinder;
    private HomeMsgInfosAdapter adapter;
    private List<MsgDataBean> listData = new ArrayList<>();
    private int tempPosition = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.module_fragment_home;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, rootView);
        tv_tab_name.setText(msg_name);
        tvSearchTxt.clearFocus();
        tvSearchTxt.setCursorVisible(false);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        initRecycleView();
        rlSearch.setVisibility(View.VISIBLE);
    }

    private void initRecycleView() {

        adapter = new HomeMsgInfosAdapter(R.layout.recyclerview_item_msginfos, listData);
        recyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position == 0) {
                    tempPosition = 1;
                } else if (position == 1) {
                    tempPosition = 2;
                } else {
                    tempPosition = 3;
                }
                Intent intent = new Intent();
                intent.setClass(Objects.requireNonNull(getActivity()), MsgListDetailInfosActivtiy.class);
                intent.putExtra("msgList", (Serializable) listData);
                intent.putExtra("tempPosition", tempPosition);
                startActivity(intent);
            }
        });
        showAdapterDatasList();
    }

    /*展示数据*/
    private void showAdapterDatasList() {
        String name = "";
        String detail = "";
        List<MsgDataBean> bean = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            MsgDataBean msgDataBean = new MsgDataBean();
            if (i == 0) {
                name = "侨银环保科技股份有限公司";
                detail = "[审批]今天还有一个审批单待你处理，请尽快处理";
                msgDataBean.setImageUrl(R.drawable.work);
            } else if (i == 1) {
                name = "升级提醒";
                detail = "1.0.2版本新功能介绍";
                msgDataBean.setImageUrl(R.drawable.upgrade);
            } else {
                name = "项目通知:池州一体化项目进展情况";
                detail = "[执行]今天还有2个任务待你处理，请尽快完成";
                msgDataBean.setImageUrl(R.drawable.item);
            }
            msgDataBean.setName(name);
            msgDataBean.setTitle(name);
            msgDataBean.setTime(TimeUtils.getHourMinute());
            msgDataBean.setDetail(detail);
            msgDataBean.setCount(1 + "");
            bean.add(msgDataBean);
        }
        listData.clear();
        listData.addAll(bean);
        adapter.setNewData(listData);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void disposeCarListEvent(CarListEvent event) {
        switch (event.getWhat()) {
            case Constans.TRACK_SSUCESS://展示
                break;

            default:
//                showSuccessToast("获取数据失败，请重试");
                break;

        }

    }

    @OnClick(R.id.tv_search_txt)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_search_txt:
                tvSearchTxt.setCursorVisible(true);
                break;
        }
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
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
