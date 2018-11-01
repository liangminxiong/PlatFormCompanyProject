package com.yuefeng.features.modle.video;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/7/20.
 */

public class OrgansBean implements Serializable {
    /**
     * id : jst1482
     * pid : jst774
     * orgName : 张勋
     * principal :
     * principalTel :
     * fax :
     * address :
     * organs : []
     */


    private String id;
    private String pid;
    private String orgShortName;
    private String orgName;
    private String orgCode;
    private String principal;
    private String principalTel;
    private String fax;
    private String address;
    private List<Organ> organ;
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

    public List<VideoesBean> getVideoes() {
        return vehicle;
    }

    public void setVideoes(List<VideoesBean> videoes) {
        this.vehicle = videoes;
    }
}
