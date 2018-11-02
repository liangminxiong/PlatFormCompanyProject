package com.yuefeng.features.modle.video;

import com.yuefeng.features.modle.GetCaijiTypeMsgBean;

import java.io.Serializable;
import java.util.List;

/*采集类型*/
public class GetCaijiTypeBean implements Serializable {


    /**
     * success : true
     * msgTitle : 成功提示
     * msg : [{"id":"d314c837ffffffc91739d203e8f43948","titleid":"mao_caiji","code":"1","data":"线路","orderNum":"1"},
     * {"id":"d315ba67ffffffc91739d203491c3552","titleid":"mao_caiji","code":"1","data":"网格","orderNum":"2"},
     * {"id":"d319c2c0ffffffc91739d203213c3448","titleid":"mao_caiji","code":"2","data":"生活垃圾收集点","orderNum":"3"},
     * {"id":"d31a0d37ffffffc91739d20355b414b9","titleid":"mao_caiji","code":"2","data":"垃圾点","orderNum":"4"},
     * {"id":"d31a5c8fffffffc91739d20329acb6ff","titleid":"mao_caiji","code":"2","data":"公厕","orderNum":"5"}]
     */

    private boolean success;
    private String msgTitle;
    private List<GetCaijiTypeMsgBean> msg;

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

    public List<GetCaijiTypeMsgBean> getMsg() {
        return msg;
    }

    public void setMsg(List<GetCaijiTypeMsgBean> msg) {
        this.msg = msg;
    }
}
