package com.yuefeng.features.ui.activity.video;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.babelstar.gviewer.NetClient;
import com.babelstar.gviewer.VideoView;
import com.common.base.codereview.BaseActivity;
import com.common.network.ApiService;
import com.common.utils.AppUtils;
import com.common.utils.Constans;
import com.common.utils.LogUtils;
import com.common.utils.PreferencesUtils;
import com.common.utils.ResourcesUtils;
import com.common.utils.ViewUtils;
import com.common.view.popuwindow.TreesListsPopupWindow;
import com.yuefeng.cartreeList.adapter.SimpleTreeRecyclerAdapter;
import com.yuefeng.cartreeList.common.Node;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.contract.VideolistVContract;
import com.yuefeng.features.event.LllegalWorkEvent;
import com.yuefeng.features.modle.video.ChangeVideoEquipmentDataBean;
import com.yuefeng.features.presenter.VideolistVPresenter;
import com.yuefeng.utils.VideoDatasUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/*视频监控*/
public class VideoCameraActivity extends BaseActivity implements VideolistVContract.View {

    private static final String TAG = "tag";
    @BindView(R.id.iv_back)
    RelativeLayout iv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.ll_problem)
    LinearLayout ll_problem;
    @BindView(R.id.ll_nodata)
    LinearLayout ll_nodata;
    @BindView(R.id.ll_video)
    LinearLayout ll_video;

    @BindView(R.id.tv_search_txt)
    TextView tvSearchTxt;

    @BindView(R.id.imageView1)
    VideoView mImageView1;
    @BindView(R.id.imageView2)
    VideoView mImageView2;
    @BindView(R.id.imageView3)
    VideoView mImageView3;
    @BindView(R.id.imageView4)
    VideoView mImageView4;

    private UpdateViewThread mUpdateViewThread = null;

    private List<ChangeVideoEquipmentDataBean> carListData = new ArrayList<>();
    private List<Node> carDatas = new ArrayList<>();
    private List<Node> carDatasSelect = new ArrayList<>();
    private VideolistVPresenter presenter;
    private TreesListsPopupWindow carListPopupWindow;
    private SimpleTreeRecyclerAdapter carlistAdapter;
    private String carNumber;
    private String terminal;
    private String mCarNum;


    @Override
    protected int getContentViewResId() {
        return R.layout.activity_videocamera;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        presenter = new VideolistVPresenter(this, this);
        initUI();
        carDatasSelect.clear();
    }

    private void initUI() {
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        terminal = (String) bundle.get("terminalNO");
        String type = (String) bundle.get("TYPE");
        assert type != null;
        assert terminal != null;
        if (type.equals("1")) {
            tvSearchTxt.setVisibility(View.GONE);
            tv_title.setText("车辆监控");
            ll_nodata.setVisibility(View.GONE);
            ll_video.setVisibility(View.VISIBLE);
            showVideoList(terminal);
        } else {
            tv_title.setText(R.string.video);
            getCarList();
            ll_nodata.setVisibility(View.VISIBLE);
            ll_video.setVisibility(View.GONE);
        }
    }

    /*车辆列表*/
    private void getCarList() {
        if (presenter != null) {
            String pid = PreferencesUtils.getString(this, "orgId", "");
            String userid = PreferencesUtils.getString(this, "id", "");
//            pid = "dg1954";
//            userid = "b91f05e4ffffffc901823b59d8146e3d";
            presenter.getVideoTree(ApiService.GETVIDEOTREE, pid, userid, "0");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void disposeLllegalWorkEvent(LllegalWorkEvent event) {
        switch (event.getWhat()) {
            case Constans.CARLIST_SSUCESS:
                carListData = (List<ChangeVideoEquipmentDataBean>) event.getData();
                if (carListData.size() > 0) {
                    showCarlistDatas(carListData);
                } else {
                    showSuccessToast("旗下无监控车辆");
                }
                break;
            default:
                break;

        }
    }

    /*展示数据*/
    private void showCarlistDatas(List<ChangeVideoEquipmentDataBean> organs) {
        try {
            carDatas.clear();
            carDatas = VideoDatasUtils.ReturnTreesDatas(organs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*车辆列表*/
    private void initCarlistPopupView() {
        if (carDatas.size() > 0) {
            carListPopupWindow = new TreesListsPopupWindow(this, carDatas, true, false);
            carListPopupWindow.setTitleText("车辆列表");
            carListPopupWindow.setSettingText(ResourcesUtils.getString(R.string.sure));

            carListPopupWindow.setOnItemClickListener(new TreesListsPopupWindow.OnItemClickListener() {
                @Override
                public void onGoBack(String name, String terminal, String id, boolean isGetDatas) {
                    mCarNum = name;
                    tv_title.setText(name);
                    if (isGetDatas) {
                        showVideoList(terminal);
                    }
                }

                @Override
                public void onSure(String name, String terminal, String id, boolean isGetDatas) {
                    mCarNum = name;
                    tv_title.setText(name);
                    if (isGetDatas) {
                        showVideoList(terminal);
                    }
                }

                @Override
                public void onSelectCar(String carNumber, String terminal, String id, boolean isGetDatas) {
//                    showVideoList(terminal);
                }
            });

            carListPopupWindow.showAtLocation(ll_problem, Gravity.BOTTOM | Gravity.CENTER, 0, 0);
        }
    }


    /*获取*/
    private void showVideoList(String terminal) {
        LogUtils.d(terminal + "terminal");
        /*测试*/
        if (TextUtils.isEmpty(terminal)) {
            showSuccessToast("终端号获取失败");
        } else {
            PreferencesUtils.putString(VideoCameraActivity.this, Constans.TERMINAL, terminal);
            initViewHightWith();

            ll_nodata.setVisibility(View.GONE);
            ll_video.setVisibility(View.VISIBLE);
            NetClient.Initialize();
            NetClient.SetDirSvr(ApiService.VIDEO_IP, ApiService.VIDEO_IP, 6605, 0);//114.55.118.196
            mImageView1.setViewInfo(terminal, terminal, 0, "CH1");
            mImageView1.StartAV();
            mImageView2.setViewInfo(terminal, terminal, 1, "CH2");
            mImageView3.setViewInfo(terminal, terminal, 2, "CH3");
            mImageView4.setViewInfo(terminal, terminal, 3, "CH4");
            mImageView2.StartAV();
            mImageView3.StartAV();
            mImageView4.StartAV();
            mUpdateViewThread = new UpdateViewThread();
            mUpdateViewThread.start();
        }
    }

    private void initViewHightWith() {

        int hight = (int) AppUtils.mScreenHeight / 4;
        ViewUtils.setIVHightOrWidth(mImageView1, hight, ActionBar.LayoutParams.MATCH_PARENT);
        ViewUtils.setIVHightOrWidth(mImageView2, hight, ActionBar.LayoutParams.MATCH_PARENT);
        ViewUtils.setIVHightOrWidth(mImageView3, hight, ActionBar.LayoutParams.MATCH_PARENT);
        ViewUtils.setIVHightOrWidth(mImageView4, hight, ActionBar.LayoutParams.MATCH_PARENT);
    }


    @Override
    public void initData() {
    }

    @Override
    protected void setLisenter() {

    }

    @Override
    protected void widgetClick(View v) {

    }

    @OnClick({R.id.tv_search_txt, R.id.imageView1, R.id.imageView2, R.id.imageView3, R.id.imageView4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_search_txt:
                initCarlistPopupView();
                break;
            case R.id.imageView1:
                jumpZooVideoCamera(0, "CH1");
                break;
            case R.id.imageView2:
                jumpZooVideoCamera(1, "CH2");
                break;
            case R.id.imageView3:
                jumpZooVideoCamera(2, "CH3");
                break;
            case R.id.imageView4:
                jumpZooVideoCamera(3, "CH4");
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void jumpZooVideoCamera(int channel, String channum) {
        String terminal = PreferencesUtils.getString(VideoCameraActivity.this, Constans.TERMINAL, "");
        Intent intent = new Intent();
        intent.setClass(VideoCameraActivity.this, VideoCameraZooActivity.class);
        intent.putExtra("mCarNum", mCarNum);
        intent.putExtra("tp", terminal);
        intent.putExtra("channel", channel);
        intent.putExtra("channum", channum);
        startActivity(intent);
    }

    public class UpdateViewThread extends Thread {
        private boolean isExit = false;
        private boolean isPause = false;

        public void setExit(boolean isExit) {
            this.isExit = isExit;
        }

        public void setPause(boolean isPause) {
            this.isPause = isPause;
        }

        public void run() {
            while (!isExit) {
                try {
                    if (!isPause) {
                        mImageView1.updateView();
                        mImageView2.updateView();
                        mImageView3.updateView();
                        mImageView4.updateView();
                        Thread.sleep(20);
                    } else {
                        Thread.sleep(100);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.isExit = true;
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        if (mUpdateViewThread != null) {
            mUpdateViewThread.setExit(true);
            mUpdateViewThread = null;
        }
        mImageView1.StopAV();
        mImageView2.StopAV();
        mImageView3.StopAV();
        mImageView4.StopAV();
        NetClient.UnInitialize();
        super.onDestroy();
    }
}
