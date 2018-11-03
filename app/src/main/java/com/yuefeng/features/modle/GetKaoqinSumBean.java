package com.yuefeng.features.modle;

import java.io.Serializable;

/*考勤总数*/
public class GetKaoqinSumBean implements Serializable {


    /**
     * success : true
     * msgTitle : 成功提示
     * msg : {"kaoqinsum":34,"late":0,"early":0,"daiqian":2,"qiaodao":32,"kuanggong":0,"signback":0}
     */

    private boolean success;
    private String msgTitle;
    private MsgBean msg;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsgTitle() {
        return msgTitle;
    }

    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle;
    }

    public MsgBean getMsg() {
        return msg;
    }

    public void setMsg(MsgBean msg) {
        this.msg = msg;
    }

    public static class MsgBean {
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
}
