package com.common.view.dialog;


import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.common.utils.AppUtils;
import com.common.utils.ViewUtils;
import com.yuefeng.commondemo.R;


/**
 */

public class ReplyEditCancelSureDialog extends Dialog {

    private EditText mEditText;
    private TextView cancle, sure;
    private String mContent;

    public ReplyEditCancelSureDialog(@NonNull Context context) {
        super(context, R.style.DialogStyle);
        setContentView(R.layout.dialog_reply);
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
        mEditText = findViewById(R.id.tv_title);
        ViewUtils.setEditHightOrWidth(mEditText, (int) (AppUtils.mScreenHeight / 5), ActionBar.LayoutParams.MATCH_PARENT);
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


    public String getContent() {
        if (mEditText != null) {
            mContent = mEditText.getText().toString().trim();
        }

        return mContent;
    }

    public void setCancleGone() {
        this.cancle.setVisibility(View.GONE);
    }

    private ReplyCancelSureListenner deletaCacheListener;

    public void setReplyCancelListener(ReplyCancelSureListenner listener) {
        this.deletaCacheListener = listener;
    }

    public interface ReplyCancelSureListenner {
        void sure();

        void cancle();
    }
}
