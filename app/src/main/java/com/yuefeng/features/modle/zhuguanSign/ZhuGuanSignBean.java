package com.yuefeng.features.modle.zhuguanSign;

import java.io.Serializable;
import java.util.List;

/*主管考勤*/
public class ZhuGuanSignBean implements Serializable {


    /**
     * success : true
     * msgTitle : 成功提示
     * msg : [{"id":"eab2ffacffffffc976ce7286d4054823","time":"2018-12-25 11:28:00.0","lng":"113.402255","lat":"23.154967","name":"张三","tel":"1377777"}]
     */

    private boolean success;
    private String msgTitle;
    private List<ZhuGuanSignListBean> msg;

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

    public List<ZhuGuanSignListBean> getMsg() {
        return msg;
    }

    public void setMsg(List<ZhuGuanSignListBean> msg) {
        this.msg = msg;
    }

}
