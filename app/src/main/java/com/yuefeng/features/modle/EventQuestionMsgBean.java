package com.yuefeng.features.modle;

import java.io.Serializable;

public class EventQuestionMsgBean implements Serializable {

    /*质量巡查*/
    /**
     * id : 8
     * pid : dg1168
     * address : 地址
     * problem : 问题
     * uploadpeople : 2a5a6f40ffa80103701e7165535ad91d
     * uploadtime : 2018-09-05 13:57:15.0
     * imgurl : http://10.0.0.68:8080/webfiles/zgbd_rubbish/wxupload/image/2018090513571542540.png,
     * state : 1
     * longitude : 132.2222
     * latitude : 12.222
     * type : 0
     * uploadpeoplename : 侨银环保公司
     */

    private String id;
    private String pid;
    private String address;
    private String problem;
    private String uploadpeople;
    private String uploadtime;
    private String imgurl;
    private String state;
    private String longitude;
    private String latitude;
    private String type;
    private String uploadpeoplename;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getUploadpeople() {
        return uploadpeople;
    }

    public void setUploadpeople(String uploadpeople) {
        this.uploadpeople = uploadpeople;
    }

    public String getUploadtime() {
        return uploadtime;
    }

    public void setUploadtime(String uploadtime) {
        this.uploadtime = uploadtime;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUploadpeoplename() {
        return uploadpeoplename;
    }

    public void setUploadpeoplename(String uploadpeoplename) {
        this.uploadpeoplename = uploadpeoplename;
    }
}
