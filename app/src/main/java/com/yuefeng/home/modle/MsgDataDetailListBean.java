package com.yuefeng.home.modle;

import java.io.Serializable;

public class MsgDataDetailListBean implements Serializable {
    /**
     * state : 1
     * id : 358843c20a00001f5b51f5fcadf29a70
     * reviewcontent : 不通过
     * reviewdate : 2018-11-21 17:09:40
     * "isread": "1",
     * pid : dg1168
     * reviewpersonel : 测试
     * type : CONTENT
     * reviewtitle : 【维修】皖F35603(11号勾臂车)(驳回)
     * imageurls :
     * reviewid : 2bbba31c0a00001f69b7dadadae8d9fe
     */

    private String state;
    private String id;
    private String reviewcontent;
    private String reviewdate;
    private String isread;
    private String pid;
    private String reviewpersonel;
    private String type;
    private String reviewtitle;
    private String imageurls;
    private String reviewid;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReviewcontent() {
        return reviewcontent;
    }

    public void setReviewcontent(String reviewcontent) {
        this.reviewcontent = reviewcontent;
    }

    public String getReviewdate() {
        return reviewdate;
    }

    public void setReviewdate(String reviewdate) {
        this.reviewdate = reviewdate;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getReviewpersonel() {
        return reviewpersonel;
    }

    public void setReviewpersonel(String reviewpersonel) {
        this.reviewpersonel = reviewpersonel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReviewtitle() {
        return reviewtitle;
    }

    public void setReviewtitle(String reviewtitle) {
        this.reviewtitle = reviewtitle;
    }

    public String getImageurls() {
        return imageurls;
    }

    public void setImageurls(String imageurls) {
        this.imageurls = imageurls;
    }

    public String getReviewid() {
        return reviewid;
    }

    public void setReviewid(String reviewid) {
        this.reviewid = reviewid;
    }

    public String getIsread() {
        return isread;
    }

    public void setIsread(String isread) {
        this.isread = isread;
    }
}
