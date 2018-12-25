package com.common.view.dialog;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.yuefeng.commondemo.R;


/**
 * 打卡
 */

public class SigninCacheSureDialog extends Dialog {

    private TextView cancle, sure, tv_time, tv_address, tv_time_down;

    public SigninCacheSureDialog(@NonNull Context context) {
        super(context, R.style.DialogStyle);
        setContentView(R.layout.dialog_signin);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setCancelable(true);
        initView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        this.getWindow().setDimAmount(0f);
    }

    private void initView() {
        tv_time = findViewById(R.id.tv_phone);
        tv_address = findViewById(R.id.tv_address);
        tv_time_down = findViewById(R.id.tv_time_down);
        cancle = findViewById(R.id.tv_cancel);
        sure = findViewById(R.id.tv_sure);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deletaCacheListener != null) {
                    deletaCacheListener.cancle();
                }
                dismiss();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deletaCacheListener != null) {
                    deletaCacheListener.sure();
                }
                dismiss();
            }
        });
    }


    public void setTextTime(String txt) {
        this.tv_time.setText(txt);
    }

    public void setTextAddress(String txt) {
        this.tv_address.setText(txt);
    }

    public void setTextTimeDown(String txt) {
        this.tv_time_down.setText(txt);
    }

    public void setCancleGone() {
        this.cancle.setVisibility(View.GONE);
    }

    private DeletaCacheListener deletaCacheListener;

    public void setDeletaCacheListener(DeletaCacheListener listener) {
        this.deletaCacheListener = listener;
    }

    public interface DeletaCacheListener {
        void sure();

        void cancle();
    }
}
