package com.yuefeng.contacts.modle.groupchat;

import java.io.Serializable;

/*创建群主*/
public class GroupCreateBean implements Serializable {


    /**
     * success : true
     * code : 200
     * text : 说明：返回创建的群组id
     * data : c09a99c40a00004212dfbaa815b50a95
     */

    private boolean success;
    private int code;
    private String text;
    private String data;

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
