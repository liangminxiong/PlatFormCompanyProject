package com.yuefeng.features.ui.fragment;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.common.base.codereview.BaseFragment;
import com.common.utils.Constans;
import com.common.utils.LogUtils;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.adapter.VehicleListAdapter;
import com.yuefeng.features.event.JobMonitoringEvent;
import com.yuefeng.features.event.JobMonitoringFragmentEvent;
import com.yuefeng.features.modle.GetJobMonitotingMsgBean;
import com.yuefeng.features.modle.VehicleinfoListBean;
import com.yuefeng.utils.BdLocationUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


/*车辆*/
public class VehicleListFragment extends BaseFragment implements View.OnTouchListener, OnGetGeoCoderResultListener {

    private static final String TAG = "tag";
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private List<VehicleinfoListBean> listData = new ArrayList<>();
    private VehicleListAdapter adapter;

    private List<VehicleinfoListBean> list = null;
    private String latitude;
    private String longitude;
    private List<String> addressList = new ArrayList<>();
    private int lenght;
    private int len;
    // 获取反地理编码对象
    private GeoCoder mGeoCoder = GeoCoder.newInstance();
    // 用来存储获取的定位信息
    private Map<String, Object> map = new HashMap<String, Object>();


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
            list = beanMsg.getVehicleinfoList();

            lenght = list.size();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < lenght; i++) {
                        latitude = list.get(i).getLatitude();
                        longitude = list.get(i).getLongitude();
                        if (!TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longitude)) {
                            if (i == 0) {
                                getAddress(Double.valueOf(latitude), Double.valueOf(longitude));
                            }
                            getAddress(Double.valueOf(latitude), Double.valueOf(longitude));
                        }
                    }
                }
            }).start();

            if (list.size() != 0) {
                listData.clear();
                listData.addAll(list);
                adapter.setNewData(listData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 进行反地理编码	 *
     * * @param latitude* 纬度信息
     * * @param lontitude* 经度信息
     */
    private void getAddress(double latitude, double lontitude) {
        LatLng mLatLng = new LatLng(latitude, lontitude);
        // 反地理编码请求参数对象
        LogUtils.d("getAddress222" + mLatLng);
        ReverseGeoCodeOption mReverseGeoCodeOption = new ReverseGeoCodeOption();
        // 设置请求参数
        mReverseGeoCodeOption.location(BdLocationUtil.ConverGpsToBaidu(mLatLng));
//        mReverseGeoCodeOption.newVersion(1);
        // 发起反地理编码请求(经纬度->地址信息)
        mGeoCoder.reverseGeoCode(mReverseGeoCodeOption);
        // 设置查询结果监听者
        mGeoCoder.setOnGetGeoCodeResultListener(this);
    }


    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        String address = result.getAddress();
        assert addressList != null;
        if (TextUtils.isEmpty(address)) {
            address = "未知区域";
        }
        addressList.add(address);
        len = addressList.size();

        assert list != null;
        LogUtils.d("getAddress111" + address + "  +++  " + len + " ++ " + lenght);
        if (list.size() != 0 && len > 0) {
            for (int i = 0; i < len; i++) {
                list.get(i).setAddress(addressList.get(i));
            }
            listData.clear();
            listData.addAll(list);
            if (adapter != null) {
                adapter.setNewData(listData);
            }
        }
    }


    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        if (mGeoCoder != null) {
            mGeoCoder.destroy();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
