package com.yuefeng.features.modle;

import java.io.Serializable;
import java.util.List;

public class GetAllPersonalBean implements Serializable {
/*获取所有主管*/

    /**
     * success : true
     * msgTitle : 成功提示
     * msg : [{"name":"张三","id":"eab2ffacffffffc976ce7286d4054823"}]
     */

    private boolean success;
    private String msgTitle;
    private List<GetAllPersonalMsgBean> msg;

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

    public List<GetAllPersonalMsgBean> getMsg() {
        return msg;
    }

    public void setMsg(List<GetAllPersonalMsgBean> msg) {
        this.msg = msg;
    }

}
