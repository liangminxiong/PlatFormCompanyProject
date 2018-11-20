package com.yuefeng.features.modle.carlist;

import java.io.Serializable;

public class CarListSelectBean implements Serializable {

    private String id;
    private String name;
    private String type;
    private String terminal;
    private String terminalId;

    @Override
    public String toString() {
        return "CarListSelectBean{" +
                "id='" + id + '\'' +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", terminal='" + terminal + '\'' +
                ", terminalId='" + terminalId + '\'' +
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }
}
