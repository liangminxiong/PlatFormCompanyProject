package com.yuefeng.contacts.modle.groupanduser;

import java.io.Serializable;
import java.util.List;

public class GroupQueryWithUserDataBean implements Serializable {
    /**
     * id : eac99227ffffffc976ce72867fb4d7f8
     * name : 小古
     * icon :
     * grouplist : [{"id":"c0353bcc0a00004212dfbaa8ebb90d1f","name":"测试组12-18","createid":"eab2ffacffffffc976ce7286d4054823","userlist":[]},{"id":"c03b3d590a00004212dfbaa8b0879ffc","name":"测试组12-18","createid":"eac99227ffffffc976ce72867fb4d7f8","userlist":[]},{"id":"c07712850a00004212dfbaa874c37a3e","name":"测试组12-18","createid":"eab2ffacffffffc976ce7286d4054823","userlist":[]},{"id":"c0776ca70a00004212dfbaa8898e6d73","name":"测试组12-18","createid":"eab2ffacffffffc976ce7286d4054823","userlist":[]},{"id":"c0b219f70a00004212dfbaa8034d7314","name":"大神吐槽","createid":"bf7440daffffffc958722e59f33a67d9","userlist":[]},{"id":"c0b54a2affffffc9559812843713b130","name":"大神吐槽2","createid":"bf7440daffffffc958722e59f33a67d9","userlist":[]},{"id":"c0b85375ffffffc95598128400e6db2e","name":"大神吐槽2","createid":"bf7440daffffffc958722e59f33a67d9","userlist":[]},{"id":"c0b9afbaffffffc95598128409b84cd0","name":"大神吐槽2","createid":"bf7440daffffffc958722e59f33a67d9","userlist":[]},{"id":"c0bb5415ffffffc95598128464ef2a8d","name":"大神吐槽2","createid":"bf7440daffffffc958722e59f33a67d9","userlist":[]},{"id":"c0f22509ffffffc9559812847281a2ad","name":"大神吐槽3","createid":"bf7440daffffffc958722e59f33a67d9","userlist":[]},{"id":"ca906b3cffa801031bdc8a3dbcd521d6","name":"测试组12-18","createid":"eab2ffacffffffc976ce7286d4054823","userlist":[]}]
     */

    private String id;
    private String name;
    private String icon;
    private List<GrouplistBean> grouplist;

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

    public List<GrouplistBean> getGrouplist() {
        return grouplist;
    }

    public void setGrouplist(List<GrouplistBean> grouplist) {
        this.grouplist = grouplist;
    }

}
