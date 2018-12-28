package com.yuefeng.features.modle;

import java.io.Serializable;


/*排班时间*/
public class GetWorkTimeBean implements Serializable {


    /**
     * success : true
     * msgTitle : 成功提示
     * msg : {"detailtime":"","isupdate":"1"}
     */

    private boolean success;
    private String msgTitle;
    private GetWorkTimeMsgBean msg;

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

    public GetWorkTimeMsgBean getMsg() {
        return msg;
    }

    public void setMsg(GetWorkTimeMsgBean msg) {
        this.msg = msg;
    }

}
