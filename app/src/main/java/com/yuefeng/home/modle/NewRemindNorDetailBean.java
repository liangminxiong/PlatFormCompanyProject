package com.yuefeng.home.modle;

import java.io.Serializable;
import java.util.List;

/*报警详情*/
public class NewRemindNorDetailBean implements Serializable {


    /**
     * code : 0
     * msg :
     * count : 1
     * data : [{"isread":"0.0","notread":"","subject":"2018-12-23 00:21:17.0","latitude":"33.986452","organname":"601:13301444667","genre":"4","id":"d6b8c6cbffffffc90a40c625aad45ef2","issuedate":"皖F37302(1号吸污车)","content":"紧急报警","longitude":"116.865672"}]
     */

    private int code;
    private String msg;
    private int count;
    private List<NewRemindDetailDataBean> data;

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

    public List<NewRemindDetailDataBean> getData() {
        return data;
    }

    public void setData(List<NewRemindDetailDataBean> data) {
        this.data = data;
    }
}
