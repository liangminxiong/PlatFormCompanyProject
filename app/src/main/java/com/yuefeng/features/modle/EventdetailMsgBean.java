package com.yuefeng.features.modle;

import java.io.Serializable;

public class EventdetailMsgBean implements Serializable {

    /**
     * id : 1
     * problenid : 8
     * userid : 侨银环保公司
     * time : 2018-09-05 13:57:15.0
     * things : 上报问题
     * imgurl :
     * detail :
     * pinjia :
     */

    private String id;
    private String problenid;
    private String userid;
    private String time;
    private String things;
    private String imgurl;
    private String detail;
    private String pinjia;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProblenid() {
        return problenid;
    }

    public void setProblenid(String problenid) {
        this.problenid = problenid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getThings() {
        return things;
    }

    public void setThings(String things) {
        this.things = things;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getPinjia() {
        return pinjia;
    }

    public void setPinjia(String pinjia) {
        this.pinjia = pinjia;
    }
}
