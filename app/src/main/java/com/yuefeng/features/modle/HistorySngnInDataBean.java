package com.yuefeng.features.modle;

import java.io.Serializable;
import java.util.List;

/*签到历史*/
public class HistorySngnInDataBean implements Serializable {


    /**
     * success : true
     * msgTitle : 成功提示
     */

    private boolean success;
    private String msgTitle;
    private List<HistorySngnInListDataBean> msg;

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

    public List<HistorySngnInListDataBean> getMsg() {
        return msg;
    }

    public void setMsg(List<HistorySngnInListDataBean> msg) {
        this.msg = msg;
    }
}
