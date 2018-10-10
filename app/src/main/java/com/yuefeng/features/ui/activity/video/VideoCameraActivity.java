package com.yuefeng.features.ui.activity.video;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.common.utils.StatusBarUtil;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.ui.view.VideoPopupWindow;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/*单独视频*/
public class VideoCameraActivity extends BaseActivity {

    private static final String TAG = "tag";
    @BindView(R.id.iv_back)
    RelativeLayout iv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.ll_problem)
    LinearLayout ll_problem;

    @BindView(R.id.imageView1)
    VideoView mImageView1;
    @BindView(R.id.imageView2)
    VideoView mImageView2;
    @BindView(R.id.imageView3)
    VideoView mImageView3;
    @BindView(R.id.imageView4)
    VideoView mImageView4;

    private UpdateViewThread mUpdateViewThread = null;
    private UpdateViewThreadOne mUpdateViewThreadOne = null;
    private String num;
    private VideoPopupWindow popupWindow;


    @Override
    protected int getContentViewResId() {
        return R.layout.activity_videocamera;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        View view = findViewById(R.id.space);
        view.setBackground(mActivity.getResources().getDrawable(R.drawable.title_toolbar_bg_blue));
        StatusBarUtil.setFadeStatusBarHeight(mActivity, view);
        tv_title.setText(R.string.video);

        getVideoList();
    }

    /*获取*/
    private void getVideoList() {
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        num = bundle.getString("tp");
        Log.v("tag", "  videoterminalNO " + num);
        if (TextUtils.isEmpty(num)) {
            showSuccessToast("终端号获取失败");
        } else {

            NetClient.Initialize();
            NetClient.SetDirSvr(ApiService.VIDEO_IP, ApiService.VIDEO_IP, 6605, 0);//114.55.118.196
            mImageView1.setViewInfo(num, num, 0, "CH1");
            mImageView1.StartAV();
            mImageView2.setViewInfo(num, num, 1, "CH2");
            mImageView3.setViewInfo(num, num, 2, "CH3");
            mImageView4.setViewInfo(num, num, 3, "CH4");
            mImageView2.StartAV();
            mImageView3.StartAV();
            mImageView4.StartAV();
            mUpdateViewThread = new UpdateViewThread();
            mUpdateViewThread.start();
        }
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

    @OnClick({R.id.imageView1, R.id.imageView2, R.id.imageView3, R.id.imageView4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
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
//        if (!TextUtils.isEmpty(num)) {
//            Intent intent = new Intent(VideoCameraActivity.this, VideoCameraZooActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putString("tp", num);
//            bundle.putInt("channel", channel);
//            bundle.putString("channum", channum);
//            intent.putExtras(bundle);
//            startActivity(intent);
//        }
        popupWindow = new VideoPopupWindow(this);
        popupWindow.videoview.setViewInfo(num, num, channel, channum);
        popupWindow.videoview.StartAV();
        mUpdateViewThreadOne = new UpdateViewThreadOne();
        mUpdateViewThreadOne.start();
        popupWindow.setOnItemClickListener(new VideoPopupWindow.OnItemClickListener() {
            @Override
            public void onVideoDismiss() {
                mUpdateViewThreadOne.setExit(true);
                mUpdateViewThreadOne = null;
                popupWindow.videoview.StopAV();
                popupWindow.dismiss();
            }
        });

        popupWindow.showAtLocation(ll_problem, Gravity.BOTTOM | Gravity.CENTER, 0, 0);
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

    public class UpdateViewThreadOne extends Thread {
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
                        if (popupWindow != null) {
                            popupWindow.videoview.updateView();
                        }
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
        mUpdateViewThread.setExit(true);
        mUpdateViewThread = null;
        mImageView1.StopAV();
        mImageView2.StopAV();
        mImageView3.StopAV();
        mImageView4.StopAV();
        NetClient.UnInitialize();
        super.onDestroy();
    }
}
