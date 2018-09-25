package com.yuefeng.features.modle;

import java.io.Serializable;
import java.util.List;

public class GetJobMonitotingMsgBean implements Serializable {
    /**
     * vehiclenum : 2
     * personalnum : 4
     * questionnum : 2
     * vehicleinfoList : [a801030020b8ad9a32aa5d","longitude":"","latitude":""},
     * personalinfoList : [{"id":"eab2ffacffffffc976ce7286d4054823","pid":"环卫测试","name":"张三","tel":"13166668888","position":"主管","terminalNO":"101:145","terminalTypeID":"101","simNO":"","longitude":"","latitude":"","stateType":""},{"id":"eab3c9d3ffffffc976ce7286ad153c1b","pid":"环卫测试","name":"李四","tel":"13188886666","position":"普通员工","terminalNO":"101:2344","terminalTypeID":"101","simNO":"","longitude":"","latitude":"","stateType":""},{"id":"eab563fbffffffc976ce72866c1a4dac","pid":"环卫测试","name":"王五","tel":"13688886666","position":"普通员工","terminalNO":"101:1","terminalTypeID":"101","simNO":"","longitude":"","latitude":"","stateType":""},{"id":"eac99227ffffffc976ce72867fb4d7f8","pid":"环卫测试","name":"小古","tel":"13760895045","position":"经理","terminalNO":"101:2","terminalTypeID":"101","simNO":"","longitude":"","latitude":"","stateType":""}]
     * questionList : [{"id":"68","pid":"ea9b4033ffffee0101ed1860a1febcfb","address":"广东省广州市天河区新塘大街28","problem":"描述","uploadpeople":"eab2ffacffffffc976ce7286d4054823","uploadtime":"2018-09-18 17:25:50.0","imgurl":"http://120.78.217.251:80/webfiles/zgbd_rubbish/wxupload/image/2018091817254736562.png,http://120.78.217.251:80/webfiles/zgbd_rubbish/wxupload/image/2018091817254913919.png","state":"1","longitude":"113.414184","latitude":"23.158781","type":"1","uploadpeoplename":"张三","claimid":"","claimpeople":""},{"id":"69","pid":"ea9b4033ffffee0101ed1860a1febcfb","address":"广东省广州市天河区新塘大街28","problem":"把下半生","uploadpeople":"eab2ffacffffffc976ce7286d4054823","uploadtime":"2018-09-18 17:26:10.0","imgurl":"http://120.78.217.251:80/webfiles/zgbd_rubbish/wxupload/image/2018091817260997318.png","state":"1","longitude":"113.414209","latitude":"23.158793","type":"0","uploadpeoplename":"张三","claimid":"","claimpeople":""}]
     */

    private int vehiclenum;
    private int personalnum;
    private int questionnum;
    private List<VehicleinfoListBean> vehicleinfoList;
    private List<PersonalinfoListBean> personalinfoList;
    private List<QuestionListBean> questionList;

    public int getVehiclenum() {
        return vehiclenum;
    }

    public void setVehiclenum(int vehiclenum) {
        this.vehiclenum = vehiclenum;
    }

    public int getPersonalnum() {
        return personalnum;
    }

    public void setPersonalnum(int personalnum) {
        this.personalnum = personalnum;
    }

    public int getQuestionnum() {
        return questionnum;
    }

    public void setQuestionnum(int questionnum) {
        this.questionnum = questionnum;
    }

    public List<VehicleinfoListBean> getVehicleinfoList() {
        return vehicleinfoList;
    }

    public void setVehicleinfoList(List<VehicleinfoListBean> vehicleinfoList) {
        this.vehicleinfoList = vehicleinfoList;
    }

    public List<PersonalinfoListBean> getPersonalinfoList() {
        return personalinfoList;
    }

    public void setPersonalinfoList(List<PersonalinfoListBean> personalinfoList) {
        this.personalinfoList = personalinfoList;
    }

    public List<QuestionListBean> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<QuestionListBean> questionList) {
        this.questionList = questionList;
    }

}
