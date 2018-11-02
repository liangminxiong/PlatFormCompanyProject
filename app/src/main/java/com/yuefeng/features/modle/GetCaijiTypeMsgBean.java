package com.yuefeng.features.modle;

import java.io.Serializable;

public class GetCaijiTypeMsgBean implements Serializable {
    /**
     * id : d314c837ffffffc91739d203e8f43948
     * titleid : mao_caiji
     * code : 1
     * data : 线路
     * orderNum : 1
     */

    private String id;
    private String titleid;
    private String code;
    private String data;
    private String orderNum;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitleid() {
        return titleid;
    }

    public void setTitleid(String titleid) {
        this.titleid = titleid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }
}
