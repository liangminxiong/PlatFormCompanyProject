package com.yuefeng.contacts.modle;

import java.io.Serializable;

public class CheckOnlineBean implements Serializable {


    /**
     * success : true
     * code : 200
     * text : 说明：在线状态，1为在线，0为不在线。
     * data : 0
     */

    private boolean success;
    private int code;
    private String text;
    private int data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }
}
