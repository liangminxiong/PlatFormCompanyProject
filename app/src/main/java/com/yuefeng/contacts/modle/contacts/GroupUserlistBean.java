package com.yuefeng.contacts.modle.contacts;

import java.io.Serializable;
import java.util.List;

public class GroupUserlistBean implements Serializable {
    /**
     * id : efdb1590ffffffc9449c873e605d08c1
     * name : 杨杨
     * icon : http://testresource.hangyunejia.com/resource/uploads/file/20181212/YM1mlVZxMnpBAhM2dBiK.jpeg
     * grouplist : []
     */

    private String id;
    private String name;
    private String icon;
    private List<?> grouplist;

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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<?> getGrouplist() {
        return grouplist;
    }

    public void setGrouplist(List<?> grouplist) {
        this.grouplist = grouplist;
    }
}
