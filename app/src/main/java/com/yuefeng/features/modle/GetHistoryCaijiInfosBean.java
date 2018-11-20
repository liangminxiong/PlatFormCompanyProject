package com.yuefeng.features.modle;

import java.io.Serializable;
import java.util.List;


/*历史采集信息
 **/
public class GetHistoryCaijiInfosBean implements Serializable {


    /**
     * success : true
     * msgTitle : 成功提示
     * msg : [{"id":"d7937fe5ffffee01000d4258bce6e0b9","name":"好的咕咚咕咚","lnglat":"113.402121,23.154958","imgeurl":"http://10.0.0.68:8080/webfiles/zgbd_rubbish/map/image/2018110311173977794.png,http://10.0.0.68:8080/webfiles/zgbd_rubbish/map/image/2018110311173954820.png,http://10.0.0.68:8080/webfiles/zgbd_rubbish/map/image/2018110311173982362.png","typename":"垃圾点","time":"2018-11-03 11:17:39"},{"id":"fcf73799ffffffc95c9b0000079a39cd","name":"骨灰盒","lnglat":"113.402441,23.154955","imgeurl":"http://120.78.217.251:80/webfiles/zgbd_rubbish/map/image/2018111017323179918.png,http://120.78.217.251:80/webfiles/zgbd_rubbish/map/image/2018111017323144448.png,http://120.78.217.251:80/webfiles/zgbd_rubbish/map/image/2018111017323173105.png","typename":"生活垃圾采集点","time":"2018-11-10 17:32:31"},{"id":"e7c4b9a6ffffffc94be0006f3d52c870","name":"测试测试结果","lnglat":"113.402159,23.155138","imgeurl":"http://120.78.217.251:80/webfiles/zgbd_rubbish/map/image/2018110614452069997.png,http://120.78.217.251:80/webfiles/zgbd_rubbish/map/image/2018110614452123864.png,http://120.78.217.251:80/webfiles/zgbd_rubbish/map/image/2018110614452181684.png","typename":"生活垃圾采集点","time":"2018-11-06 14:45:20"},{"id":"d41c1b5affffee0101bcf50e987e6ea7","name":"测试你","lnglat":"113.402297,23.154985","imgeurl":"http://10.0.0.68:8080/webfiles/zgbd_rubbish/map/image/2018110219082386557.png","typename":"生活垃圾采集点","time":"2018-11-02 19:08:23"},{"id":"d3fe1b7bffffee0101bcf50e4af82f60","name":"测试测试","lnglat":"113.390930,23.150436","imgeurl":"http://10.0.0.68:8080/webfiles/zgbd_rubbish/map/image/2018110218353713751.png,http://10.0.0.68:8080/webfiles/zgbd_rubbish/map/image/2018110218353718836.png,http://10.0.0.68:8080/webfiles/zgbd_rubbish/map/image/2018110218353740606.png","typename":"生活垃圾采集点","time":"2018-11-02 18:35:37"},{"id":"fcfb936bffffffc95c9b000019381726","name":"很好","lnglat":"113.39573780512212,23.148727392038055;113.40215149847243,23.154964109296753;113.40233467926555,23.154943265771404;113.40230428233536,23.15497769737287;113.40223562266013,23.15499476704335;113.4022280235388,23.155002358115414;113.40219735481422,23.155044382385622;113.40220495290698,23.155050078286237;113.40220495174515,23.155065263411146;113.39573780512212,23.148727392038055","imgeurl":"http://120.78.217.251:80/webfiles/zgbd_rubbish/map/image/201811101737175644.png,http://120.78.217.251:80/webfiles/zgbd_rubbish/map/image/201811101737173997.png,http://120.78.217.251:80/webfiles/zgbd_rubbish/map/image/2018111017371719756.png","typename":"作业网格","time":"2018-11-10 17:37:17"},{"id":"e7cccd37ffffffc94be0006f9ba5576b","name":"测试测试","lnglat":"113.40210591022763,23.154928035926623;113.40219736037729,23.15497171072004;113.40222802690252,23.154958429720473;113.40220495919652,23.15496791591746;113.40210591022763,23.154928035926623","imgeurl":"http://120.78.217.251:80/webfiles/zgbd_rubbish/map/image/201811061454107939.png,http://120.78.217.251:80/webfiles/zgbd_rubbish/map/image/2018110614541053929.png,http://120.78.217.251:80/webfiles/zgbd_rubbish/map/image/2018110614541076367.png","typename":"作业网格","time":"2018-11-06 14:54:10"},{"id":"d7932f60ffffee01000d4258b37a449c","name":"测试测试","lnglat":"113.40212870571837,23.154928040265457;113.40214389529307,23.15502511949481;113.40212110022101,23.15501942071556;113.402136296497,23.15502891432017;113.40215909260317,23.155021326133564;113.40216696745476,23.154956519712233;113.402136304221,23.154928041717504;113.40212870571837,23.154928040265457","imgeurl":"http://10.0.0.68:8080/webfiles/zgbd_rubbish/map/image/2018110311171996895.png,http://10.0.0.68:8080/webfiles/zgbd_rubbish/map/image/2018110311171991541.png,http://10.0.0.68:8080/webfiles/zgbd_rubbish/map/image/2018110311171958521.png","typename":"作业网格","time":"2018-11-03 11:17:19"},{"id":"d773305affffee01000d4258b76e9979","name":"测试测试","lnglat":"113.39576976933175,23.148763406421633;113.40213609313757,23.15414410999461;113.40216696716404,23.15496031599301;113.40216696789093,23.154950825291095;113.39576976933175,23.148763406421633","imgeurl":"http://10.0.0.68:8080/webfiles/zgbd_rubbish/map/image/201811031042229392.png,http://10.0.0.68:8080/webfiles/zgbd_rubbish/map/image/2018110310422226530.png","typename":"作业网格","time":"2018-11-03 10:42:22"},{"id":"fcf9ce32ffffffc95c9b000026fd94fd","name":"灌灌灌","lnglat":"113.402175,23.155000","imgeurl":"http://120.78.217.251:80/webfiles/zgbd_rubbish/map/image/2018111017352184660.png,http://120.78.217.251:80/webfiles/zgbd_rubbish/map/image/2018111017352162163.png,http://120.78.217.251:80/webfiles/zgbd_rubbish/map/image/2018111017352192495.png","typename":"作业线路","time":"2018-11-10 17:35:21"},{"id":"fcf7a8a0ffffffc95c9b000066a5bb88","name":"嘿嘿嘿","lnglat":"113.402098,23.154907","imgeurl":"http://120.78.217.251:80/webfiles/zgbd_rubbish/map/image/2018111017330052419.png","typename":"作业线路","time":"2018-11-10 17:33:00"},{"id":"e7cabeaaffffffc94be0006f556da84f","name":"测试测试","lnglat":"113.402205,23.154993","imgeurl":"http://120.78.217.251:80/webfiles/zgbd_rubbish/map/image/2018110614515535352.png,http://120.78.217.251:80/webfiles/zgbd_rubbish/map/image/2018110614515531842.png,http://120.78.217.251:80/webfiles/zgbd_rubbish/map/image/2018110614515534844.png","typename":"作业线路","time":"2018-11-06 14:51:55"},{"id":"d790183fffffee01000d42583f6dc836","name":"很多很多","lnglat":"113.402358,23.155037","imgeurl":"http://10.0.0.68:8080/webfiles/zgbd_rubbish/map/image/201811031113567269.png,http://10.0.0.68:8080/webfiles/zgbd_rubbish/map/image/2018110311135639321.png,http://10.0.0.68:8080/webfiles/zgbd_rubbish/map/image/2018110311135618757.png","typename":"作业线路","time":"2018-11-03 11:13:56"},{"id":"d41d922cffffee0101bcf50ebcf7702d","name":"测试中","lnglat":"113.402243,23.155008","imgeurl":"http://10.0.0.68:8080/webfiles/zgbd_rubbish/map/image/2018110219095917445.png","typename":"作业线路","time":"2018-11-02 19:09:59"},{"id":"d40ea54cffffee0101bcf50ec32a4b04","name":"测试一下","lnglat":"113.402395,23.154989","imgeurl":"http://10.0.0.68:8080/webfiles/zgbd_rubbish/map/image/2018110218530499146.png,http://10.0.0.68:8080/webfiles/zgbd_rubbish/map/image/2018110218530472609.png","typename":"作业线路","time":"2018-11-02 18:53:04"},{"id":"d40837abffffee0101bcf50e72e2fcab","name":"测试版","lnglat":"113.390229,23.151106","imgeurl":"http://10.0.0.68:8080/webfiles/zgbd_rubbish/map/image/2018110218460693926.png","typename":"作业线路","time":"2018-11-02 18:46:06"},{"id":"d404138affffee0101bcf50e263b42c2","name":"测试测试结果","lnglat":"113.389904,23.151268","imgeurl":"http://10.0.0.68:8080/webfiles/zgbd_rubbish/map/image/2018110218420829598.png,http://10.0.0.68:8080/webfiles/zgbd_rubbish/map/image/2018110218420869781.png","typename":"作业线路","time":"2018-11-02 18:42:08"}]
     */

    private boolean success;
    private String msgTitle;
    private List<GetHistoryCaijiInfosMsgBean> msg;

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

    public List<GetHistoryCaijiInfosMsgBean> getMsg() {
        return msg;
    }

    public void setMsg(List<GetHistoryCaijiInfosMsgBean> msg) {
        this.msg = msg;
    }
}
