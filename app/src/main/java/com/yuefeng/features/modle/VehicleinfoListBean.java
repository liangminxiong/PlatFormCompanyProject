package com.yuefeng.features.modle;

import java.io.Serializable;

public class VehicleinfoListBean implements Serializable {
    /**
     * id : eab8a86affffffc976ce72860f4a52ae
     * registrationNO : 工作车辆1
     * terminalNO : 601:123
     * simNO : 123
     * pid : 环卫测试 班组
     * terminalTypeID : 601
     * stateType :
     * carType : 7648dc72ffa801030020b8ad9a32aa5d
     * longitude :
     * latitude :
     */

    private String id;
    private String registrationNO;
    private String terminalNO;
    private String simNO;
    private String pid;
    private String terminalTypeID;
    private String stateType;
    private String carType;
    private String longitude;
    private String latitude;
    private String address;
    private String time;
    private String speed;
    private String videoterminalNO;


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

    public String getRegistrationNO() {
        return registrationNO;
    }

    public void setRegistrationNO(String registrationNO) {
        this.registrationNO = registrationNO;
    }

    public String getTerminalNO() {
        return terminalNO;
    }

    public void setTerminalNO(String terminalNO) {
        this.terminalNO = terminalNO;
    }

    public String getSimNO() {
        return simNO;
    }

    public void setSimNO(String simNO) {
        this.simNO = simNO;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTerminalTypeID() {
        return terminalTypeID;
    }

    public void setTerminalTypeID(String terminalTypeID) {
        this.terminalTypeID = terminalTypeID;
    }

    public String getStateType() {
        return stateType;
    }

    public void setStateType(String stateType) {
        this.stateType = stateType;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getVideoterminalNO() {
        return videoterminalNO;
    }

    public void setVideoterminalNO(String videoterminalNO) {
        this.videoterminalNO = videoterminalNO;
    }
}
