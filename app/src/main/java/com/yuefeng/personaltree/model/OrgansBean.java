package com.yuefeng.personaltree.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/7/20.
 */

public class OrgansBean implements Serializable {

    private String id;
    private String pid;
    private String orgName;
    private List<Organ> organ;
    private List<PersonalBean> personlist;

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

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }


    public List<Organ> getOrgans() {
        return organ;
    }

    public void setOrgans(List<Organ> organ) {
        this.organ = organ;
    }

    public List<PersonalBean> getPersonlist() {
        return personlist;
    }

    public void setPersonlist(List<PersonalBean> personlist) {
        this.personlist = personlist;
    }
}
