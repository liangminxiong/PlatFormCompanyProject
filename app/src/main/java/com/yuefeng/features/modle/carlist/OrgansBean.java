package com.yuefeng.features.modle.carlist;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/7/20.
 */

public class OrgansBean implements Serializable {
    /**
     * id : jst1482
     * pid : jst774
     * orgCode : jst1482
     * orgName : 张勋
     * orgShortName : 张勋
     * principal :
     * principalTel :
     * fax :
     * address :
     * organs : []
     * vehicles : [{"id":"jst15142","registrationNO":"赣CC6638","terminalNO":"601:13825944356","simNO":"13825944356","pid":"jst1482","terminalTypeID":"10","stateType":"1","gpsTime":"2018-07-20 16:45:51.0","oilMax":"0","oilSum":"0","brandType":"重型自卸货车","carType":"货车","frameNumber":"LJ13R6FK3H3314049","engineNumber":"","masterID":".|13902781817.|.","password":"","driverID1":"13301107622.|.","driverID2":".|.","limitedDate":"2010-09-21 00:00:00.0","memo":"","hasvideo":"0","videoterminalNO":"","gt":"2018-07-20 16:43:21.0","speed":"0","obd":"ACC开","lat":"24.056550","lon":"115.970048"},{"id":"jst10221","registrationNO":"粤MN1656","terminalNO":"601:18814072413","simNO":"18814072413","pid":"jst1482","terminalTypeID":"10","stateType":"0","gpsTime":"2018-07-20 16:43:13.0","oilMax":"0","oilSum":"0","brandType":"重型自卸货车","carType":"变色小车","frameNumber":"","engineNumber":"","masterID":"","password":"123456","driverID1":"","driverID2":"","limitedDate":"2010-09-21 00:00:00.0","memo":"","hasvideo":"","videoterminalNO":"","gt":"2018-07-20 16:43:13.0","speed":"0","obd":"ACC关","lat":"24.311532","lon":"116.075544"},{"id":"jst15143","registrationNO":"赣C5L786","terminalNO":"601:13825944351","simNO":"13825944351","pid":"jst1482","terminalTypeID":"10","stateType":"0","gpsTime":"2018-07-20 02:49:19.0","oilMax":"0","oilSum":"0","brandType":"重型自卸货车","carType":"货车","frameNumber":"LJ13R6FK1J3305274","engineNumber":"","masterID":".|13902781817.|.","password":"","driverID1":"13301107625.|.","driverID2":".|.","limitedDate":"2010-09-21 00:00:00.0","memo":"","hasvideo":"0","videoterminalNO":"","gt":"2018-07-20 02:48:23.0","speed":"0","obd":"ACC开","lat":"24.216900","lon":"116.631568"}]
     */

    private String id;
    private String pid;
    private String orgShortName;
    private String orgCode;
    private String orgName;
    private String principal;
    private String principalTel;
    private String fax;
    private String address;
    private List<Organ> organs;
    private List<VehiclesBean> vehicles;

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

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgShortName() {
        return orgShortName;
    }

    public void setOrgShortName(String orgShortName) {
        this.orgShortName = orgShortName;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getPrincipalTel() {
        return principalTel;
    }

    public void setPrincipalTel(String principalTel) {
        this.principalTel = principalTel;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Organ> getOrgans() {
        return organs;
    }

    public void setOrgans(List<Organ> organs) {
        this.organs = organs;
    }

    public List<VehiclesBean> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<VehiclesBean> vehicles) {
        this.vehicles = vehicles;
    }

}
