package com.yuefeng.features.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.base.codereview.BaseActivity;
import com.common.network.ApiService;
import com.common.utils.Constans;
import com.common.utils.PreferencesUtils;
import com.common.view.other.StarLinearLayout;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.contract.EvaluationContract;
import com.yuefeng.features.event.EvaluationEvent;
import com.yuefeng.features.presenter.EvaluationPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/*问题关闭*/
public class EvaluationEventActivity extends BaseActivity implements EvaluationContract.View {

    @BindView(R.id.iv_back)
    RelativeLayout iv_back;
    @BindView(R.id.tv_back)
    TextView tv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindColor(R.color.titel_color)
    int coloeWhite;
    @BindColor(R.color.titel_color)
    int coloeGray;
    private static final String TAG = "tag";
    @BindView(R.id.ll_problem)
    LinearLayout ll_problem;
    @BindView(R.id.edt_problem_txt)
    EditText edt_problem_txt;
    @BindView(R.id.tv_txt_count)
    TextView tv_txt_count;
    @BindView(R.id.star)
    StarLinearLayout mStar;
    @BindView(R.id.tv_score)
    TextView tv_score;
    @BindView(R.id.tv_upload)
    ImageView tv_upload;

    private String problemid;
    private AlertDialog alertDilaog;
    private String orgId;
    private String userId;
    private EvaluationPresenter presenter;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_evaluationevent;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        ButterKnife.bind(this);
        presenter = new EvaluationPresenter(this, this);

//        View view = findViewById(R.id.space);
//
//        view.setBackground(mActivity.getResources().getDrawable(R.drawable.title_toolbar_bg_blue));
//        StatusBarUtil.setFadeStatusBarHeight(mActivity, view);
        tv_title.setText(R.string.problem_evaluation);
        tv_back.setText("返回");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            problemid = (String) bundle.get("PROBLEMID");
        }

        mStar.setScore((float) 5);
        tv_score.setText("非常好");
        mStar.setChangeListener(new StarLinearLayout.ChangeListener() {
            String string = "";

            @Override
            public void Change(int level) {
                if (level == 1) {
                    string = "非常差";
                } else if (level == 2) {
                    string = "差";
                } else if (level == 3) {
                    string = "一般";
                } else if (level == 4) {
                    string = "好";
                } else {
                    string = "非常好";
                }
                tv_score.setText(string);
            }
        });
    }


    @Override
    public void initData() {
        initEditText();
    }

    @Override
    protected void setLisenter() {

    }

    @Override
    protected void widgetClick(View v) {

    }


    private void initEditText() {
        edt_problem_txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();
                if (length >= 0) {
                    EventBus.getDefault().post(new EvaluationEvent(Constans.ETIDEXT_SUCESS, String.valueOf(100 - length)));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void disposeEvaluationEvent(EvaluationEvent event) {
        dismissLoadingDialog();
        switch (event.getWhat()) {
            case Constans.ETIDEXT_SUCESS:
                String count = (String) event.getData();
                tv_txt_count.setText("还可以输入" + count + "字");
                break;

            case Constans.CARRY_SUCESS:
                String msg = (String) event.getData();
                if (msg.contains(getString(R.string.submit_success))) {
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    showSuccessDialog("关闭问题成功，退出当前界面?");
                } else {
                    showErrorToast("提交失败，请重试！");
                }
                break;
            default:
                showErrorToast("操作失败，请重试");
                break;
        }
    }


    @OnClick(R.id.tv_upload)
    public void tv_upload() {
        String problem = edt_problem_txt.getText().toString().trim();
        if (TextUtils.isEmpty(problem)) {
            showSuccessToast("请填写问题描述");
            return;
        }

        orgId = PreferencesUtils.getString(this, "orgId", "");
        userId = PreferencesUtils.getString(this, "id", "");
        closedEvent(orgId, userId, problem);
    }


    /*关闭*/
    private void closedEvent(String orgId, String userId, String problem) {

        String pinjia = tv_score.getText().toString();
        showLoadingDialog(getString(R.string.closing));
        presenter.updatequestions(ApiService.UPDATEQUESTIONS, userId, problemid,
                "4", "", problem, pinjia, "");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
