package com.yuefeng.features.modle;

import java.io.Serializable;

/*签到历史*/
public class HistorySngnInListDataBean implements Serializable {
    /**
     * id : 601a8731ffffffc94f694d6027d4ba5b
     * pid : ea9b4033ffffee0101ed1860a1febcfb
     * personid : eab2ffacffffffc976ce7286d4054823
     * time : 2018-11-29 23:33:30.0
     * lon : 113.25901956165985
     * lat : 23.131040760558808
     * address : 广东省广州市越秀区百灵路118
     * imgurl : http://120.78.217.251:80/webfiles/zgbd_rubbish/workqiaodao/image/2018112923333048346.png
     * memo : 好
     */

    private String id;
    private String pid;
    private String personid;
    private String personalname;
    private String time;
    private String lon;
    private String lat;
    private String address;
    private String imgurl;
    private String memo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPersonid() {
        return personid;
    }

    public void setPersonid(String personid) {
        this.personid = personid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getPersonalName() {
        return personalname;
    }

    public void setPersonalName(String personalName) {
        this.personalname = personalName;
    }
}
