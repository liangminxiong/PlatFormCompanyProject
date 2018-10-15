package com.yuefeng.features.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.common.base.codereview.BaseActivity;
import com.common.network.ApiService;
import com.common.utils.Constans;
import com.common.utils.LogUtils;
import com.common.utils.StatusBarUtil;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.adapter.QualityInspectionDetailAdapter;
import com.yuefeng.features.contract.QualityDetailContract;
import com.yuefeng.features.event.QualityDetailEvent;
import com.yuefeng.features.modle.EventdetailMsgBean;
import com.yuefeng.features.modle.GetEventdetailMsgBean;
import com.yuefeng.features.presenter.QualityDetailPresenter;
import com.yuefeng.photo.adapter.GvAdapter;
import com.yuefeng.photo.bean.ImageInfo;
import com.yuefeng.photo.view.MyGridView2;
import com.yuefeng.photo.view.PicShowDialog;
import com.yuefeng.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

/*问题详情*/

public class QualityInspectionDetailActivity extends BaseActivity implements QualityDetailContract.View {

    @BindView(R.id.iv_back)
    RelativeLayout iv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindColor(R.color.titel_color)
    int coloeWhite;
    @BindColor(R.color.titel_color)
    int coloeGray;
    @BindView(R.id.rl_type)
    RelativeLayout rl_type;

    @BindView(R.id.scrollview)
    ScrollView scrollview;
    @BindView(R.id.tv_problem_type)
    TextView tv_problem_type;
    @BindView(R.id.tv_single_num)
    TextView tv_single_num;
    @BindView(R.id.tv_problem_time)
    TextView tv_problem_time;

    @BindView(R.id.tv_problem_reporter)
    TextView tv_problem_reporter;
    @BindView(R.id.tv_reporter_phone)
    TextView tv_reporter_phone;

    @BindView(R.id.tv_problem_what)
    TextView tv_problem_what;
    @BindView(R.id.gridview)
    MyGridView2 gridview;
    @BindView(R.id.tv_problem_address)
    TextView tv_problem_address;
    @BindView(R.id.tv_problem_class)
    TextView tv_problem_class;

    @BindView(R.id.tv_problem_usetime)
    TextView tv_problem_usetime;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private List<EventdetailMsgBean> listData = new ArrayList<>();
    private QualityInspectionDetailAdapter detailAdapter;
    private String problemid;
    private String state;
    private String name;
    private List<ImageInfo> images;
    private GvAdapter adapter;
    private int colorInt;
    private QualityDetailPresenter presenter;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_qualityinspectiondetail;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        presenter = new QualityDetailPresenter(this, this);

        tv_title.setText(R.string.problem_detail_txt);
        View view = findViewById(R.id.space);
        gridview = findViewById(R.id.gridview);

        view.setBackground(mActivity.getResources().getDrawable(R.drawable.title_toolbar_bg_blue));
        StatusBarUtil.setFadeStatusBarHeight(mActivity, view);
//        PROBLEMID
        Bundle bundle = getIntent().getExtras();
        problemid = (String) bundle.get("PROBLEMID");
        name = (String) bundle.get("NAME");
        tv_problem_reporter.setText(name);

        scrollview.post(new Runnable() {
            @Override
            public void run() {
                scrollview.fullScroll(ScrollView.FOCUS_UP);
            }
        });
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        initRecycler();
        LogUtils.d("=============" + problemid);
        getEventDetailInfos(problemid);
    }

    /*获取详情*/
    private void getEventDetailInfos(String problemid) {
        presenter.GetEventdetail(ApiService.GETEVENTDETAIL, problemid);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void disposeQualityDetailEvent(QualityDetailEvent event) {
        switch (event.getWhat()) {
            case Constans.DETAIL_SSUCESS://
                recyclerview.setVisibility(View.VISIBLE);
                List<GetEventdetailMsgBean> bean = (List<GetEventdetailMsgBean>) event.getData();
                if (bean != null) {
                    showEventDetailInfos(bean);
                }
                break;
            default:
                showErrorToast("获取问题详情失败!");
                break;
        }

    }


    /*显示*/
    private void showEventDetailInfos(List<GetEventdetailMsgBean> msg) {
        if (msg.size() != 0) {
            GetEventdetailMsgBean msgBean = msg.get(0);
            showText(msgBean);
            showListText(msgBean.getDetail());
        }

    }

    /*流程*/
    private void showListText(List<EventdetailMsgBean> detail) {
        if (detail.size() != 0) {
            listData.clear();
            listData.addAll(detail);
            detailAdapter.setNewData(listData);
        }

    }

    @SuppressLint("SetTextI18n")
    private void showText(GetEventdetailMsgBean msgBean) {
        state = msgBean.getState();

        if (state.contains("1")) {
            state = getString(R.string.pending_txt);//待处理
            colorInt = QualityInspectionDetailActivity.this.getResources().getColor(R.color.red_hand_color);
            tv_problem_type.setTextColor(getResources().getColor(R.color.red));
        } else if (state.contains("2")) {
            tv_problem_type.setTextColor(getResources().getColor(R.color.red));
            state = getString(R.string.processing_txt);//处理中
            colorInt = QualityInspectionDetailActivity.this.getResources().getColor(R.color.red_hand_color);
        } else if (state.contains("3")) {
            state = getString(R.string.toclosed_txt);//待关闭
            colorInt = QualityInspectionDetailActivity.this.getResources().getColor(R.color.list_divider);
        } else {
            state = getString(R.string.closed_txt);//已关闭
            colorInt = QualityInspectionDetailActivity.this.getResources().getColor(R.color.list_divider);
        }
        rl_type.setBackgroundColor(colorInt);
        tv_problem_type.setText(state);
        tv_single_num.setText("问题单号:" + StringUtils.returnStrTxt(msgBean.getId()));
        String time = StringUtils.returnStrTime(msgBean.getUploadtime());
        tv_problem_time.setText(time);

//        tv_problem_reporter.setText(StringUtils.returnStrTxt(msgBean.getPid()));
//        tv_reporter_phone.setText("138282554555");

        tv_problem_what.setText(StringUtils.returnStrTxt(msgBean.getProblem()));
        tv_problem_address.setText(StringUtils.returnStrTxt(msgBean.getAddress()));
        tv_problem_class.setText(StringUtils.returnStrTxt(msgBean.getPid()));

        String timeUse = StringUtils.returnStrTxt(msgBean.getTime());
        if (!TextUtils.isEmpty(timeUse)) {
            timeUse = "用时" + timeUse;
        }
        tv_problem_usetime.setText(timeUse);

        String imgUrl = StringUtils.returnStrTxt(msgBean.getImgurl());
        showImgUrl(imgUrl);
    }

    private void showImgUrl(String imgUrl) {

        images = new ArrayList<>();
        String[] split = imgUrl.split(",");
        for (int i = 0; i < split.length; i++) {
            ImageInfo imageInfo = new ImageInfo(split[i], 200, 200);
            images.add(imageInfo);
        }
        if (adapter == null) {
            adapter = new GvAdapter(this, images);
            gridview.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        if (images.size() > 0) {
            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    PicShowDialog dialog = new PicShowDialog(QualityInspectionDetailActivity.this, images, position);
                    dialog.show();
                }
            });
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

    private void initRecycler() {

        detailAdapter = new QualityInspectionDetailAdapter(R.layout.recycler_item_quadetail, listData);
        recyclerview.setAdapter(detailAdapter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
