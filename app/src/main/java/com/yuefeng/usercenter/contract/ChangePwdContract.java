package com.yuefeng.usercenter.contract;


import com.common.base.codereview.BaseView;

/**
 修改密码
 */

public interface ChangePwdContract {
    interface View extends BaseView<Object> {}
    interface Presenter{

        void changePwd(String function,String userid,String newPwd,String oldPwd);
    }
}
