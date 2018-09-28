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
    private List<String> addressList = new ArrayList<>();
    private int lenght;
    private int len;

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
        addressList.clear();
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

        list = beanMsg.getPersonalinfoList();
        lenght = list.size();
        for (int i = 0; i < lenght; i++) {
            latitude = list.get(i).getLatitude();
            longitude = list.get(i).getLongitude();
            if (!TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longitude)) {
                if (mLocationUtils == null) {
                    mLocationUtils = new LocationUtils(getActivity());
//         开启定位
                    mLocationUtils.startLocation();
                    mLocationUtils.registerOnResult(this);
                }
                if (i == 0) {
                    mLocationUtils.getAddress(Double.valueOf(latitude), Double.valueOf(longitude));
                }
                mLocationUtils.getAddress(Double.valueOf(latitude), Double.valueOf(longitude));
            }
        }

        if (list.size() > 0) {
            listData.clear();
            listData.addAll(list);
            if (adapter != null) {
                adapter.setNewData(listData);
            }
        }
    }

    @Override
    public void onReverseGeoCodeResult(Map<String, Object> map) {
        String address = (String) map.get("address");
        assert addressList != null;
        if (!TextUtils.isEmpty(address)) {
            addressList.add(address);
        }
        len = addressList.size();

        assert list != null;
        LogUtils.d("getAddress111" + address + "  +++  " + len + " ++ " + lenght);

        if (list.size() != 0 && len > 0) {
            for (int i = 0; i < len; i++) {
                list.get(i).setAddress(addressList.get(i));
            }
            listData.clear();
            listData.addAll(list);
            adapter.setNewData(listData);
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
