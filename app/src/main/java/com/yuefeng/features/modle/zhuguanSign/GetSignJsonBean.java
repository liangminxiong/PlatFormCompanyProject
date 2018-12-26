package com.yuefeng.features.modle.zhuguanSign;

import java.io.Serializable;
import java.util.List;

/*获取主管id列表*/
public class GetSignJsonBean implements Serializable {


    /**
     * success : true
     * msgTitle : 成功提示
     * msg : [{"id":"eab2ffacffffffc976ce7286d4054823","pid":"ea9b4033ffffee0101ed1860a1febcfb","name":"张三","tel":"13166668888","position":"eaa6caf8ffffffc976ce7286606af088","terminalNO":"","terminalTypeID":"","simNO":"","longitude":"","latitude":"","stateType":""}]
     */

    private boolean success;
    private String msgTitle;
    private List<GetSignJsonMsgBean> msg;

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

    public List<GetSignJsonMsgBean> getMsg() {
        return msg;
    }

    public void setMsg(List<GetSignJsonMsgBean> msg) {
        this.msg = msg;
    }

}
