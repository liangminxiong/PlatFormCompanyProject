package com.yuefeng.home.modle;

import java.io.Serializable;

public class NewRemindDetailDataBean implements Serializable {
    /**
     * isread : 0.0
     * notread :
     * subject : 2018-12-23 00:21:17.0
     * latitude : 33.986452
     * organname : 601:13301444667
     * genre : 4
     * id : d6b8c6cbffffffc90a40c625aad45ef2
     * issuedate : 皖F37302(1号吸污车)
     * content : 紧急报警
     * longitude : 116.865672
     */

    private String isread;
    private String notread;
    private String subject;
    private String latitude;
    private String organname;
    private String genre;
    private String id;
    private String issuedate;
    private String content;
    private String longitude;

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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getOrganname() {
        return organname;
    }

    public void setOrganname(String organname) {
        this.organname = organname;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIssuedate() {
        return issuedate;
    }

    public void setIssuedate(String issuedate) {
        this.issuedate = issuedate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
