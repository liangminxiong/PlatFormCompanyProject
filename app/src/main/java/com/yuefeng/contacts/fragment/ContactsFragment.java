package com.yuefeng.contacts.fragment;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.common.base.codereview.BaseFragment;
import com.common.utils.Constans;
import com.yuefeng.commondemo.R;
import com.yuefeng.contacts.adapter.ContactsAdapter;
import com.yuefeng.contacts.modle.contacts.OrganPersonalDataBean;
import com.yuefeng.contacts.modle.contacts.OrganlistBean;
import com.yuefeng.contacts.ui.activity.FirstGroupNameActivity;
import com.yuefeng.contacts.ui.activity.SelectPersonalWhoActivity;
import com.yuefeng.contacts.ui.activity.UserDetailInfosActivity;
import com.yuefeng.login_splash.contract.SignInContract;
import com.yuefeng.login_splash.event.SignInEvent;
import com.yuefeng.login_splash.presenter.SignInPresenter;
import com.yuefeng.ui.MainActivity;
import com.yuefeng.ui.MyApplication;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/*通讯录*/
public class ContactsFragment extends BaseFragment implements SignInContract.View {
    Unbinder unbinder;

    //    @BindView(R.id.iv_logo)
//    ImageView ivLogo;
//    @BindView(R.id.tv_name)
//    TextView tvName;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private List<OrganlistBean> mListData = new ArrayList<>();
    private ContactsAdapter mAdapter;
    private SignInPresenter presenter;
    private int typeOnclick = 0;
    private String mName;
    private String mUserId;
    private int mCount;


    @Override
    protected int getLayoutId() {
        return R.layout.module_fragment_contacts;
    }


    @Override
    protected void initView() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        unbinder = ButterKnife.bind(this, rootView);
        presenter = new SignInPresenter(this, (MainActivity) getActivity());
        initRecyclerView();
        typeOnclick = 0;
    }

    private void initRecyclerView() {
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ContactsAdapter(R.layout.recyclerview_item_contacts, mListData);
        recyclerview.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                typeOnclick = 1;
                OrganlistBean contactsBean = mListData.get(position);
                if (contactsBean != null) {
                    mName = contactsBean.getName();
                    boolean isorgan = contactsBean.isIsorgan();
                    if (isorgan) {
                        getWhatGroupDatasByNet(contactsBean.getId());
                    } else {
                        toUserDetailActivity(contactsBean.getId());
                    }
                }

            }
        });
    }

    private void toUserDetailActivity(String id) {
        Intent intent = new Intent();
        intent.setClass(getContext(), UserDetailInfosActivity.class);
        intent.putExtra(Constans.GROUPID, id);
        startActivity(intent);
    }

    /*跳转*/
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void toActivity(OrganPersonalDataBean bean) {
        typeOnclick = 0;
        Intent intent = new Intent();
        if (bean != null) {
            List<OrganlistBean> listData = new ArrayList<>();
            boolean isorgan = bean.isIsorgan();//是否机构
            boolean haschild = bean.isHaschild();//是否有人
            List<OrganlistBean> organlist = bean.getOrganlist();
            List<OrganlistBean> userlist = bean.getUserlist();
            int sizeOrgan = organlist.size();
            int sizeUser = userlist.size();
            if (sizeOrgan == 0 && sizeUser == 0) {
                showSuccessToast("旗下无机构和人");
                return;
            }

            if (sizeOrgan > 0) {
                listData.addAll(organlist);
                intent.setClass(Objects.requireNonNull(getContext()), FirstGroupNameActivity.class);
            } else {
                if (sizeUser > 0) {
                    intent.setClass(Objects.requireNonNull(getContext()), SelectPersonalWhoActivity.class);
                }
            }
            if (sizeUser > 0) {
                listData.addAll(userlist);
            }

            intent.putExtra(Constans.GROUPNAME, mName);
            intent.putExtra(Constans.GROUPID, (Serializable) listData);
            startActivity(intent);
        }
    }


    @Override
    protected void initData() {

    }


    @Override
    protected void fetchData() {
        mCount = 0;
        mUserId = "dg1168";
        getWhatGroupDatasByNet(mUserId);
    }

    private void getWhatGroupDatasByNet(String userId) {
        boolean networkConnected = MyApplication.getInstance().isNetworkConnected();
        if (!networkConnected) {
            return;
        }
        if (presenter != null) {
            presenter.findOrganWithID(userId, "", 0);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void disposeSignInEvent(SignInEvent event) {
        switch (event.getWhat()) {
            case Constans.CONTACTS_SUCCESS://获取通讯录成功
                OrganPersonalDataBean bean = (OrganPersonalDataBean) event.getData();
                if (typeOnclick == 0) {
                    addData(bean);
                } else {
                    toActivity(bean);
                }
                break;
            case Constans.CONTACTS_ERROR:
                mCount++;
                if (mCount > 4) {
                    showSureGetAgainDataDialog("数据加载失败，是否重新加载?");
                }
                break;
        }
    }

    @Override
    public void getDatasAgain() {
        super.getDatasAgain();
        getWhatGroupDatasByNet(mUserId);
    }

    /*添加数据*/
    private void addData(OrganPersonalDataBean bean) {
        mListData.clear();
        if (bean != null) {
            List<OrganlistBean> organlist = bean.getOrganlist();
            int sizeOrgan = organlist.size();
            if (sizeOrgan > 0) {
                mListData.addAll(organlist);
            }
            List<OrganlistBean> userlist = bean.getUserlist();
            int sizeUser = userlist.size();
            if (sizeUser > 0) {
                mListData.addAll(userlist);
            }
        }
        if (mAdapter != null) {
            mAdapter.setNewData(mListData);
        }
    }

    @Override
    protected void setLisenter() {

    }

    @Override
    protected void widgetClick(View v) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}