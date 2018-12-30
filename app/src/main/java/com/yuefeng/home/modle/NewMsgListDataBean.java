package com.yuefeng.home.modle;

import java.io.Serializable;

public class NewMsgListDataBean implements Serializable {

    /**
     * id : 5919909a0a00000b407cd4441dbbcc27
     * organname : 洗扫车组
     * issuedate : 2018-11-28 14:55:04.0
     * subject : 222222
     * content : 333333333
     * genre : 1
     * isread : 1
     * notread : 0
     */

    private String id;
    private String organname;
    private String issuedate;
    private String subject;
    private String content;
    private String genre;
    private String isread;
    private String notread;
    private String latitude;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    private String longitude;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrganname() {
        return organname;
    }

    public void setOrganname(String organname) {
        this.organname = organname;
    }

    public String getIssuedate() {
        return issuedate;
    }

    public void setIssuedate(String issuedate) {
        this.issuedate = issuedate;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getIsread() {
        return isread;
    }

    public void setIsread(String isread) {
        this.isread = isread;
    }

    public String getNotread() {
        return notread;
    }

    public void setNotread(String notread) {
        this.notread = notread;
    }
}
