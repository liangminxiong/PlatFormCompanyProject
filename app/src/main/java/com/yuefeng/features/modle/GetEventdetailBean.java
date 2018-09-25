package com.yuefeng.features.modle;

import java.io.Serializable;
import java.util.List;

public class GetEventdetailBean implements Serializable {

    /**
     * success : true
     * msgTitle : 成功提示
     * msg : [{"id":"8","pid":"侨银环保科技股份有限公司","address":"地址","problem":"问题",
     * "uploadtime":"2018-09-05 13:57:15.0",
     * "imgurl":"http://10.0.0.68:8080/webfiles/zgbd_rubbish/wxupload/image/2018090513571542540.png,",
     * "state":"4","closetime":"2018-09-07 15:11:12.0","longitude":"132.2222","latitude":"12.222",
     * "type":"0","time":"2天13分钟57秒","detail":[{"id":"1","problenid":"8","userid":"侨银环保公司",
     * "time":"2018-09-05 13:57:15.0","things":"上报问题","imgurl":"","detail":"","pinjia":""},
     * {"id":"7","problenid":"8","userid":"侨银环保公司","time":"2018-09-06 14:51:48.0",
     * "things":"认领问题","imgurl":"","detail":"","pinjia":""},{"id":"17","problenid":"8",
     * "userid":"侨银环保公司","time":"2018-09-06 17:17:25.0","things":"完成处理","imgurl":"",
     * "detail":"","pinjia":""},{"id":"21","problenid":"8","userid":"侨银环保公司","time":"2018-09-06 17:44:29.0","things":"关闭问题","imgurl":"","detail":"","pinjia":""},{"id":"38","problenid":"8","userid":"侨银环保公司","time":"2018-09-07 11:57:40.0","things":"认领问题","imgurl":"","detail":"","pinjia":""},{"id":"41","problenid":"8","userid":"侨银环保公司","time":"2018-09-07 14:30:12.0","things":"完成处理","imgurl":"","detail":"","pinjia":""},{"id":"42","problenid":"8","userid":"侨银环保公司","time":"2018-09-07 14:31:42.0","things":"完成处理","imgurl":"http://10.0.0.68:8080/webfiles/zgbd_rubbish/wxupload/image/2018090714314222602.png","detail":"","pinjia":""},{"id":"44","problenid":"8","userid":"侨银环保公司","time":"2018-09-07 15:10:05.0","things":"关闭问题","imgurl":"","detail":"aqqq","pinjia":"非常好"},{"id":"45","problenid":"8","userid":"侨银环保公司","time":"2018-09-07 15:10:07.0","things":"关闭问题","imgurl":"","detail":"aqqq","pinjia":"非常好"},{"id":"46","problenid":"8","userid":"侨银环保公司","time":"2018-09-07 15:10:09.0","things":"关闭问题","imgurl":"","detail":"aqqq","pinjia":"非常好"},{"id":"48","problenid":"8","userid":"侨银环保公司","time":"2018-09-07 15:11:12.0","things":"关闭问题","imgurl":"","detail":"gvgh","pinjia":"好"}]}]
     */

    private boolean success;
    private String msgTitle;
    private List<GetEventdetailMsgBean> msg;

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

    public List<GetEventdetailMsgBean> getMsg() {
        return msg;
    }

    public void setMsg(List<GetEventdetailMsgBean> msg) {
        this.msg = msg;
    }
}
