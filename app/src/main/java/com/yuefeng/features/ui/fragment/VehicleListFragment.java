package com.yuefeng.features.ui.fragment;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.common.base.codereview.BaseFragment;
import com.common.utils.Constans;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.adapter.VehicleListAdapter;
import com.yuefeng.features.event.JobMonitoringFragmentEvent;
import com.yuefeng.features.event.VechileListEvent;
import com.yuefeng.features.modle.GetJobMonitotingMsgBean;
import com.yuefeng.features.modle.VehicleinfoListBean;
import com.yuefeng.utils.LocationUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


/*车辆*/
public class VehicleListFragment extends BaseFragment implements View.OnTouchListener, LocationUtils.OnResultMapListener {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private List<VehicleinfoListBean> listData = new ArrayList<>();
    private VehicleListAdapter adapter;

    private List<VehicleinfoListBean> list = new ArrayList<>();
    private String latitude;
    private String longitude;
    // 获取反地理编码对象
    private LocationUtils mLocationUtils;
    // 用来存储获取的定位信息
    private GetJobMonitotingMsgBean bean = null;
    private int count = 0;
    private int mLastItemPosition;
    private int mFirstItemPosition;
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

            // 创建定位管理信息对象
            mLocationUtils = new LocationUtils(getActivity());
//         开启定位
            mLocationUtils.startLocation();
            mLocationUtils.registerOnResult(this);
            mFirstItemPosition = 0;
            mLastItemPosition = 4;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initRecycler() {
        try {
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
            recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                }

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    //判断是当前layoutManager是否为LinearLayoutManager
                    // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                    if (layoutManager instanceof LinearLayoutManager) {
                        LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                        //获取最后一个可见view的位置
                        //获取第一个可见view的位置
                        mFirstItemPosition = linearManager.findFirstVisibleItemPosition();
                        if (count > mLastItemPosition) {
                            count = mFirstItemPosition;
                        }
                        mLastItemPosition = linearManager.findLastVisibleItemPosition();
                        if (count < mLastItemPosition) {
                            count = mFirstItemPosition;
                            benginGetAddress(count, false);
                        }
                    }
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
//                    list = bean.getVehicleinfoList();
//                    mSize = list.size();
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
    public void disposeVechileListEvent(VechileListEvent event) {
        switch (event.getWhat()) {
            case Constans.VEHICLE_SSUCESS_LIST://展示
                list.clear();
                list = (List<VehicleinfoListBean>) event.getData();
                mSize = list.size();
                if (mSize > 0) {
//                    showAdapterDatasList(list);
                } else {
                    noData();
                }
                break;
        }
    }


    @Override
    protected void fetchData() {
        if (mSize > 0) {
            showAdapterDatasList(list);
        }
    }

    /*展示列表数据*/
    private void showAdapterDatasList(List<VehicleinfoListBean> list) {
        try {
            count = 0;
            mSize = list.size();
            initAdapterData(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initAdapterData(List<VehicleinfoListBean> list) {

        if (list.size() != 0 && adapter != null) {
            listData.clear();
            listData.addAll(list);
            adapter.setNewData(listData);
            benginGetAddress(count, true);
        } else {
            noData();
        }
    }

    private void benginGetAddress(int count, boolean isFirst) {
        try {
            if (count > (mSize - 1)) {
                return;
            }
            if (mSize > 0 || list != null) {
                latitude = list.get(count).getLatitude();
                longitude = list.get(count).getLongitude();
                if (!TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longitude)) {
                    if (mLocationUtils == null) {
                        // 开启反编译
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
            if (mSize > 0 || adapter != null) {
                if (count <= (mSize - 1)) {
                    list.get(count).setAddress(address);
                    adapter.notifyDataSetChanged();
                    count++;
                    if (count <= mLastItemPosition) {
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
