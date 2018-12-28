package com.yuefeng.features.modle;

import java.io.Serializable;

public class GetWorkTimeMsgBean implements Serializable {
    /**
     * detailtime :
     * isupdate : 1
     */

    private String detailtime;
    private String isupdate;

    public String getDetailtime() {
        return detailtime;
    }

    public void setDetailtime(String detailtime) {
        this.detailtime = detailtime;
    }

    public String getIsupdate() {
        return isupdate;
    }

    public void setIsupdate(String isupdate) {
        this.isupdate = isupdate;
    }
}
