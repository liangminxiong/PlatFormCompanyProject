package com.yuefeng.photo.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;

import com.common.utils.Constans;
import com.common.utils.FileUtils;
import com.common.utils.ImageUtils;
import com.common.utils.StringUtils;
import com.common.utils.ToastUtils;
import com.common.view.popuwindow.CameraPhotoPopupWindow;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.yuefeng.commondemo.R;
import com.yuefeng.photo.adapter.GridImageAdapter;
import com.yuefeng.photo.other.FullyGridLayoutManager;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/*图片选择*/
public class PictureSelectorUtils {

    private FragmentActivity fragmentActivity;
    public int type = PictureMimeType.ofImage();
    private static StringBuffer mStringBuffer = null;
    private static String strImages;
    private GridImageAdapter mAdapter;
    private CameraPhotoPopupWindow mPopupWindow;
    private Context mContext;
    private String mImagesArrays;
    private List<LocalMedia> mSelectList;

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

    /*图片选择*/
    public void initSselectPhoto(final Context context, final View view, RecyclerView recyclerView,
                                  final List<LocalMedia> selectList) {
        FullyGridLayoutManager manager = new FullyGridLayoutManager(context,
                Constans.FOUR, GridLayoutManager.VERTICAL, false);
        this.mContext = context;
        this.mSelectList = selectList;
        recyclerView.setLayoutManager(manager);
        mAdapter = new GridImageAdapter(context, new GridImageAdapter.onAddPicClickListener() {
            @Override
            public void onAddPicClick() {
                initPopupView(context, view, selectList);
            }
        });
        mAdapter.setList(selectList);
        mAdapter.setSelectMax(Constans.FOUR);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            // 预览图片 可自定长按保存路径
                            //PictureSelector.create(MainActivity.this).externalPicturePreview(position, "/custom_file", mSelectList);
                            PictureSelector.create((Activity) context).externalPicturePreview(position, selectList);
                            break;
                    }
                }
            }
        });
        delectSelectPhotos(context);
    }

    /*拍照，图片*/
    private void initPopupView(final Context context, View view, final List<LocalMedia> selectList) {
        mPopupWindow = new CameraPhotoPopupWindow(context);
        mPopupWindow.setOnItemClickListener(new CameraPhotoPopupWindow.OnItemClickListener() {
            @Override
            public void onCaremaClicked() {
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
//                onCarema();
                PictureSelectorUtils.getInstance().onAcCamera((Activity) context,
                        PictureSelectorUtils.getInstance().type, Constans.FOUR, selectList);
            }

            @Override
            public void onPhotoClicked() {
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
//                onPhoto();
                PictureSelectorUtils.getInstance().onAcAlbum((Activity) context,
                        PictureSelectorUtils.getInstance().type, Constans.FOUR, Constans.FOUR,
                        true, ImageUtils.getPath(), selectList);
            }

            @Override
            public void onCancelClicked() {
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
            }
        });
        mPopupWindow.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /*相册*/
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

    /*拍照*/
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

    public static String getFileSize(List<LocalMedia> dataList) {
        double filesSize = 0;
        for (LocalMedia localMedia : dataList) {
            String path = localMedia.getCompressPath();
            filesSize = filesSize + FileUtils.getFileOrFilesSize(path, FileUtils.SIZETYPE_KB);
        }

        return StringUtils.getStringDistance(filesSize);
    }


    // 清空图片缓存，包括裁剪、压缩后的图片 注意:必须要在上传完成后调用 必须要获取权限
    private void delectSelectPhotos(final Context context) {
        RxPermissions permissions = new RxPermissions((Activity) context);
        permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    PictureFileUtils.deleteCacheDirFile(context);
                } else {
                    ToastUtils.showToast(R.string.picture_jurisdiction);
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });
    }

    /*展示图片*/
    @SuppressLint("SetTextI18n")
    public List<LocalMedia> getActivityResult(Intent data) {
        try {
            // 图片选择结果回调
            mSelectList = PictureSelector.obtainMultipleResult(data);
            // 例如 LocalMedia 里面返回三种path
            // 1.media.getPath(); 为原图path
            // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
            // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
            // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
            if (mSelectList.size() <= 0) {
                return mSelectList;
            }
            if (mAdapter != null) {
                mAdapter.setList(mSelectList);
                mAdapter.notifyDataSetChanged();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mSelectList;
    }

    public String getImagesArrays(final Context context, final List<LocalMedia> selectList, final String address) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                mImagesArrays = PictureSelectorUtils.compressionPhotos(context, selectList, address);
            }
        }).start();
        return mImagesArrays;
    }
}
