package com.yuefeng.contacts.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.base.codereview.BaseActivity;
import com.common.event.CommonEvent;
import com.common.utils.Constans;
import com.common.utils.PreferencesUtils;
import com.common.view.other.SideBar;
import com.yuefeng.commondemo.R;
import com.yuefeng.contacts.adapter.SelectSortBookAdapter;
import com.yuefeng.contacts.contract.FindAllUserContract;
import com.yuefeng.contacts.modle.contacts.ContactsBean;
import com.yuefeng.contacts.presenter.GreateSingleChatPresenter;
import com.yuefeng.rongIm.RongIMUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/*单聊*/
public class GreateSingleChatActivity extends BaseActivity implements FindAllUserContract.View {


    @BindView(R.id.tv_title_setting)
    TextView tv_setting;
    @BindView(R.id.edt_groupname)
    EditText edtGroupName;
    @BindView(R.id.listView)
    ListView mListView;
    @BindView(R.id.side_bar)
    SideBar sideBar;
    @BindView(R.id.edt_search)
    EditText mEdtSearch;
    @BindView(R.id.rl_search)
    RelativeLayout rl_search;

    private List<ContactsBean> mListData = new ArrayList<>();
    private List<ContactsBean> mSelectList = new ArrayList<>();

    private GreateSingleChatPresenter mPresenter;
    private SelectSortBookAdapter mAdapter;


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
        mPresenter = new GreateSingleChatPresenter(this, this);
        initUI();
    }

    private void initUI() {
        setTitle("发起聊天");
//        tv_setting.setText("确定");
        rl_search.setVisibility(View.GONE);
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

        initEdit();
    }

    private void initEdit() {
        mEdtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//                if (null != mAdapter) {
//                    mAdapter.getFilter().filter(charSequence);
//                }
//               也可以在这里筛选数据,但这不是异步的，有隐患，最好用系统提供的Filter类
//				 for (Iterator<String> iterator = mArrayList.iterator(); iterator
//							.hasNext();) {
//						String name = iterator.next();
//
//						if (name.contains(arg0)) {
//							mFilteredArrayList.add(name);
//						}
//				 mListViewAdapter.changeList(mFilteredArrayList);
//				 mListViewAdapter.notifyDataSetChanged();

                String key = charSequence.toString().trim();
                int length = key.length();
                if (length > 0) {
                    searchKeyList(key);
                } else {
                    initAdaterData();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initAdaterData() {
        mSelectList.clear();
        mSelectList.addAll(mListData);
        mAdapter = new SelectSortBookAdapter(GreateSingleChatActivity.this, mSelectList);
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private void searchKeyList(String key) {
        List<ContactsBean> checkList = new ArrayList<>();
        int size = mListData.size();
        if (size > 0) {
            for (ContactsBean bean : mListData) {
                if (bean.getName().contains(key)) {
                    checkList.add(bean);
                }
            }
        }
        if (checkList.size() > 0) {
            mSelectList.clear();
            mSelectList.addAll(checkList);
        }

        if (mAdapter != null) {
            mAdapter = new SelectSortBookAdapter(GreateSingleChatActivity.this, mSelectList);
            mListView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
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
        mSelectList.addAll(mListData);
        mAdapter = new SelectSortBookAdapter(GreateSingleChatActivity.this, mSelectList);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
                startChat(mSelectList.get(position).getUserId(), mSelectList.get(position).getName());
            }

        });
    }

    private void startChat(String userId, String name) {
        RongIMUtils.startPrivateChat(GreateSingleChatActivity.this, userId, name);
    }

    @Override
    protected void initData() {
        if (mPresenter != null) {
            String userid = PreferencesUtils.getString(GreateSingleChatActivity.this, Constans.ID, "");
            mPresenter.findAllUser(1, 10000, "", 0, userid);
        }
    }


    @Override
    protected void setLisenter() {

    }

    @Override
    protected void widgetClick(View v) {

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
//                showSureGetAgainDataDialog("创建群聊成功，是否开始聊天?");
                break;
        }
    }

    @Override
    public void getDatasAgain() {
        super.getDatasAgain();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
