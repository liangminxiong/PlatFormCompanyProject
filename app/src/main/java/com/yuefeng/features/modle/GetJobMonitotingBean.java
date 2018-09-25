package com.yuefeng.features.modle;

import java.io.Serializable;

public class GetJobMonitotingBean implements Serializable {
    /*作业监控*/

    /**
     * success : true
     * msgTitle : 成功提示
     * msg : {"vehiclenum":2,"personalnum":4,"questionnum":2,"vehicleinfoList":[{"id":"eab8a86affffffc976ce72860f4a52ae","registrationNO":"工作车辆1","terminalNO":"601:123","simNO":"123","pid":"环卫测试","terminalTypeID":"601","stateType":"","carType":"7648dc72ffa801030020b8ad9a32aa5d","longitude":"","latitude":""},{"id":"eab91657ffffffc976ce7286d47b09b3","registrationNO":"工作车辆2","terminalNO":"601:2344","simNO":"432","pid":"环卫测试","terminalTypeID":"601","stateType":"","carType":"","longitude":"","latitude":""}],"personalinfoList":[{"id":"eab2ffacffffffc976ce7286d4054823","pid":"环卫测试","name":"张三","tel":"13166668888","position":"主管","terminalNO":"101:145","terminalTypeID":"101","simNO":"","longitude":"","latitude":"","stateType":""},{"id":"eab3c9d3ffffffc976ce7286ad153c1b","pid":"环卫测试","name":"李四","tel":"13188886666","position":"普通员工","terminalNO":"101:2344","terminalTypeID":"101","simNO":"","longitude":"","latitude":"","stateType":""},{"id":"eab563fbffffffc976ce72866c1a4dac","pid":"环卫测试","name":"王五","tel":"13688886666","position":"普通员工","terminalNO":"101:1","terminalTypeID":"101","simNO":"","longitude":"","latitude":"","stateType":""},{"id":"eac99227ffffffc976ce72867fb4d7f8","pid":"环卫测试","name":"小古","tel":"13760895045","position":"经理","terminalNO":"101:2","terminalTypeID":"101","simNO":"","longitude":"","latitude":"","stateType":""}],"questionList":[{"id":"68","pid":"ea9b4033ffffee0101ed1860a1febcfb","address":"广东省广州市天河区新塘大街28","problem":"描述","uploadpeople":"eab2ffacffffffc976ce7286d4054823","uploadtime":"2018-09-18 17:25:50.0","imgurl":"http://120.78.217.251:80/webfiles/zgbd_rubbish/wxupload/image/2018091817254736562.png,http://120.78.217.251:80/webfiles/zgbd_rubbish/wxupload/image/2018091817254913919.png","state":"1","longitude":"113.414184","latitude":"23.158781","type":"1","uploadpeoplename":"张三","claimid":"","claimpeople":""},{"id":"69","pid":"ea9b4033ffffee0101ed1860a1febcfb","address":"广东省广州市天河区新塘大街28","problem":"把下半生","uploadpeople":"eab2ffacffffffc976ce7286d4054823","uploadtime":"2018-09-18 17:26:10.0","imgurl":"http://120.78.217.251:80/webfiles/zgbd_rubbish/wxupload/image/2018091817260997318.png","state":"1","longitude":"113.414209","latitude":"23.158793","type":"0","uploadpeoplename":"张三","claimid":"","claimpeople":""}]}
     */

    private boolean success;
    private String msgTitle;
    private GetJobMonitotingMsgBean msg;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsgTitle() {
        return msgTitle;
    }

    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle;
    }

    public GetJobMonitotingMsgBean getMsg() {
        return msg;
    }

    public void setMsg(GetJobMonitotingMsgBean msg) {
        this.msg = msg;
    }
}
