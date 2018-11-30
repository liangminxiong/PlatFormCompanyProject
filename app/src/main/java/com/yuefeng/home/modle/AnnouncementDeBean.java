package com.yuefeng.home.modle;

import java.io.Serializable;
import java.util.List;

/*公告详情*/
public class AnnouncementDeBean implements Serializable {


    /**
     * success : true
     * msg : 获取成功！
     */

    private boolean success;
    private String msg;
    private List<AnnouncementDetailBean> result;

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

    public List<AnnouncementDetailBean> getResult() {
        return result;
    }

    public void setResult(List<AnnouncementDetailBean> result) {
        this.result = result;
    }

}
