package com.yuefeng.contacts.ui.activity;

import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
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
    @BindView(R.id.ll_left_search)
    LinearLayout ll_left_search;
    @BindView(R.id.edt_groupname)
    EditText edtGroupName;
    @BindView(R.id.listView)
    ListView mListView;
    @BindView(R.id.side_bar)
    SideBar sideBar;
    @BindView(R.id.edt_search)
    EditText mEdtSearch;

    private List<ContactsBean> mListData = new ArrayList<>();
    private FindAllUserPresenter mPresenter;
    private SortBookAdapter mAdapter;
    private String mGroupID;
    private String groupUserids = "";
    private String mUserId;
    private String mGroupName;
    private String mText;
    private List<String> checkList = new ArrayList<>();
    private StringBuffer mStringBuffer;
    private SortBookAdapter.ViewHolder mViewHolder;

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
        ll_left_search.setVisibility(View.GONE);
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

//        initEdit();
    }

    private void initEdit() {
        mEdtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    initDateFalse();
                } else {
                    initDateTure();
                }
                if (null != mAdapter) {
                    mAdapter.getFilter().filter(charSequence);
                }
//               也可以在这里筛选数据,但这不是异步的，有隐患，最好用系统提供的Filter类
//				 for (Iterator<String> iterator = mArrayList.iterator(); iterator
//							.hasNext();) {
//						String name = iterator.next();
//						if (name.contains(arg0)) {
//							mFilteredArrayList.add(name);
//						}
//				 mListViewAdapter.changeList(mFilteredArrayList);
//				 mListViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    // 初始化isSelected的数据
    private void initDateTure() {
        for (String string : checkList) {
            LogUtils.d("======id=" + string);
            SortBookAdapter.getIsSelected().put(string, true);
        }
    }// 初始化isSelected的数据

    private void initDateFalse() {
        for (int i = 0; i < mListData.size(); i++) {
            SortBookAdapter.getIsSelected().put(mListData.get(i).getUserId(), false);
        }
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

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
                mViewHolder = (SortBookAdapter.ViewHolder) view.getTag();
                mViewHolder.cb_tiem.toggle();// 把CheckBox的选中状态改为当前状态的反,gridview确保是单一选中

                String userId = mListData.get(position).getUserId();

                SortBookAdapter.getIsSelected().put(userId, mViewHolder.cb_tiem.isChecked());//将CheckBox的选中状况记录下来
                if (mViewHolder.cb_tiem.isChecked()) {
//                    int size = checkList.size();
//                    if (size > 0) {
//                        for (int i = 0; i < size; i++) {
//                            String uid = checkList.get(i);
//                            if (!uid.equals(userId)) {
//                                checkList.add(userId);
//                                LogUtils.d("====uid22=" + uid + " += " + userId + " ++ " + checkList.size());
//                            }
//                        }
//                    } else {
//                        LogUtils.d("====uid11=" +  " += " + userId);
//
//                    }
                    checkList.add(userId);
                } else {
                    for (int i = checkList.size() - 1; i >= 0; i--) {
                        String item = checkList.get(i);
                        if (userId.equals(item)) {
                            checkList.remove(item);
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void initData() {
        if (mPresenter != null) {
            String userid = PreferencesUtils.getString(GreateGroupChatActivity.this, Constans.ID, "");
            mPresenter.findAllUser(1, 10000, "", 0, userid);
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

        mUserId = PreferencesUtils.getString(GreateGroupChatActivity.this, Constans.ID, "");
        if (null == mStringBuffer) {
            mStringBuffer = new StringBuffer();
        }
        mStringBuffer.setLength(0);
        int size = checkList.size();
        if (size == 0) {
            showSuccessToast("请先选择人");
            return;
        }
        if (size > 1) {
            for (String userid : checkList) {
                if (mStringBuffer.length() == 0) {
                    mStringBuffer.append(mUserId);
                } else {
                    mStringBuffer.append(",").append(userid);
                }
            }
        } else {
            mStringBuffer.append(mUserId).append(",").append(checkList.get(0));
        }

        groupUserids = mStringBuffer.toString().trim();

        mGroupName = edtGroupName.getText().toString().trim();
        if (TextUtils.isEmpty(mGroupName)) {
//            showSuccessToast("请输入群组名称");
            return;
        }


        groupCreate(groupUserids, mUserId, mGroupName);
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
                mText = groupCreateBean.getText();

                showSureGetAgainDataDialog("创建群聊成功，是否开始聊天?");

                LogUtils.d("=====创群成功" + mGroupID);

                break;
            case Constans.GROUPCREATE_ERROR:
                showSuccessToast("创群失败，请重试");
                break;
        }
    }

    @Override
    public void getDatasAgain() {
        super.getDatasAgain();
        Group group = new Group(mGroupID, mGroupName, Uri.parse(""));
        RongIMUtils.startGroupChat(GreateGroupChatActivity.this, mGroupID, mGroupName);
        RongIMUtils.refreshGroupInfoCache(group);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
