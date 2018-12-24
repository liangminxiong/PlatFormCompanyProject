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
import com.yuefeng.features.event.JobMonitoringFragmentEvent;
import com.yuefeng.features.event.PersonalListEvent;
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

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private List<PersonalinfoListBean> listData = new ArrayList<>();
    private PersonalAdapter adapter;

    private List<PersonalinfoListBean> list = new ArrayList<>();
    private String latitude;
    private String longitude;
    private GetJobMonitotingMsgBean bean = null;

    private int count = 0;
//    private GeoCoder mGeocoder;
    private LocationUtils mLocationUtils;
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
                    mFirstItemPosition = linearManager.findFirstVisibleItemPosition();
//                    if (count > mLastItemPosition) {
//                        count = mFirstItemPosition;
//                    }
                    mLastItemPosition = linearManager.findLastVisibleItemPosition();
                    //获取第一个可见view的位置
                    if (count < mLastItemPosition) {
                        count = mFirstItemPosition;
                        benginGetAddress(count, false);
                    }
                }
                LogUtils.d("==========000000000000===" + count + " === " + mFirstItemPosition);
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

//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
//    public void disposeJobMonitoringEvent(JobMonitoringEvent event) {
//        switch (event.getWhat()) {
//            case Constans.JOB_SSUCESS://展示
//                bean = (GetJobMonitotingMsgBean) event.getData();
//                if (bean != null) {
//                    list = bean.getPersonalinfoList();
//                    mSize = list.size();
//                }
//                break;
//
//            case Constans.JOB_ERROR:
//                noData();
//                break;
//
//        }
//    }

    private void noData() {
        listData.clear();
        initRecycler();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void disposePersonalListEvent(PersonalListEvent event) {
        switch (event.getWhat()) {
            case Constans.PERSONAL_SSUCESS_LIST://展示
                list.clear();
                list = (List<PersonalinfoListBean>) event.getData();
                mSize = list.size();
                if (mSize > 0) {
                    showAdapterDatasList(list);
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
        } else {
            noData();
        }
    }

    /*展示列表数据*/
    private void showAdapterDatasList(List<PersonalinfoListBean> list) {
        try {
            count = 0;
            initAdapterData(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initAdapterData(List<PersonalinfoListBean> list) {
        if (list.size() > 0) {
            listData.clear();
            listData.addAll(list);
            if (adapter != null) {
                adapter.setNewData(listData);
                benginGetAddress(count, true);
            }
        } else {
            noData();
        }
    }

    private void benginGetAddress(int position, boolean isFirst) {
        try {
            if (position > (mSize - 1)) {
                return;
            }
            if (mSize > 0 || list != null) {
                latitude = list.get(position).getLatitude();
                longitude = list.get(position).getLongitude();
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
            LogUtils.d("========================00000000");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
//        if (mGeocoder != null) {
//            mGeocoder.destroy();
//        }
    }


    @Override
    public void onReverseGeoCodeResult(Map<String, Object> map) {
        try {
            String address = (String) map.get("address");
//            if (TextUtils.isEmpty(address)) {
//                address = "检索当前地址失败!";
//            }
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


}
