package com.yuefeng.personaltree.model;

import java.io.Serializable;
import java.util.List;

public class PersoanlTreeListBean implements Serializable {


    /**
     * success : true
     * msgTitle : 成功提示
     *  msg
     */

    private boolean success;
    private String msgTitle;
    private List<PersonalParentBean> msg;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsgTitle() {
        return msgTitle;
    }

    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle;
    }

    public List<PersonalParentBean> getMsg() {
        return msg;
    }

    public void setMsg(List<PersonalParentBean> msg) {
        this.msg = msg;
    }
}
