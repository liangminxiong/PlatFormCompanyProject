package com.common.Model;

import java.io.Serializable;

public class FileBean implements Serializable {


    /**
     * status : 10000
     * message : http://www.zgjiuan.cn/file/1536039982413-海南奔牛code.docx
     * result : null
     */

    private int status;
    private String message;
    private Object result;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
