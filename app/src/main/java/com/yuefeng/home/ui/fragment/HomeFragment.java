package com.yuefeng.home.ui.fragment;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.common.base.BaseMvpFragment;
import com.common.utils.Constans;
import com.common.utils.TimeUtils;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.event.CarListEvent;
import com.yuefeng.home.ui.activity.WebDetailInfosActivtiy;
import com.yuefeng.home.ui.adapter.HomeMsgInfosAdapter;
import com.yuefeng.home.ui.modle.MsgDataBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

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

    @BindString(R.string.tab_main_name)
    String msg_name;
    Unbinder unbinder;
    private HomeMsgInfosAdapter adapter;
    private List<MsgDataBean> listData = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.module_fragment_home;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, rootView);
        tv_tab_name.setText(msg_name);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        tvSearchTxt.clearFocus();
        tvSearchTxt.setCursorVisible(false);
        initRecycleView();
    }

    private void initRecycleView() {
        adapter = new HomeMsgInfosAdapter(R.layout.recyclerview_item_msginfos, listData);
        recyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                showSuccessToast(listData.get(position).getCount());

                startActivity(new Intent(getActivity(), WebDetailInfosActivtiy.class));
            }
        });
        showAdapterDatasList();
    }

    /*展示数据*/
    private void showAdapterDatasList() {
        List<MsgDataBean> bean = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            MsgDataBean msgDataBean = new MsgDataBean();
            if (i == 0) {
                msgDataBean.setImageUrl(R.drawable.work);
            } else if (i == 1) {
                msgDataBean.setImageUrl(R.drawable.upgrade);
            } else if (i == 2) {
                msgDataBean.setImageUrl(R.drawable.item);
            } else {
                msgDataBean.setImageUrl(R.drawable.email);
            }
            msgDataBean.setTitle(i + "工作更新内容");
            msgDataBean.setTime(TimeUtils.getHourMinute());
            msgDataBean.setDetail(i + "更新详情：点击肌肉");
            msgDataBean.setCount(i + "");
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
