package com.yuefeng.features.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.common.utils.GlideUtils;
import com.common.utils.TimeUtils;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.modle.EventQuestionMsgBean;
import com.yuefeng.utils.StringUtils;

import java.util.List;

public class AllProblemAdapter extends BaseItemDraggableAdapter<EventQuestionMsgBean, BaseViewHolder> {

    private ImageView iv_item_logo;
    private String state;
    private String type;
    private int colorInt;
    private int colorIntTime;
    private String time;
    private String imgurlStr;
    private String id;
    private String uploadpeoplename;
    private String problem;

    public AllProblemAdapter(int layoutResId, @Nullable List<EventQuestionMsgBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EventQuestionMsgBean item) {
        /* tv_item_type,iv_item_type ,tv_item_time,
        iv_item_logo,tv_item_where,tv_item_reporter,tv_item_procesonal
         * ll_item ,iv_item_claim,iv_item_forward
         * 问题状态（1：待处理；2：处理中；3：待关闭 4：已关闭）
         * */
        iv_item_logo = helper.getView(R.id.iv_item_logo);
        if (item != null) {
            state = com.common.utils.StringUtils.isEntryStrZero(item.getState());
            Log.d(TAG, "convert: " + item.getId() + " ++ " + state);
            String uploadtime = com.common.utils.StringUtils.isEntryStrZero(item.getUploadtime());
            if (!TextUtils.isEmpty(uploadtime)) {
                time = StringUtils.returnStrTime(uploadtime);
            }
            if (state.equals("1")) {
                float hourSpan = TimeUtils.getHourSpan(time, TimeUtils.getCurrentTime());
                if (hourSpan >= 6) {
                    colorIntTime = mContext.getResources().getColor(R.color.red_fc);
                } else if (hourSpan >= 2) {
                    colorIntTime = mContext.getResources().getColor(R.color.huang_se);
                } else {
                    colorIntTime = mContext.getResources().getColor(R.color.maingreen);
                }
                state = mContext.getString(R.string.pending_txt);//待处理
                colorInt = mContext.getResources().getColor(R.color.red_fc);
                helper.setVisible(R.id.ll_item, true);
                helper.setVisible(R.id.iv_item_claim, true);
                helper.setImageResource(R.id.iv_item_forward, R.drawable.renling);
            } else if (state.equals("2")) {
                state = mContext.getString(R.string.processing_txt);//处理中
                helper.setVisible(R.id.ll_item, true);
                helper.setVisible(R.id.iv_item_claim, false);
                colorInt = mContext.getResources().getColor(R.color.huang_se);
                colorIntTime = mContext.getResources().getColor(R.color.gray);
                helper.setImageResource(R.id.iv_item_forward, R.drawable.finish_023x);
            } else if (state.equals("3")) {
                state = mContext.getString(R.string.toclosed_txt);//待关闭
                helper.setVisible(R.id.ll_item, true);
                helper.setVisible(R.id.iv_item_claim, false);
                colorInt = mContext.getResources().getColor(R.color.yellow);
                helper.setImageResource(R.id.iv_item_forward, R.drawable.close_023x);
                colorIntTime = mContext.getResources().getColor(R.color.gray);
            } else {
                helper.setGone(R.id.ll_item, false);
                state = mContext.getString(R.string.closed_txt);//已关闭
                colorInt = mContext.getResources().getColor(R.color.maingreen);
                colorIntTime = mContext.getResources().getColor(R.color.gray);
            }
            type = com.common.utils.StringUtils.isEntryStrZero(item.getType());

            if (type.equals("1")) {
                helper.setImageResource(R.id.iv_item_type, R.drawable.ji);
            }

            id = com.common.utils.StringUtils.isEntryStrZero(item.getId());
            uploadpeoplename = item.getUploadpeoplename();
            problem = item.getProblem();
            id = TextUtils.isEmpty(id) ? "" : id;
            uploadpeoplename = TextUtils.isEmpty(uploadpeoplename) ? "" : uploadpeoplename;
            problem = TextUtils.isEmpty(problem) ? "" : problem;
            helper.setText(R.id.tv_item_type, state)
                    .setTextColor(R.id.tv_item_type, colorInt)
                    .setText(R.id.tv_item_typenum, " #" + id)
                    .setText(R.id.tv_item_time, time)
                    .setTextColor(R.id.tv_item_time, colorIntTime)
                    .setText(R.id.tv_item_where, problem)
                    .setText(R.id.tv_item_reporter, "上报人 ：" + uploadpeoplename)
//                    .setText(R.id.tv_item_procesonal, "处理人 ：" + item.getUploadpeoplename())
                    .addOnClickListener(R.id.iv_item_claim)
                    .addOnClickListener(R.id.iv_item_forward);
            imgurlStr = item.getImgurl();
            if (!TextUtils.isEmpty(imgurlStr)) {
                String[] split = imgurlStr.split(",");
                GlideUtils.loadImageViewLoading(iv_item_logo, split[0], R.drawable.picture, R.drawable.picture);
            } else {
                helper.setImageResource(R.id.iv_item_logo, R.drawable.picture);
            }
        }
    }
}
