package com.common.updateapputils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.common.network.ApiService;
import com.common.utils.ToastUtils;
import com.yuefeng.commondemo.R;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class UpdateManager implements View.OnClickListener {
    private Context mContext;
    private final String TAG = "tag";
    private final static String APKURL = ApiService.apkPath;
    private final static String VERURL = ApiService.sersionPath;
    private int NewVersion = -1;
    private ProgressBar mProgressBar;

    /* 下载包安装路径 */
    @SuppressLint("SdCardPath")
    private static final String SavePath = Environment.getExternalStorageDirectory() + "/sdcard/";
    private static final String SaveFileName = SavePath + ApiService.APPNAME;
    private int progress = 0;
    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
    private Boolean interceptFlag = false;

    private boolean isLogin;
    protected GpsVersion version;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    NewVersion = getVerCode(mContext);
                    version = (GpsVersion) msg.obj;
                    Log.d(TAG, "handleMessage: " + NewVersion + " ++ " + version.VerCode);
                    if (Integer.valueOf(version.VerCode) > NewVersion && NewVersion > 0) {
                        ShowUpdateDialog();
                    } else {
                        if (!isLogin) {
                            ToastUtils.showToast(mContext.getString(R.string.is_newest_version));
                        }
                    }
                    break;
                case 1:
                    mProgressBar.setProgress(progress);
                    break;
                case 2:
                    installApk();
                    break;
                default:
                    break;
            }
        }
    };

    private boolean isFirstOnclick = true;
    private boolean haveInstallPermission;

    public void isAndoird8() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//如果是 则判断当前是否有权限
            haveInstallPermission = mContext.getPackageManager().canRequestPackageInstalls();
//没有权限则进行权限请求
            if (!haveInstallPermission) {
                startInstallPermissionSettingActivity();
            }
        }
    }

    //打开设置——允许安装未知来源的应用
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startInstallPermissionSettingActivity() {
//注意这个是8.0新API
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        ((Activity) mContext).startActivityForResult(intent, 10086);
    }

    private void installApk() {
        isFirstOnclick = true;
        File apkfile = new File(SaveFileName);
        if (!apkfile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (Build.VERSION.SDK_INT >= 24) {//判读版本是否在7.0以上
            //在AndroidManifest中的android:authorities值
            Uri apkUri = FileProvider.getUriForFile(mContext, "com.yuefeng.paltform.fileprovider", apkfile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");

        } else {
            intent.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        }
        mContext.startActivity(intent);
    }

    public UpdateManager(Context context, boolean isLogin) {
        this.mContext = context;
        this.isLogin = isLogin;
    }

    private AlertDialog dialog;
    private ImageView logo;
    private TextView content;
    private TextView ok;
    private TextView cancle;

    @SuppressLint("SetTextI18n")
    private void ShowUpdateDialog() {

        Builder builder = new Builder(mContext);
        View view = View.inflate(mContext, R.layout.dialog_item, null);

        logo = (ImageView) view.findViewById(R.id.iv_logo);
        Resources resources = mContext.getResources();
        Drawable logoDrawable = resources.getDrawable(R.mipmap.icon_app);
        logo.setBackgroundDrawable(logoDrawable);

        content = (TextView) view.findViewById(R.id.content);
        mProgressBar = (ProgressBar) view.findViewById(R.id.pb_Update);
        ok = (TextView) view.findViewById(R.id.ok);
        cancle = (TextView) view.findViewById(R.id.cancle);

        String des = "";
        String description = version.Description;
        if (!TextUtils.isEmpty(description)) {
            if (description.contains(",")) {
                String[] split = description.split(",");
                int count = 1;
                for (String aSplit : split) {
                    if (!TextUtils.isEmpty(aSplit)) {
                        des = des + "\r\n" + count++ + "." + aSplit;
                    }
                }

            } else {
                des = "\r\n" + "1." + description;
            }
        }

        content.setText("更新内容:\r\n" + "有新版本更新了啦!");
        ok.setText(R.string.update);
        cancle.setText(R.string.cancel);
        // 给cancle 设置监听事件。
        cancle.setOnClickListener(this);
        ok.setOnClickListener(this);

        dialog = builder.create();
        dialog.setView(view, 0, 0, 0, 0);
        if (dialog != null) {
            dialog.show();
        }

    }

    private void downloadApk() {
        new Thread() {
            public void run() {
                try {
                    URL url = new URL(APKURL);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    int length = conn.getContentLength();
                    InputStream is = conn.getInputStream();
                    File file = new File(SavePath);
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    String apkFile = SaveFileName;
                    File ApkFile = new File(apkFile);
                    if (ApkFile.exists())
                        ApkFile.delete();
                    FileOutputStream fos = new FileOutputStream(ApkFile);
                    int count = 0;
                    byte buf[] = new byte[1024];

                    do {
                        int numread = is.read(buf);

                        count += numread;
                        progress = (int) (((float) count / length) * 100);
                        //更新进度
                        mHandler.sendEmptyMessage(DOWN_UPDATE);
                        if (numread <= 0) {
                            //下载完成通知安装
                            mHandler.sendEmptyMessage(DOWN_OVER);
                            break;
                        }
                        fos.write(buf, 0, numread);
                    } while (!interceptFlag);//点击取消就停止下载.
                    fos.close();
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            ;
        }.start();
    }

    private static int getVerCode(Context context) {
        int verCode = -1;
        try {
            verCode = context.getPackageManager().getPackageInfo("com.yuefeng.paltform", 0).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return verCode;
    }

    public void checkVersion() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                StartCheckVersion();
            }
        }).start();
    }

    private void StartCheckVersion() {
        try {
            URL mUrl = new URL(VERURL);
            URLConnection ucon = mUrl.openConnection();
            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            int current = 0;
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] data = new byte[50];
            while ((current = bis.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, current);
            }
            buffer.toByteArray();
            String myString = new String(buffer.toByteArray(), "GB2312");
            if (!TextUtils.isEmpty(myString))
                parseJason(myString);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseJason(String jsonData) {
        String[] strings = null;
        try {
            String[] split = jsonData.split("\\[");
            if (split.length > 1) {
                strings = split[1].split("\\]");
            }
            GpsVersion version = new GpsVersion();
            JSONObject jsonObj = new JSONObject(strings[0]);
            version.VerName = jsonObj.getString("VerName");
            version.VerCode = jsonObj.getString("VerCode");
//            version.Description = jsonObj.getString("Description");
//            SharedPreUtil.saveString(Global.mContext, "description", version.Description);
            if (version.VerCode != null && version.VerName != null) {
                Message msg = new Message();
                msg.what = 0;
                msg.obj = version;
                mHandler.sendMessage(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class GpsVersion {
        private String VerName;
        private String VerCode;
        private String Description;//更新此版本的描述信息
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok://确定更新
                if (isFirstOnclick) {
                    isFirstOnclick = false;
                    downloadApk();
                } else {
                    ToastUtils.showToast(mContext.getString(R.string.onclick_again));
                }
                ok.setText(R.string.updating);
                break;
            case R.id.cancle://稍后再说
                interceptFlag = true;
                if (dialog != null) {
                    dialog.dismiss();
                }
                break;
            default:
                break;
        }

    }
}
