package com.yuefeng.home.ui.modle;

import java.io.Serializable;
import java.util.List;

/*公告消息列表*/
public class AnnouncementDataBean implements Serializable {
    /**
     * success : true
     * msgTitle : 成功提示
     */

    private boolean success;
    private String msgTitle;
    private List<AnnouncementDataMsgBean> msg;

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

    public List<AnnouncementDataMsgBean> getMsg() {
        return msg;
    }

    public void setMsg(List<AnnouncementDataMsgBean> msg) {
        this.msg = msg;
    }

}
