package com.yuefeng.home.modle;

import java.io.Serializable;

/*公告详情*/
public class AnnouncementDetailBean implements Serializable {

    /**
     * isread : 1
     * issuedate : 2018-11-28 12:02:44.0
     * subject : 11
     * organname : 77601dcaffffffc9051247ee0d4307c0
     * content : 11
     * id : 587bc8730a00000b20b8fe9c79e4cc83
     */
    private String isread;
    private String issuedate;
    private String subject;
    private String organname;
    private String content;
    private String id;

    public String getIsread() {
        return isread;
    }

    public void setIsread(String isread) {
        this.isread = isread;
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

    public String getOrganname() {
        return organname;
    }

    public void setOrganname(String organname) {
        this.organname = organname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
