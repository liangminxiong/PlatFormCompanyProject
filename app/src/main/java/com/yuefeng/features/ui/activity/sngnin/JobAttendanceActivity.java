package com.yuefeng.features.ui.activity.sngnin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.common.base.codereview.BaseActivity;
import com.common.utils.AppUtils;
import com.common.utils.Constans;
import com.common.utils.TimeUtils;
import com.common.utils.ViewUtils;
import com.luck.picture.lib.permissions.RxPermissions;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.contract.JobAttendanceContract;
import com.yuefeng.features.event.JobAttendanceEvent;
import com.yuefeng.features.presenter.JobAttendancePresenter;
import com.yuefeng.utils.BdLocationUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/*作业考勤*/
public class JobAttendanceActivity extends BaseActivity implements JobAttendanceContract.View {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_chidao)
    TextView tvChidao;
    @BindView(R.id.tv_zaotui)
    TextView tvZaotui;
    @BindView(R.id.tv_nosignin)
    TextView tvNosignin;
    @BindView(R.id.tv_nosignoff)
    TextView tvNosignoff;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_sngnin_count)
    TextView tvSngninCount;
    @BindView(R.id.tv_personl_count)
    TextView tvPersonlCount;
    @BindView(R.id.tv_sp_count)
    TextView tvSpCount;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.iv_personl_sngnin)
    ImageView ivPersonlSngnin;
    @BindView(R.id.iv_sp_sngnin)
    ImageView ivSpSngnin;
    @BindView(R.id.ll_bg)
    LinearLayout llBg;
    private double latitude;
    private double longitude;
    private String address;
    private boolean isFirstLocation = true;
    private JobAttendancePresenter presenter;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_jobattendance;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initUI();
        presenter = new JobAttendancePresenter(this, this);
        isFirstLocation = true;
        requestPermissions();
    }

    private void initUI() {
        ViewUtils.setLLHightOrWidth(llBg, (int) AppUtils.mScreenHeight * 2 / 5, ActionBar.LayoutParams.MATCH_PARENT);
        tvTitle.setText("作业考勤");
        tvChidao.setText("4");
        tvZaotui.setText("2");
        tvNosignin.setText("1");
        tvNosignoff.setText("0");

        tvSngninCount.setText("4");
        tvPersonlCount.setText("2");
        tvSpCount.setText("2");
    }

    /**
     * 百度地图定位的请求方法 拿到国、省、市、区、地址
     */
    @SuppressLint("CheckResult")
    private void requestPermissions() {
        RxPermissions rxPermission = new RxPermissions(this);
        rxPermission.request(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (!granted) {
                            showSuccessToast("App未能获取相关权限，部分功能可能不能正常使用.");
                        }
                        getLocation();
                    }
                });
    }

    private void getLocation() {
        BdLocationUtil.getInstance().requestLocation(new BdLocationUtil.MyLocationListener() {
            @Override
            public void myLocation(BDLocation location) {
                if (location == null) {
                    return;
                }
                if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    address = location.getAddrStr();

                    int length = address.length();
                    address = address.substring(2, length);
                    if (isFirstLocation) {
                        isFirstLocation = false;
                        if (!TextUtils.isEmpty(address)) {
                            tvAddress.setText(address);
                        } else {
                            isFirstLocation = true;
                            tvAddress.setText("点击重新定位");
                        }
                    }
                }
            }
        }, Constans.BDLOCATION_TIME);
    }

    @Override
    protected void initData() {
        initTime();
    }

    @SuppressLint("SetTextI18n")
    private void initTime() {
        tvTime.setText("今天：" + TimeUtils.getCurrentTime2());
    }

    @Override
    protected void setLisenter() {

    }

    @Override
    protected void widgetClick(View v) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void disposeJobAttendanceEvent(JobAttendanceEvent event) {
        switch (event.getWhat()) {
            case Constans.SNGNIN_SSUCESS://成功
                break;

            default:
//                showSuccessToast("获取数据失败，请重试");
                break;

        }
    }


    @OnClick({R.id.tv_back, R.id.tv_address, R.id.iv_personl_sngnin, R.id.iv_sp_sngnin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_address:
                if (isFirstLocation) {
                    requestPermissions();
                }
                break;
            case R.id.iv_personl_sngnin:
                showSuccessToast("Personal");
                break;
            case R.id.iv_sp_sngnin:
                startActivity(new Intent(JobAttendanceActivity.this,SupervisorSngnInActivity.class));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
