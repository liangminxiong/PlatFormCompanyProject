package com.yuefeng.features.ui.fragment;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.common.base.codereview.BaseFragment;
import com.common.utils.Constans;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.adapter.VehicleListAdapter;
import com.yuefeng.features.event.JobMonitoringEvent;
import com.yuefeng.features.event.JobMonitoringFragmentEvent;
import com.yuefeng.features.modle.GetJobMonitotingMsgBean;
import com.yuefeng.features.modle.VehicleinfoListBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/*车辆*/
public class VehicleListFragment extends BaseFragment implements View.OnTouchListener {

    private static final String TAG = "tag";
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private List<VehicleinfoListBean> listData = new ArrayList<>();
    private VehicleListAdapter adapter;


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
        adapter = new VehicleListAdapter(R.layout.recycler_item_job, listData);
        recyclerview.getParent().requestDisallowInterceptTouchEvent(true);
        recyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                VehicleinfoListBean vehicleinfoListBean = listData.get(position);
                EventBus.getDefault().postSticky(new JobMonitoringFragmentEvent(Constans.VEHICLE_SSUCESS, vehicleinfoListBean));
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
        try {


        List<VehicleinfoListBean> list = beanMsg.getVehicleinfoList();
        if (list.size() != 0) {
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
