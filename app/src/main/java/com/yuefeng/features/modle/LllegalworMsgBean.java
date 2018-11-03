package com.yuefeng.features.modle;

import java.io.Serializable;

public class LllegalworMsgBean implements Serializable {

    /**
     * id : 3765
     * name : 考勤员刘芝皊
     * time : 2018-10-30 10:00:17.0
     * contents : 超时停止
     * address : 安徽省-淮北市-相山区-桂苑路
     * lat : 33.944188
     * lon : 116.803172
     * team :
     */

    private String id;
    private String name;
    private String time;
    private String contents;
    private String address;
    private String lat;
    private String lon;
    private String team;
    private String renshu;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getRenshu() {
        return renshu;
    }

    public void setRenshu(String renshu) {
        this.renshu = renshu;
    }
}
