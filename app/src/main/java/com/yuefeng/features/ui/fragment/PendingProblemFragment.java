package com.yuefeng.features.ui.fragment;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.common.base.codereview.BaseFragment;
import com.common.event.CommonEvent;
import com.common.network.ApiService;
import com.common.utils.Constans;
import com.common.utils.PreferencesUtils;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.adapter.AllProblemAdapter;
import com.yuefeng.features.contract.QualityGetFragmentContract;
import com.yuefeng.features.event.AllProblemEvent;
import com.yuefeng.features.event.PendingEvent;
import com.yuefeng.features.event.ProcessingEvent;
import com.yuefeng.features.modle.EventQuestionMsgBean;
import com.yuefeng.features.presenter.PendingProblemPresenter;
import com.yuefeng.features.ui.activity.ForwardProblemActivity;
import com.yuefeng.features.ui.activity.QualityInspectionActivity;
import com.yuefeng.features.ui.activity.QualityInspectionDetailActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


/*待处理*/
public class PendingProblemFragment extends BaseFragment implements QualityGetFragmentContract.View {

    @BindView(R.id.ll_fragment)
    LinearLayout ll_fragment;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private List<EventQuestionMsgBean> listData = new ArrayList<>();
    private AllProblemAdapter allProblemAdapter;
    private String orgId;
    private String userId;
    private PendingProblemPresenter presenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_allproblem;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this, rootView);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        presenter = new PendingProblemPresenter(this, (QualityInspectionActivity) getActivity());
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        initRecycler();
    }


    @Override
    protected void fetchData() {

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getEventDatasByNet(true);
        }
    }

    private void initRecycler() {
        allProblemAdapter = new AllProblemAdapter(R.layout.recycler_item_tab, listData);
        recyclerview.setAdapter(allProblemAdapter);
        allProblemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String problemid = listData.get(position).getId();
                String name = listData.get(position).getUploadpeoplename();
                String state = listData.get(position).getState();
                Intent intent = new Intent();
                intent.setClass(Objects.requireNonNull(getActivity()), QualityInspectionDetailActivity.class);
                intent.putExtra("PROBLEMID", problemid);
                intent.putExtra("NAME", name);
                intent.putExtra("STATE", state);
                intent.putExtra("NOREPLY", "Yes");
                startActivity(intent);
            }
        });

        allProblemAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                EventQuestionMsgBean eventQuestionMsgBean = listData.get(position);
                switch (id) {
                    case R.id.iv_item_forward:
                        if (eventQuestionMsgBean != null)
                            claimEvent(eventQuestionMsgBean);
                        break;
                    case R.id.iv_item_claim:
                        if (eventQuestionMsgBean != null) {
                            iv_item_claim(eventQuestionMsgBean);
                        }
                        break;
                }
            }
        });
    }

    /*转发*/
    private void iv_item_claim(EventQuestionMsgBean eventQuestionMsgBean) {
        String problemId = eventQuestionMsgBean.getId();
        toForWardProblemActivity(problemId);
    }

    /*转发*/
    private void toForWardProblemActivity(String problemid) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), ForwardProblemActivity.class);
        intent.putExtra("PROBLEMID", problemid);
        startActivityForResult(intent, 3);
    }

    @Override
    protected void setLisenter() {

    }

    @Override
    protected void widgetClick(View v) {

    }

    /*认领问题*/
    private void claimEvent(EventQuestionMsgBean eventQuestionMsgBean) {
        orgId = PreferencesUtils.getString(getActivity(), "orgId", "");
        userId = PreferencesUtils.getString(getActivity(), "id", "");
        presenter.updatequestions(ApiService.UPDATEQUESTIONS, userId, eventQuestionMsgBean.getId(),
                "2", "", "", "", "");
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void initData() {

    }

    /*获取网络数据*/
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getEventDatasByNet(boolean b) {
        orgId = PreferencesUtils.getString(getActivity(), "orgId", "");
        userId = PreferencesUtils.getString(getActivity(), "id", "");
        presenter.getEventquestion(ApiService.GETEVENTQUESTION, orgId, userId, "1", b);

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void disposePendingEvent(PendingEvent event) {
        dismissLoadingDialog();
        switch (event.getWhat()) {
            case Constans.DAI_SSUCESS:
                recyclerview.setVisibility(View.VISIBLE);
                List<EventQuestionMsgBean> bean = (List<EventQuestionMsgBean>) event.getData();
                if (bean != null) {
                    showAdapterDatasList(bean);
                }
                break;
            case Constans.CLAIM_SUCESS://认领成功
                getEventDatasByNet(false);
                break;
            case Constans.ZHUANFA_SSUCESS://转发成功
                getEventDatasByNet(false);
                break;
            case Constans.DAI_ERROR://失败
                recyclerview.setVisibility(View.INVISIBLE);
                listData.clear();
                initRecycler();
                break;
        }
    }

    /*展示列表数据*/
    private void showAdapterDatasList(List<EventQuestionMsgBean> beanMsg) {
        if (beanMsg.size() != 0) {
            listData.clear();
            listData.addAll(beanMsg);
            allProblemAdapter.setNewData(listData);
        } else {
            listData.clear();
            initRecycler();
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 3://转发
                EventBus.getDefault().postSticky(new AllProblemEvent(Constans.ZHUANFA_SSUCESS, ""));
                EventBus.getDefault().postSticky(new CommonEvent(Constans.COUNT_AGAIN_SUCESS, ""));
                EventBus.getDefault().postSticky(new ProcessingEvent(Constans.ZHUANFA_SSUCESS, ""));
                EventBus.getDefault().postSticky(new PendingEvent(Constans.ZHUANFA_SSUCESS, ""));
                break;
        }
    }
}
