package com.yuefeng.contacts.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.common.base.codereview.BaseActivity;
import com.common.event.CommonEvent;
import com.common.utils.Constans;
import com.common.view.other.SideBar;
import com.yuefeng.commondemo.R;
import com.yuefeng.contacts.adapter.SelectSortBookAdapter;
import com.yuefeng.contacts.modle.contacts.ContactsBean;
import com.yuefeng.contacts.modle.contacts.OrganlistBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/*通讯录单聊*/
public class SelectPersonalWhoActivity extends BaseActivity {

    @BindView(R.id.rl_search)
    RelativeLayout rl_search;
    @BindView(R.id.listView)
    ListView mListView;
    @BindView(R.id.edt_search)
    EditText mEdtSearch;
    @BindView(R.id.side_bar)
    SideBar sideBar;

    private List<ContactsBean> mListData = new ArrayList<>();
    private String mTitltName;
    private SelectSortBookAdapter mAdapter;

    private List<ContactsBean> mSelectList = new ArrayList<>();


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
        initUI();
    }

    private void initUI() {
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

                String key = charSequence.toString().trim();
                int length = key.length();
                if (length > 0) {
                    searchKeyList(key);
                } else {
                    initAdaterData();
                }

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
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initAdaterData() {
        mSelectList.clear();
        mSelectList.addAll(mListData);
        mAdapter = new SelectSortBookAdapter(SelectPersonalWhoActivity.this, mSelectList);
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
            mAdapter = new SelectSortBookAdapter(SelectPersonalWhoActivity.this, mSelectList);
            mListView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
    }


    private void addDatasInRecycler(List<OrganlistBean> listData) {
        for (OrganlistBean bean : listData) {
            ContactsBean contactsBean = new ContactsBean(bean.getName(), false);
            contactsBean.setUserId(bean.getId());
            contactsBean.setPid(bean.getPid());
            contactsBean.setIcon(bean.getIcon());
            contactsBean.setIsorgan(bean.isIsorgan());
            contactsBean.setHaschild(bean.isHaschild());
//            contactsBean.setOrganlist(bean.getOrganlist());
            mListData.add(contactsBean);
        }
        Collections.sort(mListData); // 对list进行排序，需要让User实现Comparable接口重写compareTo方法
        mListView.setDividerHeight(0);
        mListView.setDivider(null);
        mSelectList.addAll(mListData);
        mAdapter = new SelectSortBookAdapter(SelectPersonalWhoActivity.this, mSelectList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toUserDetailActivity(mSelectList.get(position).getUserId());
            }
        });
    }

    private void toUserDetailActivity(String id) {
        Intent intent = new Intent();
        intent.setClass(SelectPersonalWhoActivity.this, UserDetailInfosActivity.class);
        intent.putExtra(Constans.GROUPID, id);
        startActivity(intent);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        mTitltName = (String) bundle.get(Constans.GROUPNAME);
        List<OrganlistBean> listData = (List<OrganlistBean>) intent.getSerializableExtra(Constans.GROUPID);

        initListData(mTitltName, listData);
    }

    private void initListData(String name, List<OrganlistBean> listData) {
        setTitle(name);
        addDatasInRecycler(listData);
    }

    @Override
    protected void setLisenter() {

    }

    @Override
    protected void widgetClick(View v) {

    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void disposeCommonEvent(CommonEvent commonEvent) {
        switch (commonEvent.getWhat()) {
            case Constans.COMMON:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
