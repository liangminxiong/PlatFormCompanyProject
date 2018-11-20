package com.yuefeng.features.modle.carlist;

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
        organ = new ArrayList<Organ>();
        organsbeen = new ArrayList<OrgansBean>();
        vehicle = new ArrayList<VehiclesBean>();
    }

    private String id;
    private String pid;
    private String orgCode;
    private String orgName;
    private String orgShortName;
    private String principal;
    private String principalTel;
    private String fax;
    private String address;
    private List<Organ> organ;
    private List<OrgansBean> organsbeen;
    private List<VehiclesBean> vehicle;

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

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgShortName() {
        return orgShortName;
    }

    public void setOrgShortName(String orgShortName) {
        this.orgShortName = orgShortName;
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

    public List<VehiclesBean> getVehicle() {
        return vehicle;
    }

    public void setVehicle(List<VehiclesBean> vehicle) {
        this.vehicle = vehicle;
    }

    public List<OrgansBean> getOrgansBeen() {
        return organsbeen;
    }

    public void setOrgansbeen(List<OrgansBean> organs) {
        this.organsbeen = organs;
    }

    public List<Organ> getOrgan() {
        return organ;
    }

    public void setOrgan(List<Organ> organ) {
        this.organ = organ;

    }
}
