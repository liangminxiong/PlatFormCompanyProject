package com.yuefeng.features.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.common.base.codereview.BaseActivity;
import com.common.event.CommonEvent;
import com.common.utils.Constans;
import com.common.utils.LogUtils;
import com.common.utils.StringUtils;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.modle.PersonalinfoListBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*人员详情*/
public class PersonalDetailInfosActivity extends BaseActivity {


    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_banzu)
    TextView tvBanzu;
    @BindView(R.id.tv_zhuguan)
    TextView tvZhuguan;
    @BindView(R.id.tv_speed)
    TextView tvSpeed;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_islocation)
    TextView tvIslocation;
    @BindView(R.id.tv_ang)
    TextView tvAng;
    /*@BindView(R.id.tv_ternumber)
    TextView tvTernumber;
    @BindView(R.id.tv_tertype)
    TextView tvTertype;
    @BindView(R.id.tv_hangtype)
    TextView tvHangtype;
    @BindView(R.id.tv_cartype)
    TextView tvCartype;
    @BindView(R.id.tv_cheping)
    TextView tvCheping;
    @BindView(R.id.tv_chexing_hao)
    TextView tvChexingHao;
    @BindView(R.id.tv_oilsmax)
    TextView tvOilsmax;
    @BindView(R.id.tv_oilschuan)
    TextView tvOilschuan;
    @BindView(R.id.tv_zaimax)
    TextView tvZaimax;
    @BindView(R.id.tv_seatcount)
    TextView tvSeatcount;
    @BindView(R.id.tv_oilsguan)
    TextView tvOilsguan;*/
    private PersonalinfoListBean mBean;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_pesonaldetailinfos;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        setTitle("车辆详情");
    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        mBean = (PersonalinfoListBean) bundle.getSerializable(Constans.GROUPID);
        if (mBean != null) {
            showUIData(mBean);
        }
    }

    private void showUIData(PersonalinfoListBean bean) {
        String vid = bean.getId();
        LogUtils.d("===vid===" + vid);
        String registrationNO = StringUtils.isEntryStrWu(bean.getName());
        String time = StringUtils.isEntryStrXieg(bean.getTime());
        String phone = StringUtils.isEntryStrXieg(bean.getTel());
        String banzu = StringUtils.isEntryStrXieg(bean.getPid());
        String zuguan = StringUtils.isEntryStrXieg("");
        String speed = StringUtils.isEntryStrXieg(bean.getSpeed());
        String address = StringUtils.isEntryStrXieg(bean.getAddress());
        String location = "";
        if (TextUtils.isEmpty(address)) {
            location = "-";
        } else {
            location = "定位";
        }
        location = StringUtils.isEntryStrXieg(location);
        String ang = StringUtils.isEntryStrXieg("");
        if (!speed.equals("-")) {
            speed = speed + "km/h";
        }

        setTitle(registrationNO);
        tvTime.setText(time);
        tvPhone.setText(phone);
        tvBanzu.setText(banzu);
        tvZhuguan.setText(zuguan);
        tvSpeed.setText(speed);
        tvAddress.setText(address);
        tvIslocation.setText(location);
        tvAng.setText(ang);
    }


    @OnClick(R.id.tv_phone)
    public void onClick() {
        String phone = tvPhone.getText().toString().trim();
        StringUtils.callPhone(PersonalDetailInfosActivity.this, phone);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void disposeCommonEvent(CommonEvent event) {
        switch (event.getWhat()) {
            case Constans.CARDETAIL_SUCCESS://树
//                treeListData.clear();
//                treeListData = (List<PersonalParentBean>) event.getData();
//                if (treeListData.size() > 0) {
//                    showPersonallistDatas(treeListData);
//                } else {
//                    showSuccessToast("旗下无主管");
//                }
                break;

            case Constans.CARDETAIL_ERROR:
                showSureGetAgainDataDialog("加载数据失败,是否重新加载?");
                break;
        }
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
        EventBus.getDefault().unregister(this);
    }

}
