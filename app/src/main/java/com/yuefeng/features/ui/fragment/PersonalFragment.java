package com.yuefeng.features.ui.fragment;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.common.base.codereview.BaseFragment;
import com.common.event.CommonEvent;
import com.common.utils.Constans;
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
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initRecycler() {
        adapter = new PersonalAdapter(R.layout.recycler_item_job, listData);
        recyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PersonalinfoListBean personalinfoListBean = listData.get(position);
                List<PersonalinfoListBean> personalinfoList = new ArrayList<>();
                personalinfoList.clear();
                personalinfoList.add(personalinfoListBean);
                EventBus.getDefault().postSticky(new JobMonitoringFragmentEvent(Constans.PERSONAL_SSUCESS, personalinfoList));

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
                if (bean != null) {
                    showAdapterDatasList(bean);
                }
                break;

            case Constans.JOB_ERROR:
                noData();
                break;

        }
    }

    private void noData() {
        listData.clear();
        initRecycler();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void disposeCommonEvent(CommonEvent event) {
        switch (event.getWhat()) {
            case Constans.JOB_P_SSUCESS://展示
                list.clear();
                list = (List<PersonalinfoListBean>) event.getData();
                initAdapterData(list);
                break;
        }
    }

    @Override
    protected void fetchData() {
    }

    /*展示列表数据*/
    private void showAdapterDatasList(GetJobMonitotingMsgBean beanMsg) {
        try {
            count = 0;
            list = beanMsg.getPersonalinfoList();
            initAdapterData(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initAdapterData(List<PersonalinfoListBean> list) {
        lenght = list.size();
        benginGetAddress(count, true);
        if (list.size() > 0) {
            listData.clear();
            listData.addAll(list);
            if (adapter != null) {
                adapter.setNewData(listData);
            }
        }else {
            noData();
        }
    }

    private void benginGetAddress(int count, boolean isFirst) {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReverseGeoCodeResult(Map<String, Object> map) {
        try {
            String address = (String) map.get("address");
            if (TextUtils.isEmpty(address)) {
                address = "暂无地址!";
            }
            if (!address.contains("广东省")) {
                if (lenght > 0 || adapter != null) {
                    if (count <= (lenght - 1)) {
                        list.get(count).setAddress(address);
                        adapter.notifyDataSetChanged();
                        count++;
                        benginGetAddress(count, false);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
