package com.babelstar.gviewer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Environment;
import android.util.AttributeSet;

import com.yuefeng.features.ui.activity.video.VideoCameraActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;

public class VideoView extends android.support.v7.widget.AppCompatImageView {
    public static final String RECORD_DIRECTORY = "gStorage/record";
    private static final String RECORD_PATH = Environment.getExternalStorageDirectory().getPath() + "/" + RECORD_DIRECTORY;
    public static final String SNAPSHOT_DIRECTORY = "gStorage/snapshot";
    private static final String SNAPSHOT_PATH = Environment.getExternalStorageDirectory().getPath() + "/" + SNAPSHOT_DIRECTORY;

    private VideoCameraActivity mMainActivity;
    private Context mContext;
    private Integer mIndex = 0;
    private Paint mPaint;
    private boolean mIsFocus;        //是否处于焦点
    private boolean isLoading;        //是否正在加载

    private Object lockHandle = new Object();    //RealHandle的锁
    private long mRealHandle = 0;    //实时预览对象
    private int mRealRate = 0;        //实时预览的码率
    private boolean mRecording = false;    //录像状态
    private String mDevName;        //设备名称
    private String mDevIdno;        //设备编号信息
    private int mChannel = 0;        //通道号信息
    private String mChnName = "";        //通道名称
    private String mStrLoading = "";

    private int mVideoWidth = 0;    //视频宽度和高度
    private int mVideoHeight = 0;    //视频高度
    private int mVideoRgb8888Length = 0;
    private byte[] mVideoPixel = null;
    private ByteBuffer mVideoBuffer = null;
    private Bitmap mVideoBmp = null;
    private boolean isVideoBmpVaild = false;
    private final Paint prFramePaint = new Paint(Paint.FILTER_BITMAP_FLAG);
    private long mInvalidateTime = 0;    //刷新时间

    public VideoView(Context context, int index, VideoCameraActivity mainActivity) {
        super(context);
        mStrLoading = "加载中...";
        this.mIndex = index;
        this.mMainActivity = mainActivity;
        this.mContext = context;
        initPaint();
    }

    public VideoView(Context context, AttributeSet attr) {
        super(context, attr);
        this.mContext = context;
        mStrLoading = "加载中...";
        initPaint();
    }

    public void setViewInfo(String devName, String devIdno, int channel, String chnName) {
        this.mDevName = devName;
        this.mDevIdno = devIdno;
        this.mChannel = channel;
        this.mChnName = chnName;
    }

    public Integer getIndex() {
        return this.mIndex;
    }

    public void setIsFocus(boolean bIsFocus) {
        this.mIsFocus = bIsFocus;
    }

    public boolean getIsFocus() {
        return this.mIsFocus;
    }

    private void initPaint() {
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        //noinspection ResourceAsColor
        this.mPaint.setColor(-65536);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeWidth(1.0F);
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);

        Rect rect = new Rect();
        rect.left = 1;
        rect.top = 1;
        rect.right = getWidth() - 2;
        rect.bottom = getHeight() - 2;

