package com.yuefeng.home.modle;

import java.io.Serializable;
import java.util.List;


/*App历史版本*/
public class HistoryAppVersionBean implements Serializable {


    /**
     * code : 0
     * msg :
     * count : 2
     */

    private int code;
    private String msg;
    private int count;
    private List<HistoryAppListVersionBean> data;

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

    public List<HistoryAppListVersionBean> getData() {
        return data;
    }

    public void setData(List<HistoryAppListVersionBean> data) {
        this.data = data;
    }

}
