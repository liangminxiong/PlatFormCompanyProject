package com.yuefeng.features.modle;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/28.
 */

public class WheelPathDatasBean implements Serializable {

    /**
     * tn : 601:13425013258
     * gt : 2018-05-22 00:11:16
     * st : 2018-05-22 00:12:18.0
     * la : 22.023731231689453
     * lo : 113.20093536376953
     * ang : 0.0
     * add :
     * sp : 0
     * obd : {"a":0,"b":0,"c":0,"d":0,"e":0,"f":0,"g":"43347","h":0,"i":0,"m":0,"n":0,"gs":"ACC关、高程:26、s=6小时20分、","mc":""}
     * v : 定位
     */

    private String tn;
    private String gt;
    private String st;
    private double la;
    private double lo;
    private double ang;
    private String add;
    private String sp;
    private WheelPathObdBean obd;
    private String v;

    public String getTn() {
        return tn;
    }

    public void setTn(String tn) {
        this.tn = tn;
    }

    public String getGt() {
        return gt;
    }

    public void setGt(String gt) {
        this.gt = gt;
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public double getLa() {
        return la;
    }

    public void setLa(double la) {
        this.la = la;
    }

    public double getLo() {
        return lo;
    }

    public void setLo(double lo) {
        this.lo = lo;
    }

    public double getAng() {
        return ang;
    }

    public void setAng(double ang) {
        this.ang = ang;
    }

    public String getAdd() {
        return add;
    }

    public void setAdd(String add) {
        this.add = add;
    }

    public String getSp() {
        return sp;
    }

    public void setSp(String sp) {
        this.sp = sp;
    }

    public WheelPathObdBean getObd() {
        return obd;
    }

    public void setObd(WheelPathObdBean obd) {
        this.obd = obd;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

}
