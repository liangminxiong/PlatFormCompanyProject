package com.yuefeng.features.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.common.base.codereview.BaseActivity;
import com.common.network.ApiService;
import com.common.utils.Constans;
import com.common.utils.PreferencesUtils;
import com.common.utils.StatusBarUtil;
import com.common.view.dialog.SucessCacheSureDialog;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.adapter.AllPersonalAdapter;
import com.yuefeng.features.contract.ForWardContract;
import com.yuefeng.features.event.AllPersoanlEvent;
import com.yuefeng.features.modle.GetAllPersonalMsgBean;
import com.yuefeng.features.presenter.ForWardPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;


/*转发问题*/
public class ForwardProblemActivity extends BaseActivity implements ForWardContract.View {

    @BindView(R.id.iv_back)
    RelativeLayout iv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindColor(R.color.titel_color)
    int coloeWhite;
    @BindColor(R.color.titel_color)
    int coloeGray;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private String problemid;
    private String orgId;
    private String userId;
    private ForWardPresenter presenter;
    private AllPersonalAdapter adapter;
    private List<GetAllPersonalMsgBean> listData = new ArrayList<>();
    private SucessCacheSureDialog sureDialog;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_forward;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        ButterKnife.bind(this);
        presenter = new ForWardPresenter(this, this);

        View view = findViewById(R.id.space);

        view.setBackground(mActivity.getResources().getDrawable(R.drawable.title_toolbar_bg_blue));
        StatusBarUtil.setFadeStatusBarHeight(mActivity, view);
        tv_title.setText("人员列表");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            problemid = (String) bundle.get("PROBLEMID");
        }
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        initRecycler();
    }

    private void initRecycler() {
        adapter = new AllPersonalAdapter(R.layout.recyclerview_item_forward, listData);
        recyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String name = listData.get(position).getName();
                String personalID = listData.get(position).getId();
                showDialog(name, personalID);

            }
        });
    }

    private void showDialog(String name, final String personalID) {
        if (sureDialog == null) {
            sureDialog = new SucessCacheSureDialog(this);
        }
        sureDialog.setTextContent("是否转发任务给" + name);
        sureDialog.setDeletaCacheListener(new SucessCacheSureDialog.DeletaCacheListener() {
            @Override
            public void sure() {
                sureDialog.dismiss();
                ForwardProblemActivity.this.sure(personalID);
            }

            @Override
            public void cancle() {
                sureDialog.dismiss();
            }
        });

        if (!isFinishing()) {
            sureDialog.show();
        }
    }

    /*派发任务*/
    private void sure(String personalID) {
        orgId = PreferencesUtils.getString(this, "orgId", "");
        userId = PreferencesUtils.getString(this, "id", "");
        presenter.updatequestions(ApiService.UPDATEQUESTIONS, userId, problemid, "5", "",
                "", "", personalID);
    }

    /*获取主管*/
    @Override
    public void initData() {
        String pid = PreferencesUtils.getString(this, Constans.ORGID, "");
        presenter.getAllPersonal(ApiService.GETALLPERSON, pid);
    }

    @Override
    protected void setLisenter() {

    }

    @Override
    protected void widgetClick(View v) {

    }


    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void disposeAllPersoanlEvent(AllPersoanlEvent event) {

        switch (event.getWhat()) {
            case Constans.GETPERSONAL_SSUCESS:
                List<GetAllPersonalMsgBean> bean = (List<GetAllPersonalMsgBean>) event.getData();
                if (bean.size() != 0) {
                    showAdapterDatasList(bean);
                } else {
                    listData.clear();
                    initRecycler();
                }
                break;
            case Constans.CLAIM_SUCESS://派发成功
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                showSuccessDialog("转发成功，退出当前界面?");
                break;
            case Constans.SUBMITERROR://派发失败

                break;
            default:
                break;
        }
    }

    /*展示数据*/
    private void showAdapterDatasList(List<GetAllPersonalMsgBean> bean) {
        listData.clear();
        listData.addAll(bean);
        adapter.setNewData(listData);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
