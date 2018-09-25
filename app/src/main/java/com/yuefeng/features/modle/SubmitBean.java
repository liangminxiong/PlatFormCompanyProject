package com.yuefeng.features.modle;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/3/22.
 */

public class SubmitBean implements Serializable{

    /**
     * success : true
     * msgTitle : 成功提示
     * msg : 操作成功!
     * data :
     */

    private boolean success;
    private String msgTitle;
    private String msg;
    private String data;

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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
