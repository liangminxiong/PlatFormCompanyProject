package com.yuefeng.features.modle;

import java.io.Serializable;

/*考勤总数*/
public class GetKaoqinSumBean implements Serializable {


    /**
     * success : true
     * msgTitle : 成功提示
     * msg : {"kaoqinsum":34,"late":0,"early":0,"daiqian":2,"qiaodao":32,"kuanggong":0,"signback":0}
     */

    private boolean success;
    private String msgTitle;
    private GetKaoqinSumMsgBean msg;

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

    public GetKaoqinSumMsgBean getMsg() {
        return msg;
    }

    public void setMsg(GetKaoqinSumMsgBean msg) {
        this.msg = msg;
    }

}
