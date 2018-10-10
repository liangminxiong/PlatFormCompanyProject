package com.yuefeng.features.modle.video;

import java.io.Serializable;

public class VideoesBean implements Serializable {
//    "createTime":"2018-07-17 09:44:56.0","id":"a5e97ef6ffffffc9530ca17125570784","name":"45","pid":"87418d64ffffffc943bb121973682e3d","terminalno":"5465","txh":"54"

    /**
     * createTime : 2018-04-27 15:34:42.0
     * id : 06064845ffffffc95ae293ce0a544617
     * name : 视频3
     * pid : c5073eab0a00000518c6c9b9c959a415
     * terminalno : 211999
     * txh : 211999
     */

    public void VideoesBean() {

    }

    private String createTime;
    private String id;
    private String name;
    private String pid;
    private String terminalno;
    private String txh;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTerminalno() {
        return terminalno;
    }

    public void setTerminalno(String terminalno) {
        this.terminalno = terminalno;
    }

    public String getTxh() {
        return txh;
    }

    public void setTxh(String txh) {
        this.txh = txh;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
