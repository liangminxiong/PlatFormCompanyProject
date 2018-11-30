package com.yuefeng.home.modle;

import java.io.Serializable;
import java.util.List;

public class NewMsgDataBean implements Serializable {


    /**
     * success : true
     * msgTitle : 成功提示
     */

    private boolean success;
    private String msgTitle;
    private List<NewMsgListDataBean> msg;

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

    public List<NewMsgListDataBean> getMsg() {
        return msg;
    }

    public void setMsg(List<NewMsgListDataBean> msg) {
        this.msg = msg;
    }

}
