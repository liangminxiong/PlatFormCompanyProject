package com.yuefeng.features.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.common.utils.ResourcesUtils;
import com.yuefeng.commondemo.R;


/**
 * Created by Horrarndoo on 2017/10/7.
 * <p>
 */

public class MsgCollectionPopupWindow extends PopupWindow {
    private Context mContext;
    private LinearLayoutCompat llPopupRoot;
    private boolean isShowAniming;//show动画是否在执行中
    private boolean isHideAniming;//hide动画是否在执行中
    public RecyclerView recyclerview;
    private View view;

    public MsgCollectionPopupWindow(Context context) {
        super(context);
        initView(context);
        this.mContext = context;

    }

    @SuppressLint("InflateParams")
    private void initView(Context context) {

        view = ResourcesUtils.inflate(R.layout.layout_listview);
        llPopupRoot = (LinearLayoutCompat) view.findViewById(R.id.ll_popup_root);
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowing())
                    dismiss();
            }
        });
        setPopView();
    }

    private void setPopView() {
        setContentView(view);
        setOutsideTouchable(true);//设置允许在外点击消失
        setFocusable(true);
        setTouchable(true);
        setBackgroundDrawable(new ColorDrawable());
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public void showPopuWindow(View parent) {
//        showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        showAsDropDown(parent);

    }

    /*@Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        if (!isShowAniming) {
            isShowAniming = true;
            popupAnim(llPopupRoot, 1.0f, 0.0f, 300, true);
        }
    }

    @Override
    public void dismiss() {
        if (!isHideAniming) {
            isHideAniming = true;
            popupAnim(llPopupRoot, 0.0f, 1.0f, 300, false);
        }
    }

    *//**
     * popupWindow属性动画
     *
     * @param view     执行属性动画的view
     * @param start    start值
     * @param end      end值
     * @param duration 动画持续时间
     * @param flag     true代表show，false代表hide
     *//*
    private void popupAnim(final View view, float start, final float end, int duration, final
    boolean flag) {
        ValueAnimator va = ValueAnimator.ofFloat(start, end).setDuration(duration);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                LogUtils.d("popuAnim == " + value);
                view.setPivotX(1);
                view.setPivotY(view.getMeasuredHeight());
//                view.setTranslationY((1 - value) * view.getHeight());
                view.setTranslationY(value * view.getHeight());
            }
        });
        va.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                if (!flag) {
                    isHideAniming = false;
                    MsgCollectionPopupWindow.super.dismiss();
                } else {
                    isShowAniming = false;
                }
            }
        });
        va.start();
    }*/
}
