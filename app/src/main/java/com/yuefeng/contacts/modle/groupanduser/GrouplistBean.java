package com.yuefeng.contacts.modle.groupanduser;

import java.io.Serializable;
import java.util.List;

public class GrouplistBean implements Serializable {
    /**
     * id : c0353bcc0a00004212dfbaa8ebb90d1f
     * name : 测试组12-18
     * createid : eab2ffacffffffc976ce7286d4054823
     * userlist : []
     */

    private String id;
    private String name;
    private String createid;
    private List<?> userlist;

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

    public String getCreateid() {
        return createid;
    }

    public void setCreateid(String createid) {
        this.createid = createid;
    }

    public List<?> getUserlist() {
        return userlist;
    }

    public void setUserlist(List<?> userlist) {
        this.userlist = userlist;
    }
}
