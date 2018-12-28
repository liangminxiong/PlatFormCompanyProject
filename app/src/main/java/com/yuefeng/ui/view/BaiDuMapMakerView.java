package com.yuefeng.ui.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.model.LatLng;
import com.common.utils.Constans;
import com.common.utils.PreferencesUtils;
import com.common.utils.ResourcesUtils;
import com.common.utils.StringUtils;
import com.common.utils.ToastUtils;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.modle.zhuguanSign.ZhuGuanSignListBean;
import com.yuefeng.features.ui.activity.sngnin.HistoryExecuTrackActivity;
import com.yuefeng.rongIm.RongIMUtils;
import com.yuefeng.utils.BdLocationUtil;

/*marker弹框*/
public class BaiDuMapMakerView {

    private RelativeLayout rl_parent;
    private TextView mTvName, mTvCarTrack, mTvSendMsg, mTvTimes, mTvPhone, mTvAddress;
    private View popoverView;
    private InfoWindow mInfoWindow;
    private LatLng mLatLng;
    private Context mContext;
    private BaiduMap mBaiduMap;

    public BaiDuMapMakerView(Context context) {
        this.mContext = context;
        popoverView = ResourcesUtils.inflate(R.layout.baidumap_infowindow_view);
        rl_parent = (RelativeLayout) popoverView.findViewById(R.id.rl_parent);
        mTvName = (TextView) popoverView.findViewById(R.id.tv_name);
        mTvCarTrack = (TextView) popoverView.findViewById(R.id.tv_car_track);
        mTvSendMsg = (TextView) popoverView.findViewById(R.id.tv_send_msg);

        mTvPhone = (TextView) popoverView.findViewById(R.id.tv_phone_number);
        mTvTimes = (TextView) popoverView.findViewById(R.id.tv_times);
        mTvAddress = (TextView) popoverView.findViewById(R.id.tv_address);
    }

    /**
     * @param mBaiduMap
     */
    @SuppressLint("SetTextI18n")
    public void showViewData(final BaiduMap mBaiduMap, final ZhuGuanSignListBean markerBean) {
        try {
            this.mBaiduMap = mBaiduMap;
            final String phone = StringUtils.isEntryStrWu(markerBean.getTel());

            mTvName.setText(StringUtils.isEntryStrWu(markerBean.getName()));
            mTvPhone.setText(phone);
            mTvTimes.setText(StringUtils.isEntryStrWu(markerBean.getTime()));
            mTvAddress.setText(StringUtils.isEntryStrWu(markerBean.getAddress()));

            String lat = markerBean.getLat();
            String lng = markerBean.getLng();
            if (!TextUtils.isEmpty(lat) && !TextUtils.isEmpty(lng)) {
                mLatLng = BdLocationUtil.ConverGpsToBaidu(new LatLng(Double.valueOf(lat), Double.valueOf(lng)));
            }

            if (!phone.equals("无")) {
                mTvPhone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StringUtils.callPhone(mContext, phone);
                    }
                });
            }
            mTvSendMsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String userid = PreferencesUtils.getString(mContext, Constans.ID, "");
                    if (userid.equals(markerBean.getId())) {
                        ToastUtils.showToast("亲，你要自己和自己聊天吗?");
                        return;
                    }
                    RongIMUtils.startPrivateChat(mContext, markerBean.getId(), markerBean.getName());
                }
            });
            mTvCarTrack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(mContext, HistoryExecuTrackActivity.class);
                    intent.putExtra(Constans.ID, markerBean.getId());
                    mContext.startActivity(intent);
                }
            });

            InfoWindow.OnInfoWindowClickListener listener = new InfoWindow.OnInfoWindowClickListener() {
                public void onInfoWindowClick() {
                    //				LatLng ll = mMarker.getPosition();
                    //				LatLng llNew = new LatLng(ll.latitude ,ll.longitude);
                    //				mMarker.setPosition(llNew);
                    //				mBaiduMap.hideInfoWindow();
                    //				mInfoWindow = null ;
                }
            };

            rl_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideMarkerView();
                }
            });

            mInfoWindow = new InfoWindow(popoverView, mLatLng, 40);
            mBaiduMap.showInfoWindow(mInfoWindow);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideMarkerView() {
        if (mInfoWindow != null) {
            mBaiduMap.hideInfoWindow();
            mInfoWindow = null;
        }
    }

}
