package com.yuefeng.login_splash.model;

/**
 * Created by Administrator on 2018/5/10.
 */

public class LoginBean {

    /**
     * success : true
     * msgTitle : 成功提示
     * msg : {"id":"ceshiggcceessssssssss","username":"ceshi3","loginid":"ceshi3","password":"123456",
     * "isadmin":1,"status":1,"telNum":"1","email":"1","orgId":"47","createTime":"May 16, 2017 4:47:31 PM",
     * "roleid":"yuncloundcustomer","roleName":"云平台-运营商","descriptions":"云平台-运营商",
     * "roletype":"57800b5effa80103009901cee4543d4d","personelid":"","isreg":false,"ip":"120.78.217.251"}
     */

    private boolean success;
    private String msgTitle;
    private LoginDataBean msg;

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

    public LoginDataBean getMsg() {
        return msg;
    }

    public void setMsg(LoginDataBean msg) {
        this.msg = msg;
    }

}
