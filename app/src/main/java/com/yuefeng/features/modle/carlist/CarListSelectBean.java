package com.yuefeng.features.modle.carlist;

import java.io.Serializable;

public class CarListSelectBean implements Serializable {

    private String name;
    private String type;
    private String terminal;

    @Override
    public String toString() {
        return "CarListSelectBean{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", terminal='" + terminal + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

}
