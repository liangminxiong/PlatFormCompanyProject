package com.yuefeng.features.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.common.base.codereview.BaseActivity;
import com.common.event.CommonEvent;
import com.common.network.ApiService;
import com.common.utils.Constans;
import com.common.utils.PreferencesUtils;
import com.common.utils.TimeUtils;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.adapter.QualityInspectionDetailAdapter;
import com.yuefeng.features.contract.QualityDetailContract;
import com.yuefeng.features.event.AllProblemEvent;
import com.yuefeng.features.event.PendingEvent;
import com.yuefeng.features.event.ProcessingEvent;
import com.yuefeng.features.event.QualityDetailEvent;
import com.yuefeng.features.modle.EventdetailMsgBean;
import com.yuefeng.features.modle.GetEventdetailMsgBean;
import com.yuefeng.features.presenter.QualityDetailPresenter;
import com.yuefeng.photo.utils.ImageHelper;
import com.yuefeng.photo.view.MyGridView2;
import com.yuefeng.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    @BindView(R.id.tv_itme_year)
    TextView tv_itme_year;

    @BindView(R.id.tv_problem_usetime)
    TextView tv_problem_usetime;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.iv_item_claim)
    ImageView ivItemClaim;
    @BindView(R.id.iv_item_forward)
    ImageView ivItemForward;
    @BindView(R.id.ll_item_two)
    LinearLayout llItemTwo;
    @BindView(R.id.iv_common)
    ImageView ivCommon;
    @BindView(R.id.ll_item_one)
    LinearLayout llItemOne;

    private List<EventdetailMsgBean> listData = new ArrayList<>();
    private QualityInspectionDetailAdapter detailAdapter;
    private String problemid;
    private String state;
    private String name;
    private int colorInt;
    private QualityDetailPresenter presenter;
    private String type;
    private String orgId;
    private String userId;

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
        gridview = findViewById(R.id.gridview);

