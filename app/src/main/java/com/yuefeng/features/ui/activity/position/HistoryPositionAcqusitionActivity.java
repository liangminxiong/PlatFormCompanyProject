package com.yuefeng.features.ui.activity.position;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.common.base.codereview.BaseActivity;
import com.common.utils.Constans;
import com.common.utils.LogUtils;
import com.common.utils.TimeUtils;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.adapter.HistoryPositionAdapter;
import com.yuefeng.features.event.PositionAcquisitionEvent;
import com.yuefeng.features.modle.LllegalworMsgBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/*信息采集历史*/
public class HistoryPositionAcqusitionActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.swf_layout)
    SwipeRefreshLayout swfLayout;
    private HistoryPositionAdapter adapter;
    private boolean isRefresh = false;//刷新
    private static int CURPAGE = 1;
    private static int PAGE_COUNT = 0;

    private List<LllegalworMsgBean> listData = new ArrayList<>();

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_historypositionacqusition;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        ButterKnife.bind(this);

        initUI();
    }

    private void initUI() {
        tvTitle.setText("历史采集");
        initRecycler();
        initSwfLayout();
    }

    private void initRecycler() {
        listData.clear();
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HistoryPositionAdapter(R.layout.recyclerview_item_lllegals, listData);
        recyclerview.setAdapter(adapter);
        adapter.setAutoLoadMoreSize(1);
        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                return true;
            }
        });
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                LogUtils.d("onLoadMoreRequested00 " + CURPAGE + " ++ " + PAGE_COUNT);
                recyclerview.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (CURPAGE <= PAGE_COUNT) {
                            CURPAGE++;
                            LogUtils.d("onLoadMoreRequested " + CURPAGE + " ++ " + PAGE_COUNT);
//                    homePresenter.getMoreHomeData(CURPAGE);
                            initRecyclerDatas(2);//更新数据
                            adapter.loadMoreComplete();
                        } else {
                            adapter.loadMoreEnd(true);
                        }
                    }
                }, 1000);
            }
        }, recyclerview);
        adapter.disableLoadMoreIfNotFullPage();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                showSuccessToast(listData.get(position).getName());
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void disposePositionAcquisitionEvent(PositionAcquisitionEvent event) {
        switch (event.getWhat()) {
            case Constans.MSGCOLECTION_SSUCESS:
                showSuccessDialog("上传成功，是否退出当前界面?");
                break;
            case Constans.MSGCOLECTION_ERROR://上传失败

                break;
            case Constans.GETCAIJI_SSUCESS://获采集类型成功

                break;
            case Constans.GETCAIJI_ERROR://获采集类型失败
//                showSuccessToast("发布失败");
                break;
            default:

                break;
        }
    }

    private void initRecyclerDatas(int size) {
        List<LllegalworMsgBean> list = new ArrayList<>();
        list.clear();
        for (int i = 0; i < size; i++) {
            LllegalworMsgBean bean = new LllegalworMsgBean();
            bean.setName("历史== " + i);
            bean.setTime(TimeUtils.getCurrentTime2());
            bean.setContents("测试==" + i);
            bean.setAddress("祺和商贸园" + i);
            list.add(bean);
        }

        if (!isRefresh) {
            listData.addAll(list);
            adapter.setNewData(listData);
            isRefresh = true;
            adapter.setEnableLoadMore(true);
        } else {
            if (list.size() != 0) {
                adapter.addData(list);
            } else {
                adapter.loadMoreEnd(true);
            }
        }

        PAGE_COUNT = 50;

        swfLayout.setRefreshing(false);//结束
    }


    private void initSwfLayout() {
        swfLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                assert adapter != null;
                initRecyclerDatas(2);
                adapter.setEnableLoadMore(false);
            }
        });
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
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
