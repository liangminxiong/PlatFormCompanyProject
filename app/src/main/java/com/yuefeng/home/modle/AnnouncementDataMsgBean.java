package com.yuefeng.home.modle;

import java.io.Serializable;

/*公告*/
public class AnnouncementDataMsgBean implements Serializable {
    /**
     * isread : 0
     * issuedate : 2018-11-28 14:55:04.0
     * subject : 222222
     * content : 333333333
     * orgName : 洗扫车组
     * id : 5919909a0a00000b407cd4441dbbcc27
     */

    private String isread;
    private String issuedate;
    private String subject;
    private String content;
    private String orgName;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
