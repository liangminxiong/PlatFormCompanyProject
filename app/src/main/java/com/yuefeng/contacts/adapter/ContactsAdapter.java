package com.yuefeng.contacts.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.common.utils.StringUtils;
import com.yuefeng.commondemo.R;
import com.yuefeng.contacts.modle.contacts.OrganlistBean;

import java.util.List;

/*通讯录*/
public class ContactsAdapter extends BaseItemDraggableAdapter<OrganlistBean, BaseViewHolder> {

    private boolean mGoneOrVisible;
    private String mName;
    private String mItemName;

    public ContactsAdapter(int layoutResId, @Nullable List<OrganlistBean> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, OrganlistBean item) {
        if (item != null) {
            boolean isorgan = item.isIsorgan();
            mItemName = item.getName();
            if (isorgan) {
                mGoneOrVisible = true;
                mName = StringUtils.returnOrganTwoLenght(mItemName);
            } else {
                mName = StringUtils.returnUserTwoLenght(mItemName);
                mGoneOrVisible = false;
            }
            helper.setText(R.id.iv_item_logo, mName)
                    .setText(R.id.tv_item_name, mItemName)
                    .setVisible(R.id.iv_item_next, mGoneOrVisible);

        }
    }
}
