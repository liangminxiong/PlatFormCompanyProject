package com.yuefeng.contacts.modle.contacts;

import java.io.Serializable;
import java.util.List;

public class OrganlistBean implements Serializable {
    /**
     * id : 370036d4ffffffc9651b3c5bdee78715
     * pid : dg1168
     * name : 河北省
     * icon :
     * isorgan : true
     * haschild : true
     * organlist : []
     */

    private String id;
    private String pid;
    private String name;
    private String icon;
    private boolean isorgan;
    private boolean haschild;
    private List<?> organlist;

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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isIsorgan() {
        return isorgan;
    }

    public void setIsorgan(boolean isorgan) {
        this.isorgan = isorgan;
    }

    public boolean isHaschild() {
        return haschild;
    }

    public void setHaschild(boolean haschild) {
        this.haschild = haschild;
    }

    public List<?> getOrganlist() {
        return organlist;
    }

    public void setOrganlist(List<?> organlist) {
        this.organlist = organlist;
    }
}
