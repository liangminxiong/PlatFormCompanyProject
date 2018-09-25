package com.yuefeng.features.modle;

import java.io.Serializable;

public class QuestionListBean implements Serializable {
    /**
     * id : 68
     * pid : ea9b4033ffffee0101ed1860a1febcfb
     * address : 广东省广州市天河区新塘大街28
     * problem : 描述
     * uploadpeople : eab2ffacffffffc976ce7286d4054823
     * uploadtime : 2018-09-18 17:25:50.0
     * imgurl : http://120.78.217.251:80/webfiles/zgbd_rubbish/wxupload/image/2018091817254736562.png,http://120.78.217.251:80/webfiles/zgbd_rubbish/wxupload/image/2018091817254913919.png
     * state : 1
     * longitude : 113.414184
     * latitude : 23.158781
     * type : 1
     * uploadpeoplename : 张三
     * claimid :
     * claimpeople :
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
    private String claimid;
    private String claimpeople;

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

    public String getClaimid() {
        return claimid;
    }

    public void setClaimid(String claimid) {
        this.claimid = claimid;
    }

    public String getClaimpeople() {
        return claimpeople;
    }

    public void setClaimpeople(String claimpeople) {
        this.claimpeople = claimpeople;
    }
}
