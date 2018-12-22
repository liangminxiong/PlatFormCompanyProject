package com.yuefeng.contacts.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
    @BindView(R.id.side_bar)
    SideBar sideBar;

    private List<ContactsBean> mListData = new ArrayList<>();
    private String mTitltName;

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
        SelectSortBookAdapter adapter = new SelectSortBookAdapter(SelectPersonalWhoActivity.this, mListData);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toUserDetailActivity(mListData.get(position).getUserId());
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
