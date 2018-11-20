package com.yuefeng.features.modle;

import java.io.Serializable;

public class GetMonitoringHistoryDetaiBean implements Serializable {
    /**
     * id : 119b04aeffffee010138595a448940bc
     * personid :
     * starttime : 2018-11-14 17:42:00.0
     * endtime : 2018-11-14 17:43:00.0
     * timesum : 60
     * point : 113.41396245951597,23.158950006105805;113.41396245951597,23.158950006105805;113.41396245951597,23.158950006105805;113.41396245951597,23.158950006105805;113.41396245951597,23.158950006105805;113.41396245951597,23.158950006105805;113.41396245951597,23.158950006105805;113.41396245951597,23.158950006105805
     */

    private String id;
    private String personid;
    private String starttime;
    private String endtime;
    private String timesum;
    private String point;
    private String startaddress;
    private String endaddress;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPersonid() {
        return personid;
    }

    public void setPersonid(String personid) {
        this.personid = personid;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getTimesum() {
        return timesum;
    }

    public void setTimesum(String timesum) {
        this.timesum = timesum;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getStartaddress() {
        return startaddress;
    }

    public void setStartaddress(String startaddress) {
        this.startaddress = startaddress;
    }

    public String getEndaddress() {
        return endaddress;
    }

    public void setEndaddress(String endaddress) {
        this.endaddress = endaddress;
    }
}
