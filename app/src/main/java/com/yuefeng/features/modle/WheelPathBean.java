package com.yuefeng.features.modle;

import java.util.List;

/**
 * Created by Administrator on 2018/5/28.
 */

public class WheelPathBean {


    /**
     * success : true
     * msgTitle : 成功提示
     * msg : []
     */

    private boolean success;
    private String msgTitle;
    private List<WheelPathDatasBean> msg;

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

    public List<WheelPathDatasBean> getMsg() {
        return msg;
    }

    public void setMsg(List<WheelPathDatasBean> msg) {
        this.msg = msg;
    }

}
