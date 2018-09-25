package com.yuefeng.login_splash.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/7/9.
 */

public class PersonelChangeBean implements Serializable {
    /**
     * id : 6a3edb800a0000637c4391346fdef973
     * name : 赵工
     * telNum : yuefeng
     * position :
     * pid : yun
     * createTime : 2018-06-26 13:11:15.0
     * orderNumber : 0
     * password : 123456
     * isadmin : 0
     * imagthpath : http://www.vocsystem.cn/webfiles/zgbd_fireControl/shouye/lunbo1.png,http://www.vocsystem.cn/webfiles/zgbd_fireControl/shouye/lunbo2.png,http://www.vocsystem.cn/webfiles/zgbd_fireControl/shouye/lunbo3.jpg
     */

    private String id;
    private String name;
    private String telNum;
    private String position;
    private String pid;
    private String createTime;
    private String orderNumber;
    private String password;
    private String isadmin;
    private String imagthpath;
    private String orgName;
    private String orgShortName;
    private List<RoleBean> role;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelNum() {
        return telNum;
    }

    public void setTelNum(String telNum) {
        this.telNum = telNum;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIsadmin() {
        return isadmin;
    }

    public void setIsadmin(String isadmin) {
        this.isadmin = isadmin;
    }

    public String getImagthpath() {
        return imagthpath;
    }

    public void setImagthpath(String imagthpath) {
        this.imagthpath = imagthpath;
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

    public List<RoleBean> getRole() {
        return role;
    }

    public void setRole(List<RoleBean> role) {
        this.role = role;
    }

    public static class RoleBean implements Serializable{
       /* *
         * id : 7d198e820a00001f0e6387d34ed63ed2
         * roleName : 大boss
         * description : 神权限。没事别用*/


        private String id;
        private String roleName;
        private String description;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRoleName() {
            return roleName;
        }

        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
