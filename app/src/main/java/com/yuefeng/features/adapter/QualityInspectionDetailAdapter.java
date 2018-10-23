package com.yuefeng.features.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.common.utils.DensityUtil;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.modle.EventdetailMsgBean;
import com.yuefeng.photo.adapter.GvAdapter;
import com.yuefeng.photo.bean.ImageInfo;
import com.yuefeng.photo.view.MyGridView2;
import com.yuefeng.photo.view.PicShowDialog;
import com.yuefeng.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class QualityInspectionDetailAdapter extends BaseItemDraggableAdapter<EventdetailMsgBean, BaseViewHolder> {

    private int intColor;
    private RelativeLayout rl_item;
    private MyGridView2 gridview;
    private String things;
    private boolean isFirst = true;
    private String userid;
    private String detail = "";
    private String pinjia = "";
    private String imgurl;
    private List<ImageInfo> images;
    private GvAdapter adapter;
    private String time;
    private String[] split;
    private ViewGroup.LayoutParams layoutParams;

    public QualityInspectionDetailAdapter(int layoutResId, @Nullable List<EventdetailMsgBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EventdetailMsgBean item) {
        rl_item = helper.getView(R.id.rl_item);
        gridview = helper.getView(R.id.gridview);
        if (item != null) {
            if (isFirst) {
                isFirst = false;
                intColor = mContext.getResources().getColor(R.color.green_signin);
            } else {
                intColor = mContext.getResources().getColor(R.color.gray);
            }
            things = item.getThings();
            userid = item.getUserid();
            detail = item.getDetail();
            pinjia = item.getPinjia();
            imgurl = item.getImgurl();
            time = item.getTime();
            if (!TextUtils.isEmpty(time)) {
                time = StringUtils.returnStrTime(time);
            }

            String[] yearHour = StringUtils.returnTimeYearHour(time);
            String yearTime = yearHour[0];
            String hourTime = yearHour[1];
            String month = yearTime.substring(5, 10);
            String hour = hourTime.substring(0, 5);
//            Log.d(TAG, "convert:aaaa " + things);
            if (things.equals("关闭问题")) {
                if (TextUtils.isEmpty(detail)) {
                    detail = "描述:无";
                } else {
                    detail = "描述:" + detail;
                }
                if (TextUtils.isEmpty(pinjia)) {
                    pinjia = "评价:无";
                } else {
                    pinjia = "评价:" + pinjia;
                }
            }else if (things.equals("完成处理")){
                if (TextUtils.isEmpty(pinjia)) {
                    pinjia = "评价:无";
                } else {
                    pinjia = "评价:" + pinjia;
                }
            }
            helper.setText(R.id.tv_item_hour, hour)//时分
                    .setTextColor(R.id.tv_item_hour, intColor)//上报
                    .setText(R.id.tv_item_month, month)
                    .setText(R.id.tv_item_pingjia, pinjia)//评价
                    .setTextColor(R.id.tv_item_month, intColor)//月
                    .setText(R.id.tv_item_detail, userid + things)
                    .setTextColor(R.id.tv_item_detail, intColor)//上报人
                    .setText(R.id.tv_item_grade, detail)
                    .setTextColor(R.id.tv_item_grade, intColor);//评价
        }
        layoutParams = rl_item.getLayoutParams();
        if (!TextUtils.isEmpty(imgurl)) {
            showImgUrl(imgurl);
            if (detail.length() > 60) {
                layoutParams.height = DensityUtil.dip2px(mContext,220);
            } else {
                layoutParams.height = DensityUtil.dip2px(mContext,150);
            }
        } else {
            if (detail.length() > 60) {
                layoutParams.height = DensityUtil.dip2px(mContext,150);
            } else {
                layoutParams.height = DensityUtil.dip2px(mContext,80);
            }
        }
        rl_item.setLayoutParams(layoutParams);
    }

    private void showImgUrl(String imgUrl) {

        images = new ArrayList<>();
        images.clear();
        if (imgUrl.contains(",")) {
            split = imgUrl.split(",");
            for (String aSplit : split) {
                ImageInfo imageInfo = new ImageInfo(aSplit, 200, 200);
                images.add(imageInfo);
            }
        } else {
            ImageInfo imageInfo = new ImageInfo(imgUrl, 200, 200);
            images.add(imageInfo);
        }
        if (adapter == null) {
            adapter = new GvAdapter(mContext, images);
            gridview.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        if (images.size() > 0) {
            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    PicShowDialog dialog = new PicShowDialog(mContext, images, position);
                    dialog.show();
                }
            });
        }
    }
}
