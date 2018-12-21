package com.common.view.other;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.HorizontalScrollView;

/**
 * Created by Administrator on 2017/4/18.
 * 宽度可变的水平滚动HorizontalScrollView，这里最大宽度设置为屏幕的4/5
 */

public class WidthVariableScrollView extends HorizontalScrollView{

    private Context context ;
    private int maxWidth ;
    public WidthVariableScrollView(Context context) {
        super(context);
        this.context = context ;
    }

    public WidthVariableScrollView(Context context, AttributeSet attributeSet)
    {
        super(context,attributeSet);
        this.context = context ;
    }

    public WidthVariableScrollView(Context context, AttributeSet attributeSet,int defStyleAttr)
    {
        super(context,attributeSet, defStyleAttr);
        this.context = context ;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        //获取屏幕的最大宽度
        DisplayMetrics metric = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;
        maxWidth = 4*width/5;
        if(widthMode == MeasureSpec.AT_MOST && widthSize > maxWidth){
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(maxWidth, widthMode);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

}

