package com.yuefeng.features.modle;

import java.io.Serializable;

public class GetMonitoringPlanCountBean implements Serializable {


    /**
     * success : true
     * msgTitle : 成功提示
     * msg : {"plan":"0","count":"1"}
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
         * plan : 0
         * count : 1
         */

        private String plan;
        private String count;

        public String getPlan() {
            return plan;
        }

        public void setPlan(String plan) {
            this.plan = plan;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }
    }
}
