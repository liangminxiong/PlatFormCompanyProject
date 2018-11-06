package com.yuefeng.features.modle;

import java.io.Serializable;

/*考勤信息详情*/
public class GetKaoqinSumMsgBean implements Serializable {
    /**
     * kaoqinsum : 34
     * late : 0
     * early : 0
     * daiqian : 2
     * qiaodao : 32
     * kuanggong : 0
     * signback : 0
     */

    private int kaoqinsum;
    private int late;
    private int early;
    private int daiqian;
    private int qiaodao;
    private int kuanggong;
    private int signback;

    public int getKaoqinsum() {
        return kaoqinsum;
    }

    public void setKaoqinsum(int kaoqinsum) {
        this.kaoqinsum = kaoqinsum;
    }

    public int getLate() {
        return late;
    }

    public void setLate(int late) {
        this.late = late;
    }

    public int getEarly() {
        return early;
    }

    public void setEarly(int early) {
        this.early = early;
    }

    public int getDaiqian() {
        return daiqian;
    }

    public void setDaiqian(int daiqian) {
        this.daiqian = daiqian;
    }

    public int getQiaodao() {
        return qiaodao;
    }

    public void setQiaodao(int qiaodao) {
        this.qiaodao = qiaodao;
    }

    public int getKuanggong() {
        return kuanggong;
    }

    public void setKuanggong(int kuanggong) {
        this.kuanggong = kuanggong;
    }

    public int getSignback() {
        return signback;
    }

    public void setSignback(int signback) {
        this.signback = signback;
    }
}
