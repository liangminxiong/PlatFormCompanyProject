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
        organ = new ArrayList<>();
        organs = new ArrayList<>();
        vehicle = new ArrayList<>();
    }



    private String id;
    private String pid;
    private String orgShortName;
    private String orgCode;
    private String orgName;
    private String principal;
    private String principalTel;
    private String fax;
    private String address;
    private List<Organ> organ;
    private List<OrgansBean> organs;
    private List<VideoesBean> vehicle;

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
        return orgShortName;
    }

    public void setOrgName(String orgName) {
        this.orgShortName = orgName;
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
        return organ;
    }

    public void setOrgans(List<Organ> organs) {
        this.organ = organs;

    }

    public List<OrgansBean> getOrgansbeen() {
        return organs;
    }

    public void setOrgansbeen(List<OrgansBean> organsbeen) {
        this.organs = organsbeen;
    }

    public List<VideoesBean> getVideoes() {
        return vehicle;
    }

    public void setVideoes(List<VideoesBean> videoes) {
        this.vehicle = videoes;
    }
}
