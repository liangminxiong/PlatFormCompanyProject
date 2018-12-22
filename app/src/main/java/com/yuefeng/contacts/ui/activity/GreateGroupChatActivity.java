package com.yuefeng.contacts.ui.activity;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.common.base.codereview.BaseActivity;
import com.common.event.CommonEvent;
import com.common.utils.Constans;
import com.common.utils.LogUtils;
import com.common.utils.PreferencesUtils;
import com.common.view.other.SideBar;
import com.yuefeng.commondemo.R;
import com.yuefeng.contacts.adapter.SortBookAdapter;
import com.yuefeng.contacts.contract.FindAllUserContract;
import com.yuefeng.contacts.modle.contacts.ContactsBean;
import com.yuefeng.contacts.modle.groupchat.GroupCreateBean;
import com.yuefeng.contacts.presenter.FindAllUserPresenter;
import com.yuefeng.rongIm.RongIMUtils;
import com.yuefeng.ui.MyApplication;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imlib.model.Group;


/*创建群组*/
public class GreateGroupChatActivity extends BaseActivity implements FindAllUserContract.View {


    @BindView(R.id.tv_title_setting)
    TextView tv_setting;
    @BindView(R.id.edt_groupname)
    EditText edtGroupName;
    @BindView(R.id.listView)
    ListView mListView;
    @BindView(R.id.side_bar)
    SideBar sideBar;

    private List<ContactsBean> mListData = new ArrayList<>();
    private FindAllUserPresenter mPresenter;
    private SortBookAdapter mAdapter;
    private String mGroupID;
    private String createuserid = "";
    private String mUserId;
    private String mGroupName;


    @Override
    protected int getContentViewResId() {
        return R.layout.module_fragment_addressbook;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        ButterKnife.bind(this);
        mPresenter = new FindAllUserPresenter(this, this);
        initUI();
    }

    private void initUI() {
        setTitle("发起群聊");
        tv_setting.setText("确定");
        sideBar.setOnStrSelectCallBack(new SideBar.ISideBarSelectCallBack() {
            @Override
            public void onSelectStr(int index, String selectStr) {
                for (int i = 0; i < mListData.size(); i++) {
                    if (selectStr.equalsIgnoreCase(mListData.get(i).getFirstLetter())) {
                        mListView.setSelection(i); // 选择到首字母出现的位置
                        return;
                    }
                }
            }
        });

    }


    private void addDatasInRecycler(List<ContactsBean> list) {
        for (ContactsBean bean : list) {
            ContactsBean contactsBean = new ContactsBean(bean.getName(), false);
            contactsBean.setUserId(bean.getUserId());
            contactsBean.setPid(bean.getPid());
            contactsBean.setIcon(bean.getIcon());
            contactsBean.setIsorgan(bean.isIsorgan());
            contactsBean.setHaschild(bean.isHaschild());
            contactsBean.setOrganlist(bean.getOrganlist());
            mListData.add(contactsBean);
        }
        Collections.sort(mListData); // 对list进行排序，需要让User实现Comparable接口重写compareTo方法
        mListView.setDividerHeight(0);
        mListView.setDivider(null);
        mAdapter = new SortBookAdapter(GreateGroupChatActivity.this, mListData);
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        if (mPresenter != null) {
            String userid = PreferencesUtils.getString(GreateGroupChatActivity.this, Constans.ID, "");
            mPresenter.findAllUser(1, 10000, "", 0,userid);
        }
    }


    @Override
    protected void setLisenter() {

    }

    @Override
    protected void widgetClick(View v) {

    }

    @OnClick(R.id.tv_title_setting)
    public void sure() {
        mGroupName = edtGroupName.getText().toString().trim();
        if (TextUtils.isEmpty(mGroupName)) {
            showSuccessToast("请输入群组名称");
            return;
        }
        mUserId = PreferencesUtils.getString(GreateGroupChatActivity.this, Constans.ID, "");

        if (mAdapter != null) {
            List<String> selectedList = mAdapter.getIsSelected();
            int size = selectedList.size();
            if (size > 1) {
                for (int i = 0; i < size; i++) {
                    if (i == 0) {
                        createuserid = mUserId + "," + selectedList.get(i);
                    } else {
                        createuserid = createuserid + "," + selectedList.get(i);
                    }
                }
            } else if (size > 0) {
                createuserid = mUserId + "," + selectedList.get(0);
            } else {
                createuserid = mUserId;
            }
        }
        groupCreate(mUserId, createuserid, mGroupName);
    }

    /*创建组*/
    private void groupCreate(String userIds, String createuserid, String grunpName) {
        boolean networkConnected = MyApplication.getInstance().isNetworkConnected();
        if (!networkConnected) {
            return;
        }
        LogUtils.d("=======" + userIds + "===" + createuserid + "==" + grunpName);
        if (mPresenter != null) {
            mPresenter.groupCreate(userIds, createuserid, grunpName);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void disposeCommonEvent(CommonEvent event) {
        switch (event.getWhat()) {
            case Constans.ALLUSER_SUCCESS:
                List<ContactsBean> list = (List<ContactsBean>) event.getData();
                if (list.size() > 0) {
                    addDatasInRecycler(list);
                }
                break;
            case Constans.ALLUSER_ERROR:
                break;

            case Constans.GROUPCREATE_SUCCESS:
                GroupCreateBean groupCreateBean = (GroupCreateBean) event.getData();
                mGroupID = groupCreateBean.getData();
                String title = groupCreateBean.getText();
                LogUtils.d("=====创群成功" + mGroupID);
                RongIMUtils.startGroupChat(GreateGroupChatActivity.this, mGroupID, title);
                Group group = new Group(mGroupID, mGroupName, Uri.parse(""));
                RongIMUtils.refreshGroupInfoCache(group);
                break;
            case Constans.GROUPCREATE_ERROR:
                showSureGetAgainDataDialog("创群失败，是否请重试?");
                break;
        }
    }

    @Override
    public void getDatasAgain() {
        super.getDatasAgain();
        groupCreate(mUserId, createuserid, mGroupName);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
