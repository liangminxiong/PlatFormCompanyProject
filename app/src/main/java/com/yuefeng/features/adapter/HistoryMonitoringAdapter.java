package com.yuefeng.features.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.common.utils.StringUtils;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.modle.GetMonitoringHistoryDetaiBean;

import java.util.List;

public class HistoryMonitoringAdapter extends BaseQuickAdapter<GetMonitoringHistoryDetaiBean, BaseViewHolder> {

    private String startTime;
    private String endTime;
    private String startAddress;
    private String endAddress;

    public HistoryMonitoringAdapter(int layoutResId, @Nullable List<GetMonitoringHistoryDetaiBean> data) {
        super(layoutResId, data);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    protected void convert(BaseViewHolder helper, GetMonitoringHistoryDetaiBean item) {
        if (item != null && helper != null) {
            startTime = item.getStarttime();
            endTime = item.getEndtime();
            startAddress = item.getStartaddress();
            endAddress = item.getEndaddress();
            startTime = StringUtils.returnStrTime(startTime);
            endTime = StringUtils.returnStrTime(endTime);
            startAddress = StringUtils.isEntryStrWu(startAddress);
            endAddress = StringUtils.isEntryStrWu(endAddress);
            helper.setText(R.id.tv_item_time, startTime + "---" + endTime)
                    .setText(R.id.tv_item_startaddress, startAddress)
                    .setImageResource(R.id.iv_start, R.drawable.jiancha_qishi3x)
                    .setText(R.id.tv_item_endaddress, endAddress);
        }
    }
}
