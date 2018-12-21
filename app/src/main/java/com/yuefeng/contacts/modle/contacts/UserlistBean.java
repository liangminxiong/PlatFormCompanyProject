package com.yuefeng.contacts.modle.contacts;

import java.io.Serializable;

public class UserlistBean implements Serializable {
    /**
     * id : 10f3ff8fffffffc924aa07ff25b946a1
     * pid : dg1168
     * name : 侨银环保
     */

    private String id;
    private String pid;
    private String name;
    private boolean haschild = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHaschild() {
        return haschild;
    }

    public void setHaschild(boolean haschild) {
        this.haschild = haschild;
    }
}
