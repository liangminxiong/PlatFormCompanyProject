package com.yuefeng.features.modle;

import java.io.Serializable;
import java.util.List;

public class LllegalworkBean implements Serializable {


    private boolean success;
    private String msgTitle;
    private List<LllegalworMsgBean> msg;

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

    public List<LllegalworMsgBean> getMsg() {
        return msg;
    }

    public void setMsg(List<LllegalworMsgBean> msg) {
        this.msg = msg;
    }

}
