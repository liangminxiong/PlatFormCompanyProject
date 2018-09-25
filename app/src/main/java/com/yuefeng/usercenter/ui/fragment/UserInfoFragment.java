package com.yuefeng.usercenter.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.base.BaseMvpFragment;
import com.common.event.ReloginEvent;
import com.common.utils.AppManager;
import com.common.utils.Constans;
import com.common.utils.DataCleanManager;
import com.common.utils.FileUtils;
import com.common.utils.ImageUtils;
import com.common.utils.PermissionUtil;
import com.common.utils.PreferencesUtils;
import com.common.view.dialog.SucessCacheSureDialog;
import com.common.view.dialog.TakePhototpop;
import com.yuefeng.commondemo.R;
import com.yuefeng.login_splash.ui.LoginActivity;
import com.yuefeng.usercenter.ui.view.DeleteCacheDialog;
import com.yuefeng.usercenter.ui.view.UserInfoItemView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import static android.app.Activity.RESULT_CANCELED;

/**
 * Created  on 2018-02-05.
 * author:seven
 * email:seven2016s@163.com
 */

public class UserInfoFragment extends BaseMvpFragment {

    private ImageView userView;
    private UserInfoItemView shareApp, clean, author, collection, setting;
    private TextView userName;
    //    private UserInfoPresenter userInfoPresenter;
    private RelativeLayout userRootView;
    private static final int CAMERA_CODE = 1;//掉相机
    private static final int GALLERY_CODE = 2;//调相册
    private static final int CROP_CODE = 3;//拍照裁剪
    private String filePath;
    private Uri mImageUri;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_info;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
//        userInfoPresenter = new UserInfoPresenter(this, (MainActivity) getActivity());
        userView = rootView.findViewById(R.id.img_user_view);
        userRootView = rootView.findViewById(R.id.user_root);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.btn_add_normal);
        userView.setImageBitmap(bitmap);
        shareApp = rootView.findViewById(R.id.ui_share);
        clean = rootView.findViewById(R.id.ui_clean);
        collection = rootView.findViewById(R.id.ui_collection);
        author = rootView.findViewById(R.id.ui_author);
        setting = rootView.findViewById(R.id.ui_setting);
        userName = rootView.findViewById(R.id.tv_user_name);
    }

    @Override
    protected void initData() {
        if (PreferencesUtils.getString(getContext(), Constans.USERNAME) != null) {
            userName.setText(PreferencesUtils.getString(getContext(), Constans.USERNAME));
        }
        try {
            clean.setNumText(DataCleanManager.getTotalCacheSize(getContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setLisenter() {
        shareApp.setOnClickListener(this);
        clean.setOnClickListener(this);
        author.setOnClickListener(this);
        collection.setOnClickListener(this);
        setting.setOnClickListener(this);
        userView.setOnClickListener(this);
    }

    @Override
    protected void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.ui_share:
                showSuccessToast("待开发");
                break;
            case R.id.ui_clean:
                DeleteCacheDialog deleteCacheDialog = new DeleteCacheDialog(getContext());
                deleteCacheDialog.setDeletaCacheListener(new DeleteCacheDialog.DeletaCacheListener() {
                    @Override
                    public void sure() {
                        DataCleanManager.clearAllCache(getContext());
                        clean.setNumText("0.0B");
                        showSuccessToast("清除成功");
                    }

                    @Override
                    public void cancle() {
                        showSuccessToast("清理失败");
                    }
                });
                deleteCacheDialog.show();
                break;
            case R.id.ui_collection:
                showSuccessToast("待开发");
//                startActivity(new Intent(getContext(), CollectListActivity.class));

                break;
            case R.id.ui_author:
                showSuccessToast("待开发");
                break;
            case R.id.ui_setting://退出登录
                outOfLogin();
                break;
            case R.id.img_user_view:
                takeOrSelectPhoto();
                break;
        }
    }

    private void outOfLogin() {

        final SucessCacheSureDialog sucessCacheSureDialog = new SucessCacheSureDialog(getContext());
        sucessCacheSureDialog.setTextContent("您是否退出登录?");
        sucessCacheSureDialog.setDeletaCacheListener(new SucessCacheSureDialog.DeletaCacheListener() {
            @Override
            public void sure() {
                sucessCacheSureDialog.dismiss();
                startActivity(new Intent(getContext(), LoginActivity.class));
                PreferencesUtils.putString(getActivity(), Constans.COOKIE_PREF, "");
                AppManager.getAppManager().removedAlllActivity(getActivity());
            }

            @Override
            public void cancle() {
                sucessCacheSureDialog.dismiss();
            }
        });
        sucessCacheSureDialog.show();
    }

    private void takeOrSelectPhoto() {
        TakePhototpop takePhototpop = new TakePhototpop(getContext());
        takePhototpop.setTakePhotoTouch(new TakePhototpop.TakePhotoTouch() {
            @Override
            public void takePhoto() {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager
                        .PERMISSION_DENIED) {
                    PermissionUtil.requestPermission(getActivity(), PermissionUtil.CODE_CAMERA,
                            mPermissionGrant);
//                        takePhoto();
                } else {
                    takePhotos();
                }
            }

            @Override
            public void takeAlarm() {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, GALLERY_CODE);
            }

            @Override
            public void tvCancle() {

            }
        });
        takePhototpop.showTakePop(userRootView);
    }

    private void takePhotos() {
        File path = new File(Environment.getExternalStorageDirectory(), getContext().getPackageName());
        if (!path.exists()) {
            path.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        filePath = path + File.separator + fileName;
//        ImageUtils.cameraPic(getActivity(), filePath, CAMERA_CODE);
        //创建一个file，用来存储拍照后的照片
        File outputfile = new File(filePath);
        //创建一个file，用来存储拍照后的照片
//        com.seven.seven.common.utils.FileUtils.creatFile(outputfile);
        Uri imageuri;

        imageuri = Uri.fromFile(outputfile);
        //启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, CAMERA_CODE);
    }

    /*申请权限成功*/
    private PermissionUtil.PermissionGrant mPermissionGrant = new PermissionUtil.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode) {
            switch (requestCode) {
                case PermissionUtil.CODE_CAMERA:
                    takePhotos();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.requestPermissionsResult(getActivity(), requestCode, permissions, grantResults,
                mPermissionGrant);
    }

    /*
     * uri转bitmap压缩以后做高斯模糊有问题，不压缩又怕oom
     * 暂时不做高斯模糊，也不裁剪直接使用,懒得单独处理背景图
     * */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_CODE:
                if (resultCode == RESULT_CANCELED) {
                    return;
                } else {
                    if (TextUtils.isEmpty(filePath)) {
                        return;
                    } else {
                        Uri uri = Uri.fromFile(new File(filePath));
                        Bitmap bitmap = ImageUtils.decodeUriAsBitmap(getActivity(), uri);
//                        headImgView.setImageBitmap(bitmap);
                        userView.setImageBitmap(bitmap);
                        /*mImageUri = cutForCamera(getActivity(), filePath, filePath,
                                getContext().getPackageName(), CROP_CODE, 150, 150);*/
                    }
                }
                break;
            case GALLERY_CODE:
                if (data == null) {
                    return;
                } else {
                    /*String path = getContext().getExternalCacheDir() + getContext().getPackageName();
                    mImageUri = cutForPhoto(getActivity(), data.getData(), path,
                            CROP_CODE, 150, 150);*/
                    Bitmap bitmap = ImageUtils.decodeUriAsBitmap(getActivity(), data.getData());
//                    headImgView.setImageBitmap(bitmap);
                    userView.setImageBitmap(bitmap);
                }
                break;
            case CROP_CODE:
                if (data == null) {
                    return;
                } else {
                    if (mImageUri == null) {
                        return;
                    } else {
                        Bitmap bitmap = ImageUtils.decodeUriAsBitmap(getActivity(), mImageUri);
//                        headImgView.setImageBitmap(bitmap);
                        userView.setImageBitmap(bitmap);
                    }
                }
                break;

        }
    }

    /**
     * 拍照之后，启动裁剪
     *
     * @param camerapath 路径
     * @return
     */
    public Uri cutForCamera(Activity context, String camerapath, String outputPath, String authority, int requestCode, int width, int heigh) {
        try {
            //设置裁剪之后的图片路径文件
            File cutfile = new File(outputPath);
            //初始化 uri
            Uri imageUri = null; //返回来的 uri
            Uri outputUri = null; //真实的 uri
            Intent intent = new Intent("com.android.camera.action.CROP");
            //拍照留下的图片
            File camerafile = new File(camerapath);
//            if (Build.VERSION.SDK_INT >= 24) {
//                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                imageUri = FileProvider.getUriForFile(context,
//                        authority,
//                        camerafile);
//            } else {
            imageUri = Uri.fromFile(camerafile);
//            }
            outputUri = Uri.fromFile(cutfile);
            // crop为true是设置在开启的intent中设置显示的view可以剪裁
            intent.putExtra("crop", true);
            // aspectX,aspectY 是宽高的比例，这里设置正方形
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            //设置要裁剪的宽高
            intent.putExtra("outputX", width);
            intent.putExtra("outputY", heigh);
            intent.putExtra("scale", true);
            //如果图片过大，会导致oom，这里设置为false
            intent.putExtra("return-data", false);
            if (imageUri != null) {
                intent.setDataAndType(imageUri, "image/*");
            }
            if (outputUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
            }
            intent.putExtra("noFaceDetection", true);
            //压缩图片
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            startActivityForResult(intent, requestCode);
            return outputUri;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 图片裁剪
     *
     * @param uri
     * @return
     */
    public Uri cutForPhoto(Activity context, Uri uri, String path, int requestCode, int width, int heigh) {
        try {
            //直接裁剪
            Intent intent = new Intent("com.android.camera.action.CROP");
            //设置裁剪之后的图片路径文件
            File cutfile = new File(path + "/" + System.currentTimeMillis() + ".jpg"); //随便命名一个
            FileUtils.creatFile(cutfile);
            //初始化 uri
            Uri imageUri = uri; //返回来的 uri
            Uri outputUri = null; //真实的 uri
            outputUri = Uri.fromFile(cutfile);
            // crop为true是设置在开启的intent中设置显示的view可以剪裁
            intent.putExtra("crop", true);
            // aspectX,aspectY 是宽高的比例，这里设置正方形
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            //设置要裁剪的宽高
            intent.putExtra("outputX", width);
            intent.putExtra("outputY", heigh);
            intent.putExtra("scale", true);
            //如果图片过大，会导致oom，这里设置为false
            intent.putExtra("return-data", false);
            if (imageUri != null) {
                intent.setDataAndType(imageUri, "image/*");
            }
            if (outputUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
            }
            intent.putExtra("noFaceDetection", true);
            //压缩图片
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            startActivityForResult(intent, requestCode);
            return outputUri;
        } catch (Exception e) {

            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
//        userInfoPresenter.getCollectSize(0);
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void disposeCollectSizeEvents(UserInfoEvent userInfoEvent) {
//        switch (userInfoEvent.getWhat()) {
//            case Constans.COLLECTSIZE:
//                CollectListInfos collectListInfosList = (CollectListInfos) userInfoEvent.getData();
//                if (collectListInfosList.getDatas().size() != 0) {
//                    collection.setNumText(String.valueOf(collectListInfosList.getDatas().size()));
//                }
//
//                break;
//            case Constans.COLLECTSIZEERROR:
//                showErrorToast(userInfoEvent.getData().toString());
//            case Constans.RELOGIN:
//
//                break;
//        }
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void disposeReLoginEvents(ReloginEvent reloginEvent) {
        switch (reloginEvent.getWhat()) {
            case Constans.RELOGIN:
                startActivity(new Intent(getContext(), LoginActivity.class));
                AppManager.getAppManager().removedAlllActivity(getActivity());
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

}
