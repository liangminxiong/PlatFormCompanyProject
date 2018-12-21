package com.yuefeng.usercenter.ui.fragment;

import android.Manifest;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.base.BaseMvpFragment;
import com.common.event.ReloginEvent;
import com.common.utils.AppManager;
import com.common.utils.Constans;
import com.common.utils.DataCleanManager;
import com.common.utils.ImageUtils;
import com.common.utils.PermissionUtil;
import com.common.utils.PreferencesUtils;
import com.common.view.dialog.SucessCacheSureDialog;
import com.common.view.dialog.TakePhototpop;
import com.yuefeng.commondemo.R;
import com.yuefeng.login_splash.ui.LoginActivity;
import com.yuefeng.ui.MyApplication;
import com.yuefeng.usercenter.ui.activity.AboutAppInfosActivity;
import com.yuefeng.usercenter.ui.activity.ChangePassWordActivity;
import com.yuefeng.usercenter.ui.view.DeleteCacheDialog;
import com.yuefeng.usercenter.ui.view.UserInfoItemView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_CANCELED;

/**
 * Created  on 2018-02-05.
 * author:seven
 * email:seven2016s@163.com
 */

public class UserInfoFragment extends BaseMvpFragment {

    @BindView(R.id.user_root)
    LinearLayout userRootView;
    @BindView(R.id.img_user_view)
    ImageView userView;
    @BindView(R.id.tv_user_name)
    TextView userName;
    @BindView(R.id.tv_user_phone)
    TextView tvUserPhone;
//    @BindView(R.id.tv_tab_name)
//    TextView tv_tab_name;
    @BindView(R.id.ui_share)
    UserInfoItemView shareApp;
    @BindView(R.id.ui_clean)
    UserInfoItemView clean;
    @BindView(R.id.ui_author)
    UserInfoItemView author;
    @BindView(R.id.ui_setting)
    UserInfoItemView setting;
    @BindString(R.string.tab_mine_name)
    String my_name;
    //    private UserInfoPresenter userInfoPresenter;
    private static final int CAMERA_CODE = 1;//掉相机
    private static final int GALLERY_CODE = 2;//调相册
    private static final int CROP_CODE = 3;//拍照裁剪
    private String filePath;
    private Uri mImageUri;
    Unbinder unbinder;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_info;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
//        userInfoPresenter = new UserInfoPresenter(this, (MainActivity) getActivity());
        unbinder = ButterKnife.bind(this, rootView);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.staff);
        userView.setImageBitmap(bitmap);
//        tv_tab_name.setText(my_name);
        initUI();
    }

    private void initUI() {
        String telNum = PreferencesUtils.getString(getContext(), Constans.TELNUM);
        if (!TextUtils.isEmpty(telNum)) {
            tvUserPhone.setText(telNum);
        }
    }

    @Override
    protected void fetchData() {
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
    }

    @Override
    protected void widgetClick(View v) {
//        switch (v.getId()) {
//            case R.id.ui_share:
//                showSuccessToast("待开发");
//                break;
//            case R.id.ui_clean:
//                DeleteCacheDialog deleteCacheDialog = new DeleteCacheDialog(getContext());
//                deleteCacheDialog.setReplyCancelListener(new DeleteCacheDialog.DeletaCacheListener() {
//                    @Override
//                    public void sure() {
//                        DataCleanManager.clearAllCache(getContext());
//                        clean.setNumText("0.0B");
//                        showSuccessToast("清除成功");
//                    }
//
//                    @Override
//                    public void cancle() {
//                        showSuccessToast("清理失败");
//                    }
//                });
//                deleteCacheDialog.show();
//                break;
//            case R.id.ui_author:
//                showSuccessToast("待开发");
//                break;
//            case R.id.ui_setting://退出登录
//                outOfLogin();
//                break;
//            case R.id.img_user_view:
//                takeOrSelectPhoto();
//                break;
//        }
    }

    @OnClick({R.id.ui_share, R.id.ui_clean, R.id.ui_author, R.id.ui_setting, R.id.img_user_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ui_share://修改密码
                startActivity(new Intent(getActivity(), ChangePassWordActivity.class));
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
            case R.id.ui_author://关于软件
                startActivity(new Intent(getActivity(), AboutAppInfosActivity.class));

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
                PreferencesUtils.putBoolean(MyApplication.getContext(), Constans.HAVE_USER_DATAS, false);
                PreferencesUtils.putString(MyApplication.getContext(), Constans.COOKIE_PREF, "");
                startActivity(new Intent(getContext(), LoginActivity.class));
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


    @Override
    public void onResume() {
        super.onResume();
//        userInfoPresenter.getCollectSize(0);
    }

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
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

}
