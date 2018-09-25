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
import com.yuefeng.features.adapter.PersonalAdapter;
import com.yuefeng.features.event.JobMonitoringEvent;
import com.yuefeng.features.event.JobMonitoringFragmentEvent;
import com.yuefeng.features.modle.GetJobMonitotingMsgBean;
import com.yuefeng.features.modle.PersonalinfoListBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/*人员*/
public class PersonalFragment extends BaseFragment {

    private static final String TAG = "tag";
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private List<PersonalinfoListBean> listData = new ArrayList<>();
    private PersonalAdapter adapter;
    private String address = "";


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
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        initRecycler();
    }

    private void initRecycler() {
        adapter = new PersonalAdapter(R.layout.recycler_item_job, listData);
        recyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PersonalinfoListBean personalinfoListBean = listData.get(position);
                EventBus.getDefault().postSticky(new JobMonitoringFragmentEvent(Constans.PERSONAL_SSUCESS, personalinfoListBean));

            }
        });

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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void disposeJobMonitoringEvent(JobMonitoringEvent event) {
        switch (event.getWhat()) {
            case Constans.JOB_SSUCESS://展示
                LogUtils.d("===========aaaaaaaaaaaaaa");
                GetJobMonitotingMsgBean bean = (GetJobMonitotingMsgBean) event.getData();
                if (bean != null) {
                    showAdapterDatasList(bean);
                }
                break;

            case Constans.JOB_ERROR:
                listData.clear();
                initRecycler();
                break;

        }

    }

    /*展示列表数据*/
    private void showAdapterDatasList(GetJobMonitotingMsgBean beanMsg) {

        List<PersonalinfoListBean> list = beanMsg.getPersonalinfoList();

        if (list.size() != 0) {
            listData.clear();
            listData.addAll(list);
            adapter.setNewData(listData);
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
