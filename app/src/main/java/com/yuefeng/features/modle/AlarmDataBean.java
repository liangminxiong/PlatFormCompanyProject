package com.yuefeng.features.modle;


import java.io.Serializable;

public class AlarmDataBean implements Serializable {
    /**
     * longitude : 116.866616
     * id : df240f8cffffffc90a40c62571834da8
     * speed : 0
     * terminalNO : 601:10198829423
     * dealType :
     * op : 围栏分析，离开区域：淮北市相山区电子围栏
     * latitude : 33.986788
     * gpsTime : 2018-12-24 15:35:18.0
     * registrationNO : 皖F37849（1号除雪车）
     */

    private String longitude;
    private String id;
    private String isread = "1";
    private String speed;
    private String terminalNO;
    private String dealType;
    private String op;
    private String latitude;
    private String gpsTime;
    private String registrationNO;

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getTerminalNO() {
        return terminalNO;
    }

    public void setTerminalNO(String terminalNO) {
        this.terminalNO = terminalNO;
    }

    public String getDealType() {
        return dealType;
    }

    public void setDealType(String dealType) {
        this.dealType = dealType;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getGpsTime() {
        return gpsTime;
    }

    public void setGpsTime(String gpsTime) {
        this.gpsTime = gpsTime;
    }

    public String getRegistrationNO() {
        return registrationNO;
    }

    public void setRegistrationNO(String registrationNO) {
        this.registrationNO = registrationNO;
    }

    public String getIsread() {
        return isread;
    }

    public void setIsread(String isread) {
        this.isread = isread;
    }
}
