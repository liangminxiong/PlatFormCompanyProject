package com.yuefeng.login_splash.model;

import java.io.Serializable;

public class LoginDataBean implements Serializable {

    /**
     * id : ceshiggcceessssssssss
     * username : ceshi3
     * loginid : ceshi3
     * password : 123456
     * isadmin : 1
     * 0：代表不需要，1代表需要上传轨迹
     * status : 1
     * telNum : 1
     * email : 1
     * orgId : 47
     * createTime : May 16, 2017 4:47:31 PM
     * roleid : yuncloundcustomer
     * roleName : 云平台-运营商
     * descriptions : 云平台-运营商
     * roletype : 57800b5effa80103009901cee4543d4d
     * personelid :
     * isreg : false
     * ip : 120.78.217.251
     */

    private String id;
    private String username;
    private String loginid;
    private String password;
    private int isadmin;
    private int status;
    private String telNum;
    private String email;
    private String orgId;
    private String createTime;
    private String roleid;
    private String roleName;
    private String descriptions;
    private String roletype;
    private String personelid;
    private boolean isreg;
    private String ip;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLoginid() {
        return loginid;
    }

    public void setLoginid(String loginid) {
        this.loginid = loginid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIsadmin() {
        return isadmin;
    }

    public void setIsadmin(int isadmin) {
        this.isadmin = isadmin;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTelNum() {
        return telNum;
    }

    public void setTelNum(String telNum) {
        this.telNum = telNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public String getRoletype() {
        return roletype;
    }

    public void setRoletype(String roletype) {
        this.roletype = roletype;
    }

    public String getPersonelid() {
        return personelid;
    }

    public void setPersonelid(String personelid) {
        this.personelid = personelid;
    }

    public boolean isIsreg() {
        return isreg;
    }

    public void setIsreg(boolean isreg) {
        this.isreg = isreg;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
