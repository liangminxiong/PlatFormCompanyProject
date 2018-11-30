package com.yuefeng.home.modle;

import java.io.Serializable;
import java.util.List;

/*消息详情*/
public class MsgDataDetailBean implements Serializable {


    /**
     * success : true
     * msg : 获取成功！
     */

    private boolean success;
    private String msg;
    private List<MsgDataDetailListBean> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<MsgDataDetailListBean> getResult() {
        return result;
    }

    public void setResult(List<MsgDataDetailListBean> result) {
        this.result = result;
    }

}
