package com.common.view.popuwindow;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yuefeng.commondemo.R;


/**
 * 树形列表
 * <p>
 */

public class TreesListsPopupWindow extends PopupWindow {
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    private LinearLayoutCompat llPopupRoot;
    private boolean isShowAniming;//show动画是否在执行中
    private boolean isHideAniming;//hide动画是否在执行中
    private View rootView;
    private TextView tv_title;
    private TextView tv_setting;

    public TreesListsPopupWindow(Context context) {
        super(null, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mContext = context;
        //设置点击空白处消失
        setTouchable(true);
        setOutsideTouchable(true);
        setClippingEnabled(false);

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        int w = wm.getDefaultDisplay().getWidth();
        int h = wm.getDefaultDisplay().getHeight();
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(Color.WHITE);//填充颜色
        setBackgroundDrawable(new BitmapDrawable(context.getResources(), bitmap));

        initView();
    }

    private void initView() {
        rootView = LayoutInflater.from(mContext).inflate(R.layout.layout_popupwindow_treeslist, null);
        setContentView(rootView);

        llPopupRoot = (LinearLayoutCompat) rootView.findViewById(R.id.ll_popup_root);
        RelativeLayout iv_back = (RelativeLayout) rootView.findViewById(R.id.iv_back);
        tv_title = (TextView) rootView.findViewById(R.id.tv_title);
        tv_setting = (TextView) rootView.findViewById(R.id.tv_title_setting);

//        rootView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isShowing())
//                    dismiss();
//            }
//        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onGoBack();
            }
        });

//        btnPhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mOnItemClickListener != null)
//                    mOnItemClickListener.onPhotoClicked();
//            }
//        });

        tv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onSure();
            }
        });
    }

    public void setTitleText(String titleText) {
        if (tv_title != null) {
            tv_title.setText(titleText);
        }
    }

    public void setSettingText(String setting) {
        if (tv_setting != null) {
            tv_setting.setText(setting);
        }
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        if (!isShowAniming) {
            isShowAniming = true;
            popupAnim(llPopupRoot, 0.0f, 1.0f, 300, true);
        }
    }

    @Override
    public void dismiss() {
        if (!isHideAniming) {
            isHideAniming = true;
            popupAnim(llPopupRoot, 1.0f, 0.0f, 300, false);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onGoBack();

//        void onPhotoClicked();

        void onSure();
    }

    /**
     * popupWindow属性动画
     *
     * @param view     执行属性动画的view
     * @param start    start值
     * @param end      end值
     * @param duration 动画持续时间
     * @param flag     true代表show，false代表hide
     */
    private void popupAnim(final View view, float start, final float end, int duration, final
    boolean flag) {
        ValueAnimator va = ValueAnimator.ofFloat(start, end).setDuration(duration);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                view.setPivotX(0);
                view.setPivotY(view.getMeasuredHeight());
                view.setTranslationY((1 - value) * view.getHeight());
            }
        });
        va.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                if (!flag) {
                    isHideAniming = false;
                    TreesListsPopupWindow.super.dismiss();
                } else {
                    isShowAniming = false;
                }
            }
        });
        va.start();
    }
}
