package com.yuefeng.personaltree.model;


import java.io.Serializable;
import java.util.List;

public class PersonalParentBean implements Serializable {

    private String id;
    private String pid;
    private String orgName;
    private List<OrgansBean> organ;
    private List<PersonalXBean> personlist;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public List<OrgansBean> getOrgans() {
        return organ;
    }

    public void setOrgans(List<OrgansBean> organ) {
        this.organ = organ;
    }

    public List<PersonalXBean> getPersonal() {
        return personlist;
    }

    public void setPersonal(List<PersonalXBean> personal) {
        this.personlist = personal;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
