package com.yuefeng.features.ui.fragment.Lllegal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.common.base.codereview.BaseFragment;
import com.common.utils.Constans;
import com.common.utils.TimeUtils;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.adapter.CarLllegalWorkListAdapter;
import com.yuefeng.features.event.LllegalWorkEvent;
import com.yuefeng.features.ui.activity.Lllegalwork.LllegalWorkDetailActivity;
import com.yuefeng.home.ui.modle.MsgDataBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/*人员*/
public class CarLllegalWorkListFragment extends BaseFragment {

    private static final String TAG = "tag";
    @BindView(R.id.rl_nodata)
    RelativeLayout rlNodata;
    @BindView(R.id.ll_data)
    LinearLayout llData;
    @BindView(R.id.tv_lllegal_count)
    TextView tvLllegalCount;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private List<MsgDataBean> listData = new ArrayList<>();
    private CarLllegalWorkListAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_carlllegalworklist;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this, rootView);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        tvLllegalCount.setTextSize(13);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        initRecycler();
    }

    private void initRecycler() {
        adapter = new CarLllegalWorkListAdapter(R.layout.recyclerview_item_lllegals, listData);
        recyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MsgDataBean msgDataBean = listData.get(position);
                Intent intent = new Intent();
                intent.setClass(getActivity(), LllegalWorkDetailActivity.class);
                intent.putExtra("DetailInfos", msgDataBean);
                intent.putExtra("type", "car");
                intent.putExtra("isVisible", "1");
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });

    }


    @Override
    protected void setLisenter() {

    }

    @Override
    protected void widgetClick(View v) {

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void initData() {

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void disposeLllegalWorkEvent(LllegalWorkEvent event) {
        switch (event.getWhat()) {
            case Constans.PERSONALLLEGAL_SSUCESS://展示
//                bean = (GetJobMonitotingMsgBean) event.getData();
//                if (bean != null) {
//                    showAdapterDatasList(bean);
//                }
                break;

            case Constans.JOB_ERROR:
                listData.clear();
                initRecycler();
                break;

        }
    }

    @OnClick({R.id.tv_lllegal_count, R.id.rl_nodata})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_lllegal_count:
                llData.setVisibility(View.INVISIBLE);
                rlNodata.setVisibility(View.VISIBLE);
                break;
            case R.id.rl_nodata:
                rlNodata.setVisibility(View.INVISIBLE);
                llData.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected void fetchData() {
        showAdapterDatasList();
    }

    /*展示数据*/
    @SuppressLint("SetTextI18n")
    private void showAdapterDatasList() {
        List<MsgDataBean> bean = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            MsgDataBean msgDataBean = new MsgDataBean();
            msgDataBean.setImageUrl(R.mipmap.icon_app);
            msgDataBean.setTitle("粤A4565" + (i + 1));
            msgDataBean.setTime(TimeUtils.getHourMinute());
            if (i == 0) {
                msgDataBean.setDetail("路线偏移");
            } else {
                msgDataBean.setDetail("作业超速");
            }
            msgDataBean.setCount("广州市天河区新塘大街28号祺和商贸园");
            bean.add(msgDataBean);
        }
        listData.clear();
        listData.addAll(bean);
        adapter.setNewData(listData);
        tvLllegalCount.setText("截止到" + TimeUtils.
                getHourMinute() +
                " 已有" + listData.size() + "辆车违规作业");
    }


    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
