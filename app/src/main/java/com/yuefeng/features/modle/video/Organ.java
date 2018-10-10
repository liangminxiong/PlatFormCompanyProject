package com.yuefeng.features.modle.video;

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
        organs = new ArrayList<>();
        organsbeen = new ArrayList<>();
        videoes = new ArrayList<>();
    }

    private String principal;
    private String principalTel;
    private String fax;
    private String address;
    private String id;
    private String pid;
    private String orgName;
    private List<Organ> organs;
    private List<OrgansBean> organsbeen;
    private List<VideoesBean> videoes;

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

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getPrincipalTel() {
        return principalTel;
    }

    public void setPrincipalTel(String principalTel) {
        this.principalTel = principalTel;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public List<Organ> getOrgans() {
        return organs;
    }

    public void setOrgans(List<Organ> organs) {
        this.organs = organs;

    }

    public List<OrgansBean> getOrgansbeen() {
        return organsbeen;
    }

    public void setOrgansbeen(List<OrgansBean> organsbeen) {
        this.organsbeen = organsbeen;
    }

    public List<VideoesBean> getVideoes() {
        return videoes;
    }

    public void setVideoes(List<VideoesBean> videoes) {
        this.videoes = videoes;
    }
}