        boolean drawVideo = false;
        synchronized (lockHandle) {
            if (isVideoBmpVaild && mVideoBmp != null) {
                drawVideo = true;
                Rect rc = new Rect();
                rc.left = 2;
                rc.top = 2;
                rc.right = getWidth() - 2;
                rc.bottom = getHeight() - 2;


                int nWndWidth = rc.right - rc.left;
                int nWndHeight = rc.bottom - rc.top;
                int nWndRate = (int) (nWndWidth * 100.0 / nWndHeight);
                int nRealRate = (int) (mVideoWidth * 100.0 / mVideoHeight);

                int nWidth = 0;
                int nHeight = 0;
                if (nWndRate > nRealRate) {
                    nHeight = nWndHeight;
                    nWidth = nWndWidth;
//                    nWidth = (int) (mVideoWidth * 1.0 * nHeight / mVideoHeight);
                } else {
                    nWidth = nWndWidth;
//                    nHeight = (int) (mVideoHeight * 1.0 * nWidth / mVideoWidth);
                    nHeight = nWndHeight;
                }

                if (nWidth > nWndWidth) {
                    nWidth = nWndWidth;
                }

                if (nHeight > nWndHeight) {
                    nHeight = nWndHeight;
                }

                Rect dest = new Rect();
                dest.left = rc.left + (nWndWidth - nWidth) / 2;
                dest.right = dest.left + nWidth;
                dest.top = rc.top + (nWndHeight - nHeight) / 2;
                dest.bottom = dest.top + nHeight;

                Rect src = new Rect();
                src.left = 0;
                src.top = 0;
                src.bottom = mVideoHeight;
                src.right = mVideoWidth;

                this.mPaint.setStyle(Paint.Style.FILL);
                this.mPaint.setColor(Color.rgb(0, 0, 0));
                canvas.drawRect(dest, this.mPaint);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
                    mVideoBmp.copyPixelsFromBuffer(mVideoBuffer);
                }
                mVideoBuffer.position(0);
                canvas.drawBitmap(mVideoBmp, src, dest, prFramePaint);
                drawVideo = true;
            }
        }

        String title = Integer.toString(this.mIndex + 1);
        if (!mChnName.equals("")) {
            title += " - " + mChnName;
        }
        if (isViewing()) {
            if (!drawVideo) {
                this.mPaint.setColor(Color.rgb(113, 227, 28));
                this.mPaint.setTextAlign(Paint.Align.CENTER);
                this.mPaint.setTextSize(24);
                canvas.drawText(mStrLoading, getWidth() / 2, getHeight() / 2, this.mPaint);
            }

            int nRate = this.mRealRate;
            if (nRate > 200) {
                nRate = 0;
            }
            //显示码率
            title += " - " + Integer.toString(nRate) + "KB/S";
        }

        this.mPaint.setStyle(Paint.Style.FILL);
        this.mPaint.setColor(Color.rgb(255, 0, 0));
        this.mPaint.setTextAlign(Paint.Align.LEFT);

        if (mRecording) {
            title += " - REC";
        }

        this.mPaint.setStyle(Paint.Style.STROKE);
        if (mIsFocus) {
            this.mPaint.setColor(Color.rgb(113, 227, 28));
        } else {
            this.mPaint.setColor(Color.rgb(72, 72, 72));
        }
        canvas.drawRect(rect, this.mPaint);
    }

    public void reflash() {
        invalidate();
    }

    public boolean StartAV() {
        //先停止
        StopAV();
        boolean ret = false;
        //启动视频预览
        synchronized (lockHandle) {
            mRealHandle = NetClient.OpenRealPlay(mDevIdno, mChannel, 1, 0);
            NetClient.StartRealPlay(mRealHandle);
            if (mRealHandle != 0) {
                isLoading = true;
                postInvalidate();
                ret = true;
            }
        }
        return ret;
    }

    public boolean StopAV() {
        boolean ret = false;
        synchronized (lockHandle) {
            if (mRealHandle != 0) {
                NetClient.StopRealPlay(mRealHandle);
                NetClient.CloseRealPlay(mRealHandle);
                mRealHandle = 0;
                isLoading = false;
                mRealRate = 0;
                mRecording = false;
                resetVideoBuf();
                postInvalidate();
                ret = true;
            }
        }
        return ret;
    }

    /*
     * 判断是否正在预览
     */
    public boolean isViewing() {
        boolean ret = false;
        synchronized (lockHandle) {
            if (this.mRealHandle != 0) {
                ret = true;
            }
        }
        return ret;
    }

    /*
     * 判断是否正在预览
     */
    public void updateView() {
        synchronized (lockHandle) {
            if (this.mRealHandle != 0) {
                boolean isPostInvalidate = false;
                boolean suc = (NetClient.GetRPlayStatus(this.mRealHandle) == NetClient.NET_SUCCESS ? true : false);
                if (isLoading != !suc) {
                    postInvalidate();
                    isLoading = !suc;
                    isPostInvalidate = true;
                }
                if (!isLoading) {
                    int[] videoSize = new int[2];
                    videoSize[0] = 0;
                    videoSize[1] = 0;
                    if (NetClient.GetRPlayImage(this.mRealHandle, mVideoRgb8888Length, mVideoPixel, videoSize, NetClient.GPS_RGB_FORMAT_8888) != NetClient.NET_SUCCESS) {
                        if (videoSize[0] > 0 && videoSize[1] > 0) {
                            initVideoBuf(videoSize[0], videoSize[1]);
                        }
                    } else {
                        isVideoBmpVaild = true;
                        postInvalidate();
                        isPostInvalidate = true;
                    }
                }

                mRealRate = NetClient.GetRPlayRate(this.mRealHandle);
                if (isPostInvalidate) {
                    mInvalidateTime = System.currentTimeMillis();
                } else {
                    if (System.currentTimeMillis() - mInvalidateTime > 1000) {
                        postInvalidate();
                        mInvalidateTime = System.currentTimeMillis();
                    }
                }
            }
        }
    }

    /*
     * 初始化图像
     */
    private void initVideoBuf(int nWidth, int nHeight) {
        mVideoWidth = nWidth;
        mVideoHeight = nHeight;
        mVideoRgb8888Length = mVideoWidth * mVideoHeight * 4;
        mVideoPixel = new byte[mVideoRgb8888Length];
        mVideoBuffer = ByteBuffer.wrap(mVideoPixel);
        mVideoBmp = Bitmap.createBitmap(mVideoWidth, mVideoHeight, Config.ARGB_8888);
        isVideoBmpVaild = false;
    }

    /*
     * 重置图像缓存
     */
    private void resetVideoBuf() {
        mVideoWidth = 0;
        mVideoHeight = 0;
        mVideoRgb8888Length = 0;
        mVideoPixel = null;
        mVideoBuffer = null;
        if (mVideoBmp != null) {
            mVideoBmp.recycle();
        }
        mVideoBmp = null;
        isVideoBmpVaild = false;
    }

    /*
     * 保存成BMP图片
     */
    public boolean savePngFile() {
        File file = new File(SNAPSHOT_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        boolean ret = false;
        synchronized (lockHandle) {
            try {
                if (isVideoBmpVaild && mVideoBmp != null) {
                    String pngName = file.getPath() + "/" + mDevName + " - " + mChnName + ".jpg";

                    FileOutputStream fos = new FileOutputStream(pngName);
                    mVideoBmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.close();
                    ret = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    /*
     * 云台控制
	 */
    public void ptzControl(int command, int param) {
        synchronized (lockHandle) {
            NetClient.RPlayPtzCtrl(mRealHandle, command, NetClient.GPS_PTZ_SPEED_DEFAULT, param);
        }
    }

    /*
    * 判断是否正在预览
    */
    public boolean isRecording() {
        boolean ret = false;
        synchronized (lockHandle) {
            ret = mRecording;
        }
        return ret;
    }

    /*
     * 启动或停止录像
     */
    public void record() {
        synchronized (lockHandle) {
            if (mRealHandle != 0) {
                if (!mRecording) {
                    NetClient.RPlayStartRecord(mRealHandle, RECORD_PATH, mDevName);
                } else {
                    NetClient.RPlayStopRecord(mRealHandle);
                }
                mRecording = !mRecording;
            }
        }
    }

    /*
     * 启动录像
     */
    public boolean startRecord() {
        synchronized (lockHandle) {
            if (mRealHandle != 0) {
                if (!mRecording) {
                    NetClient.RPlayStartRecord(mRealHandle, RECORD_PATH, mDevName);
                    mRecording = true;
                }
            }
        }
        return mRecording;
    }

    /*
     * 停止录像
     */
    public boolean stopRecord() {
        boolean ret = false;
        synchronized (lockHandle) {
            if (mRealHandle != 0) {
                if (mRecording) {
                    NetClient.RPlayStopRecord(mRealHandle);
                    mRecording = false;
                    ret = true;
                }
            }
        }
        return ret;
    }
}
