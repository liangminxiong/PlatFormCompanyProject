package com.yuefeng.personaltree.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 逻辑实体类，不与数据库联系
 *
 * @author li
 */
public class Organ implements Serializable {

    public Organ() {
        organ = new ArrayList<>();
        organsbeen = new ArrayList<>();
        personlist = new ArrayList<>();
    }

    private String id;
    private String pid;
    private String orgName;
    private List<Organ> organ;
    private List<OrgansBean> organsbeen;
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

    public void setOrgans(List<Organ> organs) {
        this.organ = organs;

    }

    public List<OrgansBean> getOrgansbeen() {
        return organsbeen;
    }

    public void setOrgansbeen(List<OrgansBean> organsbeen) {
        this.organsbeen = organsbeen;
    }

    public List<PersonalBean> getVideoes() {
        return personlist;
    }

    public void setVideoes(List<PersonalBean> videoes) {
        this.personlist = videoes;
    }
}
