package com.yuefeng.contacts.modle.contacts;

import java.io.Serializable;
import java.util.List;

public class GroupListDataBean implements Serializable {
    /**
     * id : fdaeb0e9ffa801030bc5ff59d8b6eb04
     * name : 聊扣扣
     * createid : f9276992ffffffc944becffdba449a2d
     * userlist : [{"id":"efdb1590ffffffc9449c873e605d08c1","name":"杨杨","icon":"http://testresource.hangyunejia.com/resource/uploads/file/20181212/YM1mlVZxMnpBAhM2dBiK.jpeg","grouplist":[]},{"id":"f9276992ffffffc944becffdba449a2d","name":"小梁","icon":"http://testresource.hangyunejia.com/resource/uploads/file/20181212/YM1mlVZxMnpBAhM2dBiK.jpeg","grouplist":[]}]
     */

    private String id;
    private String name;
    private String createid;
    private List<GroupUserlistBean> userlist;

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

    public List<GroupUserlistBean> getUserlist() {
        return userlist;
    }

    public void setUserlist(List<GroupUserlistBean> userlist) {
        this.userlist = userlist;
    }

}
