package com.yuefeng.features.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.common.utils.StringUtils;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.modle.HistoryMonitoringBean;

import java.util.List;

public class HistoryMonitoringAdapter extends BaseQuickAdapter<HistoryMonitoringBean, BaseViewHolder> {

    private String startTime;
    private String endTime;
    private String startAddress;
    private String endAddress;

    public HistoryMonitoringAdapter(int layoutResId, @Nullable List<HistoryMonitoringBean> data) {
        super(layoutResId, data);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    protected void convert(BaseViewHolder helper, HistoryMonitoringBean item) {
        if (item != null && helper != null) {
            startTime = item.getStartTime();
            endTime = item.getEndTime();
            startAddress = item.getStartAddress();
            endAddress = item.getEndAddress();
            startTime = StringUtils.isEntryStrNull(startTime);
            endTime = StringUtils.isEntryStrNull(endTime);
            startAddress = StringUtils.isEntryStrWu(startAddress);
            endAddress = StringUtils.isEntryStrWu(endAddress);
            helper.setText(R.id.tv_item_time, startTime + "---" + endTime)
                    .setText(R.id.tv_item_startaddress, startAddress)
                    .setImageResource(R.id.iv_start, R.drawable.jiancha_qishi3x)
                    .setText(R.id.tv_item_endaddress, endAddress);
        }
    }
}
