package com.common.view.dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yuefeng.commondemo.R;


/**
展示问题
 */

public class ShowProblempop extends PopupWindow {

    private View view;
    private TextView tv_item_name,tv_item_chiwei,tv_item_track;
    private TextView tv_item_phone, tv_item_time;
    private TextView tv_item_class, tv_item_type;
    private TextView tv_item_address;

    public ShowProblempop(Context context) {
        super(context);
        initView(context);
        setPopView();
    }

    private void setPopView() {
        setContentView(view);
        setOutsideTouchable(true);
        setFocusable(true);
        setTouchable(true);
        setBackgroundDrawable(new ColorDrawable());
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
    }

    private void initView(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.pop_show_problem, null);
        tv_item_name = view.findViewById(R.id.tv_item_title);
        tv_item_chiwei = view.findViewById(R.id.tv_item_chiwei);


//        tv_item_phone = view.findViewById(R.id.tv_item_phone);
        tv_item_time = view.findViewById(R.id.tv_item_time);

        tv_item_class = view.findViewById(R.id.tv_item_class);
        tv_item_type = view.findViewById(R.id.tv_item_type);

        tv_item_address = view.findViewById(R.id.tv_item_address);


        tv_item_track = view.findViewById(R.id.tv_item_track);
        tv_item_track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (takePhotoTouch != null) {
                    takePhotoTouch.takeTrack();
                }
//                dismiss();
            }
        });
//        tv_item_track.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (takePhotoTouch != null) {
//                    takePhotoTouch.takeNativ();
//                }
//                dismiss();
//            }
//        });

    }

    public void setTextContent(String problemID,String type,int color,String des,
                               String persoanl,String time,String address){
        tv_item_name.setText(problemID);
        tv_item_chiwei.setText(type);
        tv_item_chiwei.setTextColor(color);
//        tv_item_phone.setText(phone);
        tv_item_class.setText(des);
        tv_item_type.setText(persoanl);
        tv_item_time.setText(time);
        tv_item_address.setText(address);
    }

    public void showTakePop(View parent){
        showAtLocation(parent, Gravity.BOTTOM,0,0);
    }
    private TakePhotoTouch takePhotoTouch;

    public void setTakePhotoTouch(TakePhotoTouch touch) {
        this.takePhotoTouch = touch;
    }

    public interface TakePhotoTouch {
        void takeTrack();//轨迹

        void takeNativ();//导航

    }
}
