package com.yuefeng.personaltree.model;

import java.io.Serializable;

public class PersonalBean implements Serializable {


    /**
     * id : d253daf1ffffffc901823b5957a60417
     * pid : e36920b3d7524f0aae195cc74e5720f2
     * name : 品质主管鲍爱民
     * tel : 17356109169
     * position : eaa6caf8ffffffc976ce7286606af088
     * terminalNO : 101:0867597012507197
     * terminalTypeID : 101
     * simNO : 1440154500027
     * longitude :
     * latitude :
     * stateType : 2
     */

    private String id;
    private String pid;
    private String name;
    private String tel;
    private String position;
    private String terminalNO;
    private String terminalTypeID;
    private String simNO;
    private String longitude;
    private String latitude;
    private String stateType;

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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getTerminalNO() {
        return terminalNO;
    }

    public void setTerminalNO(String terminalNO) {
        this.terminalNO = terminalNO;
    }

    public String getTerminalTypeID() {
        return terminalTypeID;
    }

    public void setTerminalTypeID(String terminalTypeID) {
        this.terminalTypeID = terminalTypeID;
    }

    public String getSimNO() {
        return simNO;
    }

    public void setSimNO(String simNO) {
        this.simNO = simNO;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getStateType() {
        return stateType;
    }

    public void setStateType(String stateType) {
        this.stateType = stateType;
    }
}
