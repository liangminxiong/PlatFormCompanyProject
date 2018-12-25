package com.yuefeng.features.modle;

import java.io.Serializable;

public class PersonalinfoListBean implements Serializable {
    /**
     * id : eab2ffacffffffc976ce7286d4054823
     * pid : 环卫测试
     * name : 张三
     * tel : 13166668888
     * position : 主管
     * terminalNO : 101:145
     * terminalTypeID : 101
     * simNO :
     * longitude :
     * latitude :
     * stateType :
     */

    private String id;
    private String pid;
    private String name;
    private String tel;
    private String position;
    private String terminalNO;
    private String terminalTypeID;
    private String simNO;
    private String longitude;
    private String latitude;
    private String stateType;
    private String address;
    private String time;
    private String speed;


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getTerminalNO() {
        return terminalNO;
    }

    public void setTerminalNO(String terminalNO) {
        this.terminalNO = terminalNO;
    }

    public String getTerminalTypeID() {
        return terminalTypeID;
    }

    public void setTerminalTypeID(String terminalTypeID) {
        this.terminalTypeID = terminalTypeID;
    }

    public String getSimNO() {
        return simNO;
    }

    public void setSimNO(String simNO) {
        this.simNO = simNO;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getStateType() {
        return stateType;
    }

    public void setStateType(String stateType) {
        this.stateType = stateType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
