package com.yuefeng.features.ui.fragment.Lllegal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.common.base.codereview.BaseFragment;
import com.common.network.ApiService;
import com.common.utils.Constans;
import com.common.utils.PreferencesUtils;
import com.common.utils.TimeUtils;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.adapter.PersonalLllegalWorkListAdapter;
import com.yuefeng.features.contract.LllegalWorkContract;
import com.yuefeng.features.event.LllegalWorkEvent;
import com.yuefeng.features.modle.LllegalworMsgBean;
import com.yuefeng.features.presenter.LllegalWorkPresenter;
import com.yuefeng.features.ui.activity.Lllegalwork.LllegalWorkActivity;
import com.yuefeng.features.ui.activity.Lllegalwork.LllegalWorkDetailActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/*人员*/
public class PersonalLllegalWorkListFragment extends BaseFragment implements LllegalWorkContract.View {

    private static final String TAG = "tag";
    @BindView(R.id.rl_nodata)
    RelativeLayout rlNodata;
    @BindView(R.id.ll_data)
    LinearLayout llData;
    @BindView(R.id.tv_lllegal_count)
    TextView tvLllegalCount;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    Unbinder unbinder;
    private List<LllegalworMsgBean> listData = new ArrayList<>();
    private PersonalLllegalWorkListAdapter adapter;
    private LllegalWorkPresenter presenter;
    private String id;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_carlllegalworklist;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this, rootView);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        tvLllegalCount.setVisibility(View.INVISIBLE);
        presenter = new LllegalWorkPresenter(this, (LllegalWorkActivity) getActivity());
        tvLllegalCount.setTextSize(13);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        initRecycler();
    }

    private void initRecycler() {
        adapter = new PersonalLllegalWorkListAdapter(R.layout.recyclerview_item_lllegals, listData);
        recyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LllegalworMsgBean msgDataBean = listData.get(position);
                String personalId = msgDataBean.getPersonalid();
                Intent intent = new Intent();
                intent.setClass(getActivity(), LllegalWorkDetailActivity.class);
                intent.putExtra("DetailInfos", msgDataBean);
                intent.putExtra("type", "personal");
                intent.putExtra("isVisible", "1");
                intent.putExtra("position", position);
                intent.putExtra("id", personalId);
                startActivity(intent);
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
    public void disposeLllegalWorkEvent(LllegalWorkEvent event) {
        switch (event.getWhat()) {
            case Constans.PERSONALLLEGAL_SSUCESS://展示
                rlNodata.setVisibility(View.INVISIBLE);
                llData.setVisibility(View.VISIBLE);
                List<LllegalworMsgBean> beanList = (List<LllegalworMsgBean>) event.getData();
                if (beanList.size() > 0) {
                    tvLllegalCount.setVisibility(View.VISIBLE);
                    showAdapterDatasList(beanList);
                } else {
                    showNodata();
                }
                break;

            case Constans.PERSONALLLEGAL_ERROR:
//                showNodata();
                showSuccessToast("加载失败,请重试");
                break;
            case Constans.PERSONAL_ID:
                id = (String) event.getData();
                initWeiguiData(id, Constans.PERSONAL_ID);
                break;
        }
    }

    private void showNodata() {
        llData.setVisibility(View.INVISIBLE);
        rlNodata.setVisibility(View.VISIBLE);
    }

    @Override
    protected void fetchData() {

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void initWeiguiData(String vid, int typeWhat) {
        if (presenter != null) {
            String pid = PreferencesUtils.getString(Objects.requireNonNull(getContext()), Constans.ORGID, "");
            String startTime = TimeUtils.getYesterdayStartTime();
            String endTime = TimeUtils.getCurrentTime();
            startTime = "2018-10-30 10:00:00";
            endTime = "2018-11-01 15:00:00";
            presenter.getWeigui(ApiService.GETWEIGUI, pid, startTime, endTime, vid, Constans.TYPE_ONE, typeWhat);
        }
    }

    /*展示数据*/
    @SuppressLint("SetTextI18n")
    private void showAdapterDatasList(List<LllegalworMsgBean> beanList) {
        listData.clear();
        listData.addAll(beanList);
        adapter.setNewData(listData);
        tvLllegalCount.setText("昨天有" + beanList.get(0).getRenshu() + "人违规作业");
    }


    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