//        View view = findViewById(R.id.space);
//        view.setBackground(mActivity.getResources().getDrawable(R.drawable.title_toolbar_bg_blue));
//        StatusBarUtil.setFadeStatusBarHeight(mActivity, view);
//        PROBLEMID
        getIntentDatas();

        scrollview.post(new Runnable() {
            @Override
            public void run() {
                scrollview.fullScroll(ScrollView.FOCUS_UP);
            }
        });
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        initRecycler();
        getEventDetailInfos(problemid);
    }

    private void getIntentDatas() {
        Bundle bundle = getIntent().getExtras();
        problemid = (String) bundle.get("PROBLEMID");
        name = (String) bundle.get("NAME");
        type = (String) bundle.get("STATE");
        String noreply = (String) bundle.get("NOREPLY");
        tv_problem_reporter.setText(name);
        assert noreply != null;
        if (noreply.equals("No")) {
            llItemOne.setVisibility(View.GONE);
            llItemTwo.setVisibility(View.GONE);
        } else {
            if (type.equals("1")) {
                llItemOne.setVisibility(View.GONE);
                llItemTwo.setVisibility(View.VISIBLE);
                ivItemClaim.setImageResource(R.drawable.xq_zhuanfa02);
                ivItemForward.setImageResource(R.drawable.xq_renling02);
            } else if (type.equals("2")) {
                ivCommon.setImageResource(R.drawable.xq_finish02);
            } else if (type.equals("3")) {
                ivCommon.setImageResource(R.drawable.xq_close02);
            } else {
                llItemOne.setVisibility(View.GONE);
                llItemTwo.setVisibility(View.GONE);
            }
        }
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
            case Constans.CLAIM_SUCESS:
                fowardOther();
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

        showProblemState(state);

        tv_single_num.setText("问题单号:" + StringUtils.returnStrTxt(msgBean.getId()));
        String time = StringUtils.returnStrTime(msgBean.getUploadtime());
        tv_problem_time.setText(time);

        tv_problem_what.setText(StringUtils.returnStrTxt(msgBean.getProblem()));
        tv_problem_address.setText(StringUtils.returnStrTxt(msgBean.getAddress()));
        tv_problem_class.setText(StringUtils.returnStrTxt(msgBean.getPid()));

        String timeUse = StringUtils.returnStrTxt(msgBean.getTime());
        if (!TextUtils.isEmpty(timeUse)) {
            timeUse = "用时" + timeUse;
        }
        tv_problem_usetime.setText(timeUse);

        String imgUrl = StringUtils.returnStrTxt(msgBean.getImgurl());
        if (!TextUtils.isEmpty(imgUrl)) {
            ImageHelper.showImageBitmap(gridview, QualityInspectionDetailActivity.this, imgUrl);
        }
    }

    private void showProblemState(String state) {
        colorInt = QualityInspectionDetailActivity.this.getResources().getColor(R.color.white);
        if (state.contains("1")) {
            state = getString(R.string.pending_txt);//待处理
            tv_problem_type.setTextColor(getResources().getColor(R.color.red));
        } else if (state.contains("2")) {
            tv_problem_type.setTextColor(getResources().getColor(R.color.red));
            state = getString(R.string.processing_txt);//处理中
        } else if (state.contains("3")) {
            state = getString(R.string.toclosed_txt);//待关闭
        } else {
            state = getString(R.string.closed_txt);//已关闭
        }
        tv_problem_type.setText(state);
        rl_type.setBackgroundColor(colorInt);
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
        tv_itme_year.setText(TimeUtils.getYear());
        detailAdapter = new QualityInspectionDetailAdapter(R.layout.recycler_item_quadetail, listData);
        recyclerview.setAdapter(detailAdapter);
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @OnClick({R.id.iv_item_claim, R.id.iv_item_forward, R.id.iv_common})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_item_claim://转发:
                forward();
                break;
            case R.id.iv_item_forward://认领:
                claim();
                break;
            case R.id.iv_common://通用
                onCommon();
                break;
        }
    }

    /*认领*/
    private void claim() {
        assert presenter != null;
        orgId = PreferencesUtils.getString(QualityInspectionDetailActivity.this, "orgId", "");
        userId = PreferencesUtils.getString(QualityInspectionDetailActivity.this, "id", "");
        presenter.updatequestions(ApiService.UPDATEQUESTIONS, userId, problemid,
                "2", "", "", "", "");
    }

    /*转发*/
    private void forward() {
        toForWardProblemActivity(problemid);
    }

    /*转发*/
    private void toForWardProblemActivity(String problemid) {
        Intent intent = new Intent();
        intent.setClass(this, ForwardProblemActivity.class);
        intent.putExtra("PROBLEMID", problemid);
        startActivityForResult(intent, 3);
    }

    private void onCommon() {
        if (type.equals("2")) {
            toBeFinish();
        } else {
            toBeClose();
        }
    }

    private void fowardOther() {
        getEventDetailInfos(problemid);
        showProblemState("3");
        type = "3";
        ivCommon.setImageResource(R.drawable.xq_finish02);
        llItemTwo.setVisibility(View.GONE);
        llItemOne.setVisibility(View.VISIBLE);
    }

    /*代完成*/
    private void toBeFinish() {
        toSuccessEventActivity(problemid);
    }

    /*完成问题*/
    private void toSuccessEventActivity(String problemid) {
        Intent intent = new Intent();
        intent.setClass(this, SuccessProblemActivity.class);
        intent.putExtra("PROBLEMID", problemid);
        startActivityForResult(intent, 1);
    }

    /*关闭*/
    private void toBeClose() {
        toFinishEventActivity(problemid);
    }

    /*关闭*/
    private void toFinishEventActivity(String problemid) {
        Intent intent = new Intent();
        intent.setClass(this, EvaluationEventActivity.class);
        intent.putExtra("PROBLEMID", problemid);
        startActivityForResult(intent, 2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1://完成
                if (resultCode == RESULT_OK) {
                    EventBus.getDefault().postSticky(new AllProblemEvent(Constans.CARRY_SUCESS, ""));
                    EventBus.getDefault().postSticky(new CommonEvent(Constans.COUNT_AGAIN_SUCESS, ""));
                    llItemOne.setVisibility(View.VISIBLE);
                    ivCommon.setImageResource(R.drawable.xq_close02);
                    showProblemState("3");
                    type = "3";
                    getEventDetailInfos(problemid);
                    llItemTwo.setVisibility(View.GONE);

                }
                break;
            case 2://关闭
                if (resultCode == RESULT_OK) {
                    EventBus.getDefault().postSticky(new AllProblemEvent(Constans.CLOSED_SSUCESS, ""));
                    EventBus.getDefault().postSticky(new CommonEvent(Constans.COUNT_AGAIN_SUCESS, ""));
                    llItemOne.setVisibility(View.GONE);
                    showProblemState("4");
                    type = "4";
                    getEventDetailInfos(problemid);
                    llItemTwo.setVisibility(View.GONE);
                }
                break;
            case 3://转发
                fowardOther();
                EventBus.getDefault().postSticky(new AllProblemEvent(Constans.ZHUANFA_SSUCESS, ""));
                EventBus.getDefault().postSticky(new CommonEvent(Constans.COUNT_AGAIN_SUCESS, ""));
                EventBus.getDefault().postSticky(new ProcessingEvent(Constans.ZHUANFA_SSUCESS, ""));
                EventBus.getDefault().postSticky(new PendingEvent(Constans.ZHUANFA_SSUCESS, ""));
                break;
        }
    }
}
