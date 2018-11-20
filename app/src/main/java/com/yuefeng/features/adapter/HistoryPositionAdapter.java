package com.yuefeng.features.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.common.utils.GlideUtils;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.modle.GetHistoryCaijiInfosMsgBean;
import com.yuefeng.photo.utils.ImageHelper;

import java.util.List;

/*历史采集*/
public class HistoryPositionAdapter extends BaseQuickAdapter<GetHistoryCaijiInfosMsgBean, BaseViewHolder> {

    private String name;
    private String time;
    private String detail;
    private String typeName;
    private int imageId;
    private String imageUrl;

    public HistoryPositionAdapter(int layoutResId, @Nullable List<GetHistoryCaijiInfosMsgBean> data) {
        super(layoutResId, data);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    protected void convert(BaseViewHolder helper, GetHistoryCaijiInfosMsgBean item) {
        int position = helper.getLayoutPosition();
        if (position == 0) {
            helper.setBackgroundRes(R.id.ll_layout_item, R.drawable.shape_cricle_bg_gray_top);
        } else {
            helper.setBackgroundRes(R.id.ll_layout_item, R.drawable.shape_cricle_bg_gray_top0);
        }
        if (item != null) {
            name = item.getName();
            time = item.getTime();
            detail = "采集类型";
            typeName = item.getTypename();
            imageUrl = item.getImgeurl();
            name = TextUtils.isEmpty(name) ? " " : name;
            time = TextUtils.isEmpty(time) ? " " : time;
            detail = TextUtils.isEmpty(detail) ? " " : detail;
            typeName = TextUtils.isEmpty(typeName) ? " " : typeName;
            imageUrl = TextUtils.isEmpty(imageUrl) ? " " : imageUrl;
            helper.setText(R.id.tv_item_title, name)
                    .setText(R.id.tv_item_time, time)
                    .setText(R.id.tv_item_type, typeName + "\n(" + detail + ")");
            if (TextUtils.isEmpty(imageUrl)) {
                imageId = R.drawable.caiji_wutupian;
                helper.setImageResource(R.id.iv_item_image, imageId);
            } else {
                String url = ImageHelper.getFirstImageUrl(imageUrl);
                ImageView imageview = helper.getView(R.id.iv_item_image);
                GlideUtils.loadImageViewLoading(imageview, url, R.drawable.caiji_wutupian, R.mipmap.dialog_loading_img);
            }
        }
    }
}
