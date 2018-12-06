package com.yuefeng.home.modle;

import java.io.Serializable;

public class MsgListDataBean implements Serializable {

    /**
     * state : 0
     * id : 39757dbd0a00001f561956441c00ac97
     * reviewcontent : 不合格
     * reviewdate : 2018-11-22 11:27:40
     * "isread": "1",
     * pid : dg1168
     * reviewpersonel : 陈伟
     * type : CONTENT
     * reviewtitle : 【车审】粤ADV861(驳回)
     * imageurls :
     * reviewid : 11af05590a00001f20a1326282df5581
     * org : 侨银环保科技股份有限公司
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
    private String org;
    private int imageId;


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

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getIsread() {
        return isread;
    }

    public void setIsread(String isread) {
        this.isread = isread;
    }
}
