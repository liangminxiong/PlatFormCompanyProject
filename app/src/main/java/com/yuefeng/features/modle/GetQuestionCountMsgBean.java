package com.yuefeng.features.modle;

import java.io.Serializable;

public class GetQuestionCountMsgBean implements Serializable {
    /**
     * allcount : 0
     * waitcount : 0
     * doingcount : 2
     * waitclosecount : 1
     * finishcount : 0
     */

    private String allcount;
    private String waitcount;
    private String doingcount;
    private String waitclosecount;
    private String finishcount;

    public String getAllcount() {
        return allcount;
    }

    public void setAllcount(String allcount) {
        this.allcount = allcount;
    }

    public String getWaitcount() {
        return waitcount;
    }

    public void setWaitcount(String waitcount) {
        this.waitcount = waitcount;
    }

    public String getDoingcount() {
        return doingcount;
    }

    public void setDoingcount(String doingcount) {
        this.doingcount = doingcount;
    }

    public String getWaitclosecount() {
        return waitclosecount;
    }

    public void setWaitclosecount(String waitclosecount) {
        this.waitclosecount = waitclosecount;
    }

    public String getFinishcount() {
        return finishcount;
    }

    public void setFinishcount(String finishcount) {
        this.finishcount = finishcount;
    }
}
