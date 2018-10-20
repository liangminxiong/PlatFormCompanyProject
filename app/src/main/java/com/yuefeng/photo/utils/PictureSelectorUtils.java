package com.yuefeng.photo.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

/*图片选择*/
public class PictureSelectorUtils {

    private FragmentActivity fragmentActivity;
    public int type = PictureMimeType.ofImage();
    private static StringBuffer mStringBuffer = null;
    private static String strImages;

    /**
     * 单例
     *
     * @return
     */
    public static PictureSelectorUtils getInstance() {
        return PictureSelectorUtils.LocationHolder.INSTANCE;
    }

    private static class LocationHolder {
        @SuppressLint("StaticFieldLeak")
        private static final PictureSelectorUtils INSTANCE = new PictureSelectorUtils();
    }

    public void onAcAlbum(Activity activity, int type, int maxSelectNum, int imageSpanCount,
                          boolean previewImage, String path, List<LocalMedia> selectList) {
//
//        path = ImageUtils.getPath();
        PictureSelector.create(activity)
                .openGallery(type)// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .maxSelectNum(maxSelectNum)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(imageSpanCount)// 每行显示个数
                .selectionMode(PictureConfig.MULTIPLE)// 多选 SINGLE or 单选MULTIPLE
                .previewImage(previewImage)// 是否可预览图片
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .compress(true)// 是否压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .compressSavePath(path)//压缩图片保存地址
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .selectionMedia(selectList)// 是否传入已选图片
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    public void onAcCamera(Activity activity, int type, int maxSelectNum, List<LocalMedia> selectList) {
        PictureSelector.create(activity)
                .openCamera(type)// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .maxSelectNum(maxSelectNum)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .previewImage(true)// 是否可预览图片
                .compress(true)// 是否压缩
                .glideOverride(100, 100)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(16, 9)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .selectionMedia(selectList)// 是否传入已选图片
                .previewEggs(false)//预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    public void onFgAlbum(int type, int maxSelectNum, int imageSpanCount,
                          boolean previewImage, String path, List<LocalMedia> selectList) {
//        type = PictureMimeType.ofImage();
//        path = ImageUtils.getPath();
        PictureSelector.create(fragmentActivity)
                .openGallery(type)// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .maxSelectNum(maxSelectNum)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(imageSpanCount)// 每行显示个数
                .selectionMode(PictureConfig.MULTIPLE)// 多选 SINGLE or 单选MULTIPLE
                .previewImage(previewImage)// 是否可预览图片
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .compress(true)// 是否压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .compressSavePath(path)//压缩图片保存地址
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .selectionMedia(selectList)// 是否传入已选图片
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    public void onFgCamera(int type, int maxSelectNum, List<LocalMedia> selectList) {
        PictureSelector.create(fragmentActivity)
                .openCamera(type)// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .maxSelectNum(maxSelectNum)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .previewImage(true)// 是否可预览图片
                .compress(true)// 是否压缩
                .glideOverride(100, 100)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(16, 9)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .selectionMedia(selectList)// 是否传入已选图片
                .previewEggs(false)//预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }


    /*图片压缩100k内*/
    public static String compressionPhotos(final Context context, final List<LocalMedia> dataList, final String string) {
        if (mStringBuffer == null) {
            mStringBuffer = new StringBuffer();
        }
        mStringBuffer.setLength(0);
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.size() < 2) {
                mStringBuffer.append(ImageHelper.getimage(context, dataList.get(i).getCompressPath(), string));
            } else {
                if (i == 0) {
                    mStringBuffer.append(ImageHelper.getimage(context, dataList.get(i).getCompressPath(), string));
                } else {
                    mStringBuffer.append(",").append(ImageHelper.getimage(context, dataList.get(i).getCompressPath(), string));
                }
            }
        }
        strImages = "";
        strImages = mStringBuffer.toString();
        return strImages;
    }

}
