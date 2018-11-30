package com.yuefeng.home.modle;

import java.io.Serializable;

public class HistoryAppListVersionBean implements Serializable {

    /**
     * isread : 1
     * vercode : 1
     * vercontent : null
     * verdate : 2018-11-30 10:35:13.0
     * vername : V1.0.1
     * id : 1001
     */

    private String isread;
    private String vercode;
    private String vercontent;
    private String verdate;
    private String vername;
    private String id;

    public String getIsread() {
        return isread;
    }

    public void setIsread(String isread) {
        this.isread = isread;
    }

    public String getVercode() {
        return vercode;
    }

    public void setVercode(String vercode) {
        this.vercode = vercode;
    }

    public String getVercontent() {
        return vercontent;
    }

    public void setVercontent(String vercontent) {
        this.vercontent = vercontent;
    }

    public String getVerdate() {
        return verdate;
    }

    public void setVerdate(String verdate) {
        this.verdate = verdate;
    }

    public String getVername() {
        return vername;
    }

    public void setVername(String vername) {
        this.vername = vername;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
