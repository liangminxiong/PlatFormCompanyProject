package com.yuefeng.features.presenter.zhuguansign;

import java.io.Serializable;

public class ExecutiveAtteanTrackMsgBean implements Serializable {
    /**
     * id : eab2ffacffffffc976ce7286d4054823
     * time : 2018-12-25 16:27:54.0
     * lng : 113.402208
     * lat : 23.155046
     * name :
     * tel :
     * address :
     */

    private String id;
    private String time;
    private String lng;
    private String lat;
    private String name;
    private String tel;
    private String address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
