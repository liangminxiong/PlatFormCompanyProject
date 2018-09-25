package com.common.view.other;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yuefeng.commondemo.R;

import java.util.ArrayList;
import java.util.List;

/*仿淘宝评价*/

public class StarLinearLayout extends LinearLayout implements View.OnClickListener {

    /**
     * 星星之间的间距
     */
    private int mMargin = 10;
    /**
     * 是否可点击
     */
    private boolean isEdit;
    /**
     * 初始的值
     */
    private float mScore = 0;

    private List<ImageView> stars = new ArrayList<>();

    public StarLinearLayout(Context context) {
        this(context, null);
    }

    public StarLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StarLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.star);
            mMargin = (int) a.getDimension(R.styleable.star_margin, 10);
            isEdit = a.getBoolean(R.styleable.star_isEdit, false);
            mScore = a.getFloat(R.styleable.star_score, 0);
            a.recycle();
        }
        init();
        setScore(mScore);
    }

    private void init() {
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        params.rightMargin = mMargin;
        for (int i = 0; i < 5; i++) {
            ImageView star = new ImageView(getContext());
            star.setImageResource(R.mipmap.ic_star_select);
            stars.add(star);
            addView(star, params);

            star.setOnClickListener(this);
        }
    }

    public void setScore(float score) {
        if (score < 0 || score > 5) score = 0;
        mScore = score;
        setStar(((int) (10 * score)) / 5);
    }

    public float getScore() {
        return mScore;
    }

    private void setStar(int level) {
        int i;
        for (i = 0; i < level / 2; i++) {
            stars.get(i).setImageResource(R.mipmap.ic_star_select);
        }
        if (level % 2 > 0) {
            stars.get(i).setImageResource(R.mipmap.ic_star_select);
            i++;
        }
        for (; i < stars.size(); i++) {
            stars.get(i).setImageResource(R.mipmap.ic_star_unselect);
        }
    }

    @Override
    public void onClick(View v) {
        if (stars.contains(v)) {
            if (!isEdit) return;
            int index = stars.indexOf(v);
            setScore(index + 1);
            changeListener.Change(index + 1);
        }
    }

    ChangeListener changeListener;

    // 为每个接口设置监听器
    public void setChangeListener(ChangeListener change) {
        this.changeListener = change;
    }

    public interface ChangeListener {

        void Change(int level);

    }
}
