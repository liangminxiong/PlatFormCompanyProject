package com.yuefeng.home.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.common.base.codereview.BaseActivity;
import com.common.utils.TimeUtils;
import com.yuefeng.commondemo.R;
import com.yuefeng.home.ui.adapter.MsgDetailInfosAdapter;
import com.yuefeng.home.ui.modle.MsgDataBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/*信息详情*/
@SuppressLint("Registered")
public class MsgDetailInfosActivtiy extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;
//    @BindView(R.id.space)
//    View view;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private List<MsgDataBean> listData = new ArrayList<>();
    private MsgDetailInfosAdapter adapter;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_msgdetailinfos;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        tv_title.setText(R.string.detail);
//        view.setBackground(mActivity.getResources().getDrawable(R.drawable.title_toolbar_bg_blue));
//        StatusBarUtil.setFadeStatusBarHeight(mActivity, view);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        initRecycleView();

        getDatas();
    }

    private void getDatas() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
//        listData = (List<MsgDataBean>) intent.getSerializableExtra("msgList");
//        adapter.setNewData(listData);
        assert bundle != null;
        int size = (int) bundle.get("tempPosition");
        showAdapterDatasList(size);
    }

    /*展示数据*/
    private void showAdapterDatasList(int size) {
        String name = "";
        String title = "";
        String content = "";
        List<MsgDataBean> bean = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            MsgDataBean msgDataBean = new MsgDataBean();
            msgDataBean.setImageUrl(R.drawable.item);
            if (size == 3) {
                name = "集团";
                title = "侨银环保生活垃圾分类示范线路正式运营!";
                content = "城市垃圾分类非常成功 ! ! !";
            } else if (size == 1) {
                name = "集团";
                title = "六甲片区监察任务";
                content = "六甲片区——八路一组全部项目主管需在10月1日至11月1日完成监察工作。计划周期一个月，监察次数10次";
            } else {
                name = "集团";
                title = "软件更新!";
                content = "新版本新体验 ! ! !";
            }
            msgDataBean.setName(name);
            msgDataBean.setTitle(title);
            msgDataBean.setTime(TimeUtils.getHourMinute());
            msgDataBean.setDetail(content);
            msgDataBean.setCount(i + "");
            bean.add(msgDataBean);
        }
        listData.clear();
        listData.addAll(bean);
        adapter.setNewData(listData);
    }

    private void initRecycleView() {
        adapter = new MsgDetailInfosAdapter(R.layout.recyclerview_item_msgdetail, listData);
        recyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                showSuccessToast(listData.get(position).getCount());

            }
        });
    }


    @Override
    protected void initData() {

    }

    @Override
    protected void setLisenter() {

    }

    @Override
    protected void widgetClick(View v) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
