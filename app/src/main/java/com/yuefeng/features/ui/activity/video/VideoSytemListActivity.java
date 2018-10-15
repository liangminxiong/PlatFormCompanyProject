package com.yuefeng.features.ui.activity.video;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.base.codereview.BaseActivity;
import com.common.utils.Constans;
import com.common.utils.StatusBarUtil;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.contract.VideoListContract;
import com.yuefeng.features.event.VideoListEvent;
import com.yuefeng.features.modle.video.ChangeVideoEquipmentDataBean;
import com.yuefeng.features.presenter.VideoListPresenter;
import com.yuefeng.videotreesList.Node;
import com.yuefeng.videotreesList.OnTreeNodeClickListener;
import com.yuefeng.videotreesList.adapter.SimpleTreeRecyclerAdapter;
import com.yuefeng.utils.ChangeDatasUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/*视频列表*/
public class VideoSytemListActivity extends BaseActivity implements VideoListContract.View {

    private static final String TAG = "tag";
    @BindView(R.id.iv_back)
    RelativeLayout iv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.ll_problem)
    LinearLayout ll_problem;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private VideoListPresenter presenter;
    private List<Node> mDatas = new ArrayList<>();
    private SimpleTreeRecyclerAdapter mAdapter;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_videosytemlist;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        ButterKnife.bind(this);
        presenter = new VideoListPresenter(this, this);

        View view = findViewById(R.id.space);

        view.setBackground(mActivity.getResources().getDrawable(R.drawable.title_toolbar_bg_blue));
        StatusBarUtil.setFadeStatusBarHeight(mActivity, view);
        tv_title.setText(R.string.video_list);
        initRecycler();

        getVideoList();
    }

    /*获取*/
    private void getVideoList() {
        if (presenter != null) {
            String pid = "yun";
            String type = "0";
            presenter.getVideoList(pid, type);
        }
    }

    /*设置一个空的数据*/
    private void initRecycler() {
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SimpleTreeRecyclerAdapter(recyclerview, this,
                mDatas, 0, true);
        recyclerview.setAdapter(mAdapter);
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


    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void disposeVideoListEvent(VideoListEvent event) {
        switch (event.getWhat()) {
            case Constans.VIDEO_SSUCESS:
                List<ChangeVideoEquipmentDataBean> beanList = (List<ChangeVideoEquipmentDataBean>) event.getData();
                if (beanList.size() > 0) {
                    initRVdatas(beanList);
                }
                break;
            default:
                showErrorToast("处理失败，请重试");
                break;
        }
    }

    private void initRVdatas(List<ChangeVideoEquipmentDataBean> beanList) {
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
//        //第一个参数  RecyclerView
//        //第二个参数  上下文
//        //第三个参数  数据集
//        //第四个参数  默认展开层级数 0为不展开
//        //第五个参数  展开的图标
//        //第六个参数  闭合的图标
        mDatas = ChangeDatasUtils.ReturnTreesDatas(beanList);

        mAdapter = new SimpleTreeRecyclerAdapter(recyclerview, this,
                mDatas, 0, R.drawable.tree_open, R.drawable.tree_close, true);
//
        recyclerview.setAdapter(mAdapter);

        mAdapter.setOnTreeNodeClickListener(new OnTreeNodeClickListener() {
            @Override
            public void onClick(Node node, int position) {
                showSelectItemDatas();
            }

        });
    }

    private void showSelectItemDatas() {
        String tenNum = "";
        if (mAdapter == null) {
            return;
        }
        final List<Node> allNodes = mAdapter.getAllNodes();
        for (int i = 0; i < allNodes.size(); i++) {
            if (allNodes.get(i).isChecked()) {
                tenNum = allNodes.get(i).getCount();
            }
        }
        if (!TextUtils.isEmpty(tenNum)) {
            Intent intent = new Intent();
            intent.setClass(VideoSytemListActivity.this, VideoCameraActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("tp", tenNum);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
