package com.yuefeng.features.modle;

import java.io.Serializable;
import java.util.List;

public class EventQuestionBean implements Serializable {

    /*质量巡查*/
    /**
     * success : true
     * msgTitle : 成功提示
     * msg : [{"id":"8","pid":"dg1168","address":"地址","problem":"问题",
     * "uploadpeople":"2a5a6f40ffa80103701e7165535ad91d","uploadtime":"2018-09-05 13:57:15.0",
     * "imgurl":"http://10.0.0.68:8080/webfiles/zgbd_rubbish/wxupload/image/2018090513571542540.png,",
     * "state":"1","longitude":"132.2222","latitude":"12.222","type":"0","uploadpeoplename":"侨银环保公司"}]
     */

    private boolean success;
    private String msgTitle;
    private List<EventQuestionMsgBean> msg;

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

    public List<EventQuestionMsgBean> getMsg() {
        return msg;
    }

    public void setMsg(List<EventQuestionMsgBean> msg) {
        this.msg = msg;
    }

}
