package com.yuefeng.photo.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.yuefeng.commondemo.R;
import com.yuefeng.photo.bean.ImageInfo;
import com.yuefeng.photo.view.photoView.PhotoView;
import com.yuefeng.photo.view.photoView.PhotoViewAttacher;

import java.util.ArrayList;
import java.util.List;

/**
 * 主界面点击发布，弹出半透明对话框
 * Created by xiaoke on 2016/4/28.
 */
public class PicShowDialog extends Dialog {
    private Context context;
    private View view;
    private List<ImageInfo> imageInfos;
    private MyViewPager vp;
    private List<View> views = new ArrayList<View>();
    private LayoutAnimationController lac;
    private LinearLayout ll_point;
    private ViewPagerAdapter pageAdapter;
    private int position;
    private LinearLayout.LayoutParams paramsL = new LinearLayout.LayoutParams(10, 10);
    // 图片缓存 默认 等
    private DisplayImageOptions optionsImag = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.mipmap.zanwutupian)
            .showImageOnFail(R.mipmap.zanwutupian)
            .cacheInMemory(true)
            .cacheOnDisc(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565).build();

    public PicShowDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }


    public PicShowDialog(Context context, List<ImageInfo> imageInfos, int position) {
        this(context, R.style.Pic_Dialog);
        this.imageInfos = imageInfos;
        this.position = position;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_dialog_pic);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        vp = (MyViewPager) findViewById(R.id.vp);
        ll_point = (LinearLayout) findViewById(R.id.ll_point);
        initMyPageAdapter();
        vp.setCurrentItem(position);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int position) {
                if (views.size() != 0 && views.get(position) != null) {
                    for (int i = 0; i < views.size(); i++) {
                        if (i == position) {
                            views.get(i).setBackgroundResource(R.drawable.point_focus2);
                        } else {
                            views.get(i).setBackgroundResource(R.drawable.point_normal2);
                        }
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    /***
     * 初始化viewpager适配器
     */

    private void initMyPageAdapter() {
        initPoint();
        if (pageAdapter == null) {
            pageAdapter = new ViewPagerAdapter();
            if (vp != null) {
                vp.setAdapter(pageAdapter);
            }

        } else {
            pageAdapter.notifyDataSetChanged();
        }
    }

    private void initPoint() {
        views.clear();
        ll_point.removeAllViews();
        if (imageInfos.size() == 1) {
            ll_point.setVisibility(View.GONE);
        } else {
            for (int i = 0; i < imageInfos.size(); i++) {
                View view = new View(context);
                paramsL.setMargins(dip2px(context, 5), dip2px(context, 2), 0, dip2px(context, 5));
                view.setLayoutParams(paramsL);
                if (i == position) {
                    view.setBackgroundResource(R.drawable.point_focus2);
                } else {
                    view.setBackgroundResource(R.drawable.point_normal2);
                }

                views.add(view);
                ll_point.addView(view);
            }
        }

    }

    private class ViewPagerAdapter extends PagerAdapter {


        @Override
        public int getCount() {
            return imageInfos.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = View.inflate(context, R.layout.item_pic_show, null);
            PhotoView photoView = (PhotoView) view.findViewById(R.id.pic_pv);
            ImageLoader.getInstance().displayImage(imageInfos.get(position).getUrl(), photoView, optionsImag);
            photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    dismiss();
                }
            });
            ((ViewPager) container).addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}

