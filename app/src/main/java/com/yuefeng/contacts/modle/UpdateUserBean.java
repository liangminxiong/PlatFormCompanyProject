package com.yuefeng.contacts.modle;

import java.io.Serializable;

/*修改用户消息*/
public class UpdateUserBean implements Serializable {


    /**
     * success : true
     * code : 200
     */

    private boolean success;
    private int code;

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
}
