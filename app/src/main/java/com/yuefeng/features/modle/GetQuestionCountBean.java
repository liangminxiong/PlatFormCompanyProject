package com.yuefeng.features.modle;

import java.io.Serializable;

public class GetQuestionCountBean implements Serializable {


    /**
     * success : true
     * msgTitle : 成功提示
     * msg : {"allcount":"0","waitcount":"0","doingcount":"2","waitclosecount":"1","finishcount":"0"}
     */

    private boolean success;
    private String msgTitle;
    private GetQuestionCountMsgBean msg;

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

    public GetQuestionCountMsgBean getMsg() {
        return msg;
    }

    public void setMsg(GetQuestionCountMsgBean msg) {
        this.msg = msg;
    }

}
