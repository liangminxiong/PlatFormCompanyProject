package com.yuefeng.home.modle;

import java.io.Serializable;
import java.util.List;

/*公告消息列表*/
public class AnnouncementDataBean implements Serializable {


    /**
     * code : 0
     * msg :
     * count : 3
     */

    private int code;
    private String msg;
    private int count;
    private List<AnnouncementDataMsgBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<AnnouncementDataMsgBean> getData() {
        return data;
    }

    public void setData(List<AnnouncementDataMsgBean> data) {
        this.data = data;
    }

}
