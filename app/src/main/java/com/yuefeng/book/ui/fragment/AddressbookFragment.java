package com.yuefeng.book.ui.fragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.common.base.BaseMvpFragment;
import com.common.event.CommonEvent;
import com.common.utils.Constans;
import com.common.utils.ToastUtils;
import com.common.view.other.SideBar;
import com.yuefeng.commondemo.R;
import com.yuefeng.contacts.modle.contacts.ContactsBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by Administrator on 2018/7/11.
 * 通讯录
 */

public class AddressbookFragment extends BaseMvpFragment {

    Unbinder unbinder;
    @BindView(R.id.listView)
    ListView mListView;
    @BindView(R.id.side_bar)
    SideBar sideBar;
    private ArrayList<ContactsBean> list;


    @Override
    protected int getLayoutId() {
        return R.layout.module_fragment_addressbook;
    }


    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, rootView);
    }

    @Override
    protected void initData() {
        initListData();
        sideBar.setOnStrSelectCallBack(new SideBar.ISideBarSelectCallBack() {
            @Override
            public void onSelectStr(int index, String selectStr) {
                for (int i = 0; i < list.size(); i++) {
                    if (selectStr.equalsIgnoreCase(list.get(i).getFirstLetter())) {
                        mListView.setSelection(i); // 选择到首字母出现的位置
                        return;
                    }
                }
            }
        });
    }

    private void initListData() {
        list = new ArrayList<>();
//        Collections.sort(list); // 对list进行排序，需要让User实现Comparable接口重写compareTo方法
//        SortBookAdapter adapter = new SortBookAdapter(getContext(), list);
//        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToastUtils.showToast(list.get(position).getName());
            }
        });
    }


    @Override
    protected void fetchData() {
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void disposeCommonEvent(CommonEvent commonEvent) {
        switch (commonEvent.getWhat()) {
            case Constans.COMMON:
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
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
