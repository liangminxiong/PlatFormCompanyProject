package com.common.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.yuefeng.ui.MyApplication;

import java.security.MessageDigest;

/**
 * Glide图片加载
 */

public class GlideUtils {
    /*
     * 正常加载
     * */
    public static void loadImageview(ImageView imageview, String path) {
        Glide.with(MyApplication.getContext()).load(path).into(imageview);
    }

    /*
     *
     * 加载带有占位图的view
     * */
    public static void loadImageViewLoading(ImageView imageView, Object path, int errorPath, int loadingPath) {
        RequestOptions requestOptions = new RequestOptions();
        Glide.with(MyApplication.getContext()).load(path)
                .apply(requestOptions.placeholder(loadingPath).error(errorPath))
                .into(imageView);
    }

    /*
     * 加载指定大小的image
     * */
    public static void loadImageViewSize(ImageView imageView, String path, int loadingPath, int errorPath, int width, int height) {
        RequestOptions requestOptions = new RequestOptions();
        Glide.with(MyApplication.getContext()).load(path).apply(requestOptions.override(width, height)
                .placeholder(loadingPath).error(errorPath))
                .into(imageView);
    }

    /*
     * 裁剪大小，设置缓存模式，防止oom(不缓存)
     *
     * */
    public static void loadImageViewOOM(ImageView imageView, int loadingPath, int errorPath, String path, int height, int width) {
        RequestOptions requestOptions = new RequestOptions();
        Glide.with(MyApplication.getContext()).load(path)
                .apply(requestOptions.override(width, height).skipMemoryCache(true)
                        .placeholder(loadingPath).error(errorPath))
                .into(imageView);
    }

    /*
     * 加载圆形图片
     * */
    public static void loadImageViewCircle(ImageView imageView, String path, int loadingPath, int errorPath) {
        RequestOptions requestOptions = new RequestOptions();
        Glide.with(AppUtils.getContext()).asBitmap().load(path)
                .apply(requestOptions.skipMemoryCache(true).optionalCircleCrop()
                        .placeholder(loadingPath).error(errorPath))
                .into(imageView);
    }

    /*
     * 加载圆角图片
     * */
    public static void loadRectImageView(ImageView imageView, String path, int dp) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
//                .placeholder(R.mipmap.ic_launcher)
                .priority(Priority.HIGH)
                .transform(new GlideRoundTransform(dp));
        Glide.with(AppUtils.getContext()).load(path).apply(options).into(imageView);
    }

    static class GlideRoundTransform extends BitmapTransformation {
        private float radius = 0f;

        public GlideRoundTransform() {
            this(4);
        }

        public GlideRoundTransform(int dp) {
            super();
            this.radius = Resources.getSystem().getDisplayMetrics().density * dp;
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return roundCrop(pool, toTransform);
        }

        private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
            canvas.drawRoundRect(rectF, radius, radius, paint);
            return result;
        }

        @Override
        public void updateDiskCacheKey(MessageDigest messageDigest) {

        }
    }
}
