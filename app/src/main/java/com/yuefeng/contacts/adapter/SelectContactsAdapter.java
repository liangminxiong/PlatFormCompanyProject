package com.yuefeng.contacts.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yuefeng.commondemo.R;
import com.yuefeng.contacts.modle.contacts.ContactsBean;

import java.util.List;

/*通讯录*/
public class SelectContactsAdapter extends BaseItemDraggableAdapter<ContactsBean, BaseViewHolder> {

    private int mIcon;
    private boolean mGoneOrVisible;

    public SelectContactsAdapter(int layoutResId, @Nullable List<ContactsBean> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, ContactsBean item) {
        if (item != null) {
            boolean isorgan = item.isIsorgan();
            if (isorgan) {
                mIcon = R.drawable.work;
                mGoneOrVisible = true;
            } else {
                mIcon = R.drawable.item;
                mGoneOrVisible = false;
            }
            helper.setText(R.id.iv_item_logo, item.getName())
                    .setText(R.id.tv_item_name, item.getName())
                    .setVisible(R.id.iv_item_next, mGoneOrVisible);

        }
    }
}
