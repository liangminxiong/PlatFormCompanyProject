package com.yuefeng.features.modle;

import java.io.Serializable;

/*车辆详情*/
public class CarDetailInfosBean implements Serializable {


    /**
     * success : true
     * msgTitle : 成功提示
     * msg : {"terminalNO":"14532623282","terminalTypeID":"部标","vehicletype":"环卫","carType":"","brandType":"","modeltype":"","oilMax":"","oilSum":"","weight":"","seatcount":""}
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
         * terminalNO : 14532623282
         * terminalTypeID : 部标
         * vehicletype : 环卫
         * carType :
         * brandType :
         * modeltype :
         * oilMax :
         * oilSum :
         * weight :
         * seatcount :
         */

        private String terminalNO;
        private String terminalTypeID;
        private String vehicletype;
        private String carType;
        private String brandType;
        private String modeltype;
        private String oilMax;
        private String oilSum;
        private String weight;
        private String seatcount;

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

        public String getVehicletype() {
            return vehicletype;
        }

        public void setVehicletype(String vehicletype) {
            this.vehicletype = vehicletype;
        }

        public String getCarType() {
            return carType;
        }

        public void setCarType(String carType) {
            this.carType = carType;
        }

        public String getBrandType() {
            return brandType;
        }

        public void setBrandType(String brandType) {
            this.brandType = brandType;
        }

        public String getModeltype() {
            return modeltype;
        }

        public void setModeltype(String modeltype) {
            this.modeltype = modeltype;
        }

        public String getOilMax() {
            return oilMax;
        }

        public void setOilMax(String oilMax) {
            this.oilMax = oilMax;
        }

        public String getOilSum() {
            return oilSum;
        }

        public void setOilSum(String oilSum) {
            this.oilSum = oilSum;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getSeatcount() {
            return seatcount;
        }

        public void setSeatcount(String seatcount) {
            this.seatcount = seatcount;
        }
    }
}
