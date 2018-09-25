package com.yuefeng.features.modle;

import java.io.Serializable;
import java.util.List;

public class GetEventdetailMsgBean implements Serializable {

    /**
     * id : 8
     * pid : 侨银环保科技股份有限公司
     * address : 地址
     * problem : 问题
     * uploadtime : 2018-09-05 13:57:15.0
     * imgurl : http://10.0.0.68:8080/webfiles/zgbd_rubbish/wxupload/image/2018090513571542540.png,
     * state : 4
     * closetime : 2018-09-07 15:11:12.0
     * longitude : 132.2222
     * latitude : 12.222
     * type : 0
     * time : 2天13分钟57秒
     * detail : [{"id":"1","problenid":"8","userid":"侨银环保公司","time":"2018-09-05 13:57:15.0","things":"上报问题","imgurl":"","detail":"","pinjia":""},{"id":"7","problenid":"8","userid":"侨银环保公司","time":"2018-09-06 14:51:48.0","things":"认领问题","imgurl":"","detail":"","pinjia":""},{"id":"17","problenid":"8","userid":"侨银环保公司","time":"2018-09-06 17:17:25.0","things":"完成处理","imgurl":"","detail":"","pinjia":""},{"id":"21","problenid":"8","userid":"侨银环保公司","time":"2018-09-06 17:44:29.0","things":"关闭问题","imgurl":"","detail":"","pinjia":""},{"id":"38","problenid":"8","userid":"侨银环保公司","time":"2018-09-07 11:57:40.0","things":"认领问题","imgurl":"","detail":"","pinjia":""},{"id":"41","problenid":"8","userid":"侨银环保公司","time":"2018-09-07 14:30:12.0","things":"完成处理","imgurl":"","detail":"","pinjia":""},{"id":"42","problenid":"8","userid":"侨银环保公司","time":"2018-09-07 14:31:42.0","things":"完成处理","imgurl":"http://10.0.0.68:8080/webfiles/zgbd_rubbish/wxupload/image/2018090714314222602.png","detail":"","pinjia":""},{"id":"44","problenid":"8","userid":"侨银环保公司","time":"2018-09-07 15:10:05.0","things":"关闭问题","imgurl":"","detail":"aqqq","pinjia":"非常好"},{"id":"45","problenid":"8","userid":"侨银环保公司","time":"2018-09-07 15:10:07.0","things":"关闭问题","imgurl":"","detail":"aqqq","pinjia":"非常好"},{"id":"46","problenid":"8","userid":"侨银环保公司","time":"2018-09-07 15:10:09.0","things":"关闭问题","imgurl":"","detail":"aqqq","pinjia":"非常好"},{"id":"48","problenid":"8","userid":"侨银环保公司","time":"2018-09-07 15:11:12.0","things":"关闭问题","imgurl":"","detail":"gvgh","pinjia":"好"}]
     */

    private String id;
    private String pid;
    private String address;
    private String problem;
    private String uploadtime;
    private String imgurl;
    private String state;
    private String closetime;
    private String longitude;
    private String latitude;
    private String type;
    private String time;
    private List<EventdetailMsgBean> detail;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getUploadtime() {
        return uploadtime;
    }

    public void setUploadtime(String uploadtime) {
        this.uploadtime = uploadtime;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getClosetime() {
        return closetime;
    }

    public void setClosetime(String closetime) {
        this.closetime = closetime;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<EventdetailMsgBean> getDetail() {
        return detail;
    }

    public void setDetail(List<EventdetailMsgBean> detail) {
        this.detail = detail;
    }

}
