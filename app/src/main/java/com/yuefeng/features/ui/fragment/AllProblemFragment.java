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
import com.yuefeng.features.modle.EventQuestionMsgBean;
import com.yuefeng.features.presenter.AllProblemPresenter;
import com.yuefeng.features.ui.activity.EvaluationEventActivity;
import com.yuefeng.features.ui.activity.ForwardProblemActivity;
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


/*全部*/
public class AllProblemFragment extends BaseFragment implements QualityGetFragmentContract.View {

    private static final String TAG = "tag";
    @BindView(R.id.ll_fragment)
    LinearLayout ll_fragment;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private List<EventQuestionMsgBean> listData = new ArrayList<>();
    private AllProblemAdapter allProblemAdapter;
    private String orgId;
    private String userId;
    private AllProblemPresenter presenter;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_allproblem;
    }

    @Override
    public void initView() {
        try {
            ButterKnife.bind(this, rootView);
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }
            presenter = new AllProblemPresenter(this, (QualityInspectionActivity) getActivity());
            recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
            initRecycler();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initRecycler() {
        try {
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
                            if (eventQuestionMsgBean != null) {
                                chuliEvent(eventQuestionMsgBean);
                            }
                            break;
                        case R.id.iv_item_claim:
                            if (eventQuestionMsgBean != null) {
                                iv_item_claim(eventQuestionMsgBean);
                            }
                            break;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void fetchData() {
        getEventDatasByNet(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        try {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser) {
                getEventDatasByNet(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*转发*/
    private void iv_item_claim(EventQuestionMsgBean eventQuestionMsgBean) {
        String problemId = eventQuestionMsgBean.getId();
        toForWardProblemActivity(problemId);
    }

    /*处理问题*/
    private void chuliEvent(EventQuestionMsgBean eventQuestionMsgBean) {
        String id = eventQuestionMsgBean.getId();
        String state = eventQuestionMsgBean.getState();

        if (state.equals("1")) {
            claimEvent(id);//待处理
        } else if (state.equals("2")) {
            toSuccessEventActivity(id);//处理中
        } else if (state.equals("3")) {
            toFinishEventActivity(id);//待关闭
        }
    }

    /*转发*/
    private void toForWardProblemActivity(String problemid) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), ForwardProblemActivity.class);
        intent.putExtra("PROBLEMID", problemid);
        startActivityForResult(intent, 3);
    }

    /*关闭*/
    private void toFinishEventActivity(String problemid) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), EvaluationEventActivity.class);
        intent.putExtra("PROBLEMID", problemid);
        startActivityForResult(intent, 2);
    }

    /*完成问题*/
    private void toSuccessEventActivity(String problemid) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), SuccessProblemActivity.class);
        intent.putExtra("PROBLEMID", problemid);
        startActivityForResult(intent, 1);
    }

    /*r认领问题*/
    private void claimEvent(String id) {
        orgId = PreferencesUtils.getString(getActivity(), "orgId", "");
        userId = PreferencesUtils.getString(getActivity(), "id", "");

        presenter.updatequestions(ApiService.UPDATEQUESTIONS, userId, id,
                "2", "", "", "", "");
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

    /*获取网络数据*/
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getEventDatasByNet(boolean b) {
        try {
            orgId = PreferencesUtils.getString(Objects.requireNonNull(getActivity()), "orgId", "");
            userId = PreferencesUtils.getString(getActivity(), "id", "");
            presenter.getEventquestion(ApiService.GETEVENTQUESTION, orgId, userId, "5", b);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void disposeAllProblemEvent(AllProblemEvent event) {
        switch (event.getWhat()) {
            case Constans.ALL_SSUCESS://成功
                List<EventQuestionMsgBean> bean = (List<EventQuestionMsgBean>) event.getData();
                if (bean != null) {
                    showAdapterDatasList(bean);
                }
                break;
            case Constans.CLAIM_SUCESS://认领成功
                getEventDatasByNet(false);
                break;
            case Constans.CARRY_SUCESS://完成
                getEventDatasByNet(false);
                break;
            case Constans.CLOSED_SSUCESS://关闭
                getEventDatasByNet(false);
                break;
            case Constans.ZHUANFA_SSUCESS://ZHUANF
                getEventDatasByNet(false);
                break;

            case Constans.CLAIM_ERROR:
                showErrorToast("认领失败，请重试");
                break;
            case Constans.SUBMITERROR://失败
                listData.clear();
                if (allProblemAdapter != null) {
                    allProblemAdapter.setNewData(listData);
                }
                break;
        }
    }

    /*展示列表数据*/
    private void showAdapterDatasList(List<EventQuestionMsgBean> beanMsg) {
        try {
            if (beanMsg.size() != 0) {
                listData.clear();
                listData.addAll(beanMsg);
                allProblemAdapter.setNewData(listData);
            } else {
                listData.clear();
                initRecycler();
            }
        } catch (Exception e) {
            e.printStackTrace();
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
            case 1://完成
                if (resultCode == RESULT_OK) {
                    EventBus.getDefault().postSticky(new AllProblemEvent(Constans.CARRY_SUCESS, ""));
                    EventBus.getDefault().postSticky(new CommonEvent(Constans.COUNT_AGAIN_SUCESS, ""));
//                    EventBus.getDefault().postSticky(new ProcessingEvent(Constans.CARRY_SUCESS, ""));
//                    EventBus.getDefault().postSticky(new ToClosedEvent(Constans.CARRY_SUCESS, ""));
                }
                break;
            case 2://关闭
                if (resultCode == RESULT_OK) {
                    EventBus.getDefault().postSticky(new AllProblemEvent(Constans.CLOSED_SSUCESS, ""));
                    EventBus.getDefault().postSticky(new CommonEvent(Constans.COUNT_AGAIN_SUCESS, ""));
//                    EventBus.getDefault().postSticky(new ToClosedEvent(Constans.CLOSED_SSUCESS, ""));
                }
                break;

            case 3://转发
                EventBus.getDefault().postSticky(new AllProblemEvent(Constans.ZHUANFA_SSUCESS, ""));
                EventBus.getDefault().postSticky(new CommonEvent(Constans.COUNT_AGAIN_SUCESS, ""));
//                EventBus.getDefault().postSticky(new ProcessingEvent(Constans.ZHUANFA_SSUCESS, ""));
//                EventBus.getDefault().postSticky(new PendingEvent(Constans.ZHUANFA_SSUCESS, ""));
                break;
        }
    }
}
