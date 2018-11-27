package com.yuefeng.features.ui.fragment;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.common.base.codereview.BaseFragment;
import com.common.utils.Constans;
import com.common.utils.LogUtils;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.adapter.QuetionListAdapter;
import com.yuefeng.features.event.JobMonitoringFragmentEvent;
import com.yuefeng.features.event.ProblemListEvent;
import com.yuefeng.features.modle.GetJobMonitotingMsgBean;
import com.yuefeng.features.modle.QuestionListBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/*问题*/
public class QuestionListFragment extends BaseFragment {

    private static final String TAG = "tag";
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private List<QuestionListBean> listData = new ArrayList<>();
    private QuetionListAdapter adapter;
    private GetJobMonitotingMsgBean bean = null;
    private List<QuestionListBean> mList = new ArrayList<>();
    private int mSize;


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
            recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
            initRecycler();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initRecycler() {
        try {
            adapter = new QuetionListAdapter(R.layout.recycler_item_job, listData);
            recyclerview.setAdapter(adapter);
            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    QuestionListBean questionListBean = listData.get(position);
                    EventBus.getDefault().postSticky(new JobMonitoringFragmentEvent(Constans.PROBLEM_SSUCESS, questionListBean));

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
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

//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
//    public void disposeJobMonitoringEvent(JobMonitoringEvent event) {
//        switch (event.getWhat()) {
//            case Constans.JOB_SSUCESS://展示
//                bean = (GetJobMonitotingMsgBean) event.getData();
//                if (bean != null) {
//                    mList = bean.getQuestionList();
//                    mSize = mList.size();
//                }
//                break;
//            case Constans.JOB_ERROR:
//                noData();
//                break;
//        }
//    }

    private void noData() {
        listData.clear();
        initRecycler();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void disposeProblemListEvent(ProblemListEvent event) {
        switch (event.getWhat()) {
            case Constans.PROBLEM_SSUCESS_LIST://展示:
                mList.clear();
                mList = (List<QuestionListBean>) event.getData();
                LogUtils.d("333333333==" + mList.size());
                mSize = mList.size();
                if (mSize > 0) {
                    showAdapterDatasList(mList);
                } else {
                    noData();
                }
                break;
        }
    }

    @Override
    protected void fetchData() {
        if (mSize > 0) {
            showAdapterDatasList(mList);
        }
    }

    /*展示列表数据*/
    private void showAdapterDatasList(List<QuestionListBean> list) {
        try {
            if (list.size() > 0) {
                listData.clear();
                listData.addAll(list);
                adapter.setNewData(listData);
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
}
