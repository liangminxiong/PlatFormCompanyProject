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
import com.yuefeng.features.event.ProcessingEvent;
import com.yuefeng.features.event.ToClosedEvent;
import com.yuefeng.features.modle.EventQuestionMsgBean;
import com.yuefeng.features.presenter.ProcessingProblemPresenter;
import com.yuefeng.features.ui.activity.QualityInspectionActivity;
import com.yuefeng.features.ui.activity.QualityInspectionDetailActivity;
import com.yuefeng.features.ui.activity.SuccessProblemActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;


/*处理中*/
public class ProcessingProblemFragment extends BaseFragment implements QualityGetFragmentContract.View {

    @BindView(R.id.ll_fragment)
    LinearLayout ll_fragment;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private List<EventQuestionMsgBean> listData = new ArrayList<>();
    private AllProblemAdapter allProblemAdapter;
    private String orgId;
    private String userId;
    private ProcessingProblemPresenter presenter;

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
        presenter = new ProcessingProblemPresenter(this, (QualityInspectionActivity) getActivity());
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        initRecycler();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void fetchData() {
        getEventDatasByNet(true);
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
                Intent intent = new Intent();
                intent.setClass(Objects.requireNonNull(getActivity()), QualityInspectionDetailActivity.class);
                intent.putExtra("PROBLEMID", problemid);
                intent.putExtra("NAME", name);
                startActivity(intent);
            }
        });

        allProblemAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                switch (id) {
                    case R.id.iv_item_forward:
                        String problemid = listData.get(position).getId();
                        toSuccessEventActivity(problemid);
                        break;
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void toSuccessEventActivity(String problemid) {
        Intent intent = new Intent();
        intent.setClass(Objects.requireNonNull(getActivity()), SuccessProblemActivity.class);
        intent.putExtra("PROBLEMID", problemid);
        startActivityForResult(intent, 1);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void initData() {

    }

    @Override
    protected void setLisenter() {

    }

    @Override
    protected void widgetClick(View v) {

    }

    /*获取网络数据*/
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getEventDatasByNet(boolean b) {
        orgId = PreferencesUtils.getString(getActivity(), "orgId", "");
        userId = PreferencesUtils.getString(getActivity(), "id", "");
        presenter.getEventquestion(ApiService.GETEVENTQUESTION, orgId, userId, "2", b);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void disposeProcessingEvent(ProcessingEvent event) {
        switch (event.getWhat()) {
            case Constans.CHULIZHONG_SSUCESS://获取数据成功
                recyclerview.setVisibility(View.VISIBLE);
                List<EventQuestionMsgBean> bean = (List<EventQuestionMsgBean>) event.getData();
                if (bean != null) {
                    showAdapterDatasList(bean);
                }
                break;
            case Constans.CLAIM_SUCESS://认领
                getEventDatasByNet(false);
                break;

            case Constans.CARRY_SUCESS://完成成功
                getEventDatasByNet(false);
                break;
            case Constans.ZHUANFA_SSUCESS://转发成功
                getEventDatasByNet(false);
                break;

            case Constans.CHULIZHONG_ERROR:
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {//完成
                    EventBus.getDefault().postSticky(new CommonEvent(Constans.COUNT_AGAIN_SUCESS, ""));
                    EventBus.getDefault().postSticky(new ProcessingEvent(Constans.CARRY_SUCESS, ""));
                    EventBus.getDefault().postSticky(new AllProblemEvent(Constans.CARRY_SUCESS, ""));
                    EventBus.getDefault().postSticky(new ToClosedEvent(Constans.CARRY_SUCESS, ""));
                }
                break;
        }
    }


    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
