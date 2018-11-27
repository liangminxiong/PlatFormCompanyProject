package com.yuefeng.home.ui.modle;

import java.io.Serializable;

public class ReplyContentBean implements Serializable {


    /**
     * success : true
     * msg : 保存成功
     * result : {"imageurls":""}
     */

    private boolean success;
    private String msg;
    private ResultBean result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * imageurls :
         */

        private String imageurls;

        public String getImageurls() {
            return imageurls;
        }

        public void setImageurls(String imageurls) {
            this.imageurls = imageurls;
        }
    }
}
