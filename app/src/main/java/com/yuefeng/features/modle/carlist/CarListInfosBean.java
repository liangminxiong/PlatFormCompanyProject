package com.yuefeng.features.modle.carlist;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/7/20.
 */

public class CarListInfosBean implements Serializable {


    /**
     * success : true
     * msgTitle : 成功提示
     * msg : [
     */

    private boolean success;
    private String msgTitle;
    private List<CarListInfosMsgBean> msg;

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

    public List<CarListInfosMsgBean> getMsg() {
        return msg;
    }

    public void setMsg(List<CarListInfosMsgBean> msg) {
        this.msg = msg;
    }
}
