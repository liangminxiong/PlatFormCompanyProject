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
import com.common.utils.LogUtils;
import com.common.utils.PreferencesUtils;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.adapter.AllProblemAdapter;
import com.yuefeng.features.contract.QualityGetFragmentContract;
import com.yuefeng.features.event.AllProblemEvent;
import com.yuefeng.features.event.ToClosedEvent;
import com.yuefeng.features.modle.EventQuestionMsgBean;
import com.yuefeng.features.presenter.ToClosedProblemPresenter;
import com.yuefeng.features.ui.activity.EvaluationEventActivity;
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

import static android.app.Activity.RESULT_OK;


/*待关闭*/
public class ToClosedProblemFragment extends BaseFragment implements QualityGetFragmentContract.View {
    @BindView(R.id.ll_fragment)
    LinearLayout ll_fragment;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private List<EventQuestionMsgBean> listData = new ArrayList<>();
    private AllProblemAdapter allProblemAdapter;
    private String orgId;
    private String userId;
    private ToClosedProblemPresenter presenter;

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
        presenter = new ToClosedProblemPresenter(this, (QualityInspectionActivity) getActivity());
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
                switch (id) {
                    case R.id.iv_item_forward:
                        String problemid = listData.get(position).getId();
                        toSuccessEventActivity(problemid);
                        break;
                }
            }
        });
    }

    /*关闭*/
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void toSuccessEventActivity(String problemid) {
        Intent intent = new Intent();
        intent.setClass(Objects.requireNonNull(getActivity()), EvaluationEventActivity.class);
        intent.putExtra("PROBLEMID", problemid);
        startActivityForResult(intent, 2);
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
        orgId = PreferencesUtils.getString(Objects.requireNonNull(getActivity()), "orgId", "");
        userId = PreferencesUtils.getString(getActivity(), "id", "");
        presenter.getEventquestion(ApiService.GETEVENTQUESTION, orgId, userId, "3", b);
        LogUtils.d("getEventDatas 111= " + b);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void disposeToClosedEvent(ToClosedEvent event) {
        LogUtils.d("getEventDatas 555=" + event.getWhat());
        switch (event.getWhat()) {
            case Constans.TOCLOSED_SSUCESS:
                recyclerview.setVisibility(View.VISIBLE);
                List<EventQuestionMsgBean> bean = (List<EventQuestionMsgBean>) event.getData();
                if (bean != null) {
                    showAdapterDatasList(bean);
                }
                break;
            case Constans.CARRY_SUCESS://完成
                getEventDatasByNet(false);
                break;
            case Constans.TOCLOSED_ERROR:
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
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.d("onActivityResult = " + requestCode + " ++ " + resultCode);
        switch (requestCode) {
            case 2:
                if (resultCode == RESULT_OK) {
                    getEventDatasByNet(true);
                    EventBus.getDefault().postSticky(new AllProblemEvent(Constans.CLOSED_SSUCESS, ""));
                    EventBus.getDefault().postSticky(new CommonEvent(Constans.COUNT_AGAIN_SUCESS, ""));
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
