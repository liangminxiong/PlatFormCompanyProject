package com.yuefeng.home.ui.modle;

import java.io.Serializable;

/*公告*/
public class AnnouncementDataMsgBean implements Serializable {
    /**
     * id : 52e3157a0a00000b303e7d6db406f386
     * organname : 淮北项目
     * issuedate : 2018-11-27 09:57:45.0
     * subject : 测试002
     * content : 测试002
     * genre : 1 1-公告 、2-信息 ，3 -更新
     */

    private String id;
    private String organname;
    private String issuedate;
    private String subject;
    private String content;
    private String genre;

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

}
