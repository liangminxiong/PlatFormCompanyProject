package com.yuefeng.features.modle;

import java.io.Serializable;

public class GetAllPersonalMsgBean implements Serializable {
    /**
     * name : 张三
     * id : eab2ffacffffffc976ce7286d4054823
     */

    private String name;
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
