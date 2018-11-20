package com.yuefeng.features.modle;

import java.io.Serializable;
import java.util.List;

/*历史监察*/
public class GetMonitoringHistoryBean implements Serializable {


    /**
     * success : true
     * msgTitle : 成功提示
     * msg : {"qiandao":2,"report":"0","timesum":60,"detai":[{"id":"119b04aeffffee010138595a448940bc","personid":"","starttime":"2018-11-14 17:42:00.0","endtime":"2018-11-14 17:43:00.0","timesum":"60","point":"113.41396245951597,23.158950006105805;113.41396245951597,23.158950006105805;113.41396245951597,23.158950006105805;113.41396245951597,23.158950006105805;113.41396245951597,23.158950006105805;113.41396245951597,23.158950006105805;113.41396245951597,23.158950006105805;113.41396245951597,23.158950006105805"}]}
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
         * qiandao : 2
         * report : 0
         * timesum : 60
         * detai : [{"id":"119b04aeffffee010138595a448940bc","personid":"","starttime":"2018-11-14 17:42:00.0","endtime":"2018-11-14 17:43:00.0","timesum":"60","point":"113.41396245951597,23.158950006105805;113.41396245951597,23.158950006105805;113.41396245951597,23.158950006105805;113.41396245951597,23.158950006105805;113.41396245951597,23.158950006105805;113.41396245951597,23.158950006105805;113.41396245951597,23.158950006105805;113.41396245951597,23.158950006105805"}]
         */

        private int qiandao;
        private String report;
        private int timesum;
        private List<GetMonitoringHistoryDetaiBean> detai;

        public int getQiandao() {
            return qiandao;
        }

        public void setQiandao(int qiandao) {
            this.qiandao = qiandao;
        }

        public String getReport() {
            return report;
        }

        public void setReport(String report) {
            this.report = report;
        }

        public int getTimesum() {
            return timesum;
        }

        public void setTimesum(int timesum) {
            this.timesum = timesum;
        }

        public List<GetMonitoringHistoryDetaiBean> getDetai() {
            return detai;
        }

        public void setDetai(List<GetMonitoringHistoryDetaiBean> detai) {
            this.detai = detai;
        }
    }
}
