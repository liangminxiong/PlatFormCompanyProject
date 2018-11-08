package com.yuefeng.features.modle.carlist;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/7/20.
 */

public class CarListInfosMsgBean implements Serializable {
    /**
     * id : jst774
     * pid : jst666
     * orgCode : 鸿运运输有限公司
     * orgName : 鸿运运输有限公司
     * orgShortName : 鸿运运输有限公司
     * principal :
     * principalTel :
     * fax :
     * address :
     */

    private String id;
    private String pid;
    private String orgShortName;
    private String orgCode;
    private String orgName;
    private String principal;
    private String principalTel;
    private String fax;
    private String address;
    private List<OrgansBean> organs;
    private List<VehiclesBeanX> vehicles;

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

    public List<OrgansBean> getOrgans() {
        return organs;
    }

    public void setOrgans(List<OrgansBean> organs) {
        this.organs = organs;
    }

    public List<VehiclesBeanX> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<VehiclesBeanX> vehicles) {
        this.vehicles = vehicles;
    }
}
