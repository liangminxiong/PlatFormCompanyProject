package com.yuefeng.features.ui.activity.video;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;

import com.babelstar.gviewer.NetClient;
import com.babelstar.gviewer.VideoView;
import com.common.base.codereview.BaseActivity;
import com.common.network.ApiService;
import com.yuefeng.commondemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class VideoCameraZooActivity extends BaseActivity {
    @BindView(R.id.imageView)
    VideoView mImageView;

    private UpdateViewThread mUpdateViewThread = null;
    private String num;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.avtivity_videocamerazoo;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        String mCarNum = bundle.getString("mCarNum");
        tv_title.setText(mCarNum);
        num = bundle.getString("tp");
        int channel = bundle.getInt("channel");
        String channum = bundle.getString("channum");
        if (TextUtils.isEmpty(num)) {
            showSuccessToast("终端号获取失败");
        } else {
            NetClient.Initialize();
            NetClient.SetDirSvr(ApiService.VIDEO_IP, ApiService.VIDEO_IP, 6605, 0);//114.55.118.196
            mImageView.setViewInfo(num, num, channel, channum);
            mImageView.StartAV();
            mUpdateViewThread = new UpdateViewThread();
            mUpdateViewThread.start();
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setLisenter() {

    }

    @Override
    protected void widgetClick(View v) {

    }

    @Override
    protected void onDestroy() {
        mUpdateViewThread.setExit(true);
        mUpdateViewThread = null;
        mImageView.StopAV();
        NetClient.UnInitialize();
        super.onDestroy();
    }


    public class UpdateViewThread extends Thread {
        private boolean isExit = false;
        private boolean isPause = false;

        void setExit(boolean isExit) {
            this.isExit = isExit;
        }

        public void setPause(boolean isPause) {
            this.isPause = isPause;
        }

        public void run() {
            while (!isExit) {
                try {
                    if (!isPause) {
                        mImageView.updateView();
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
}
