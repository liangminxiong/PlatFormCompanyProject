package com.yuefeng.contacts.modle.contacts;

import java.io.Serializable;

public class GroupDataBean implements Serializable {


    /**
     * success : true
     * code : 200
     * msg : 成功
     * text : 说明：群组信息，userlist为群组用户数组
     * data : {"id":"fdaeb0e9ffa801030bc5ff59d8b6eb04","name":"聊扣扣","createid":"f9276992ffffffc944becffdba449a2d","userlist":[{"id":"efdb1590ffffffc9449c873e605d08c1","name":"杨杨","icon":"http://testresource.hangyunejia.com/resource/uploads/file/20181212/YM1mlVZxMnpBAhM2dBiK.jpeg","grouplist":[]},{"id":"f9276992ffffffc944becffdba449a2d","name":"小梁","icon":"http://testresource.hangyunejia.com/resource/uploads/file/20181212/YM1mlVZxMnpBAhM2dBiK.jpeg","grouplist":[]}]}
     */

    private boolean success;
    private int code;
    private String msg;
    private String text;
    private GroupListDataBean data;

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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public GroupListDataBean getData() {
        return data;
    }

    public void setData(GroupListDataBean data) {
        this.data = data;
    }
}
