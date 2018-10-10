package com.yuefeng.features.ui.fragment;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.yuefeng.utils.LocationUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


/*人员*/
public class PersonalFragment extends BaseFragment implements LocationUtils.OnResultMapListener {

    private static final String TAG = "tag";
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private List<PersonalinfoListBean> listData = new ArrayList<>();
    private PersonalAdapter adapter;

    private LocationUtils mLocationUtils;
    private List<PersonalinfoListBean> list = null;
    private String latitude;
    private String longitude;
    private int lenght;
    private GetJobMonitotingMsgBean bean = null;

    private int count = 0;

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
        // 创建定位管理信息对象
        mLocationUtils = new LocationUtils(getActivity());
//         开启定位
        mLocationUtils.startLocation();
        mLocationUtils.registerOnResult(this);
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
                bean = (GetJobMonitotingMsgBean) event.getData();
//                if (bean != null) {
//                    showAdapterDatasList(bean);
//                }
                break;

            case Constans.JOB_ERROR:
                listData.clear();
                initRecycler();
                break;

        }

    }

    @Override
    protected void fetchData() {
        if (bean != null) {
            showAdapterDatasList(bean);
        }
    }

    /*展示列表数据*/
    private void showAdapterDatasList(GetJobMonitotingMsgBean beanMsg) {
        count = 0;
        list = beanMsg.getPersonalinfoList();
        lenght = list.size();
        benginGetAddress(count, true);
        if (list.size() > 0) {
            listData.clear();
            listData.addAll(list);
            if (adapter != null) {
                adapter.setNewData(listData);
            }
        }
    }

    private void benginGetAddress(int count, boolean isFirst) {
        if (count > (lenght - 1)) {
            return;
        }
        if (lenght > 0 || list != null) {
            latitude = list.get(count).getLatitude();
            longitude = list.get(count).getLongitude();
            if (!TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longitude)) {
                if (mLocationUtils == null) {
//         开启定位
                    mLocationUtils = new LocationUtils(getActivity());
                    mLocationUtils.startLocation();
                    mLocationUtils.registerOnResult(this);
                }
                if (isFirst) {
                    mLocationUtils.getAddress(Double.valueOf(latitude), Double.valueOf(longitude));
                }
                mLocationUtils.getAddress(Double.valueOf(latitude), Double.valueOf(longitude));
            }
        }
    }

    @Override
    public void onReverseGeoCodeResult(Map<String, Object> map) {
        String address = (String) map.get("address");
        LogUtils.d("onReverseGeo 00= " + address);
        if (TextUtils.isEmpty(address)) {
            address = "暂无地址!";
        }

        if (lenght > 0 || adapter != null) {
            if (count <= (lenght - 1)) {
                list.get(count).setAddress(address);
                adapter.notifyDataSetChanged();
                count++;
                benginGetAddress(count, false);
            }

        }
    }

    @Override
    public void onGeoCodeResult(Map<String, Object> map) {

    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        if (mLocationUtils != null) {
            mLocationUtils.onDestory();
        }
    }
}
