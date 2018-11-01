package com.yuefeng.features.modle.video;

import java.io.Serializable;
import java.util.List;

public class ChangeVideoEquipmentDataBean implements Serializable {

    private String id;
    private String pid;
    private String orgShortName;
    private String orgCode;
    private String orgName;
    private String principal;
    private String principalTel;
    private String fax;
    private String address;
    private List<OrgansBean> organ;
    private List<VideoesXBean> vehicle;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrgName1() {
        return orgName;
    }

    public void setOrgName1(String orgName) {
        this.orgName = orgName;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }



    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public List<VideoesXBean> getVehicle() {
        return vehicle;
    }

    public void setVehicle(List<VideoesXBean> vehicle) {
        this.vehicle = vehicle;
    }

    public List<OrgansBean> getOrgan() {
        return organ;
    }

    public void setOrgan(List<OrgansBean> organ) {
        this.organ = organ;
    }
}
