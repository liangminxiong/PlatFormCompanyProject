package com.yuefeng.features.modle;

import java.io.Serializable;
import java.util.List;

/*报警消息列表*/
public class AlarmListBean implements Serializable {


    /**
     * code : 0
     * msg :
     * count : 7577
     * data : [{"longitude":"116.866616","id":"df240f8cffffffc90a40c62571834da8","speed":"0","terminalNO":"601:10198829423","dealType":"","op":"围栏分析，离开区域：淮北市相山区电子围栏","latitude":"33.986788","gpsTime":"2018-12-24 15:35:18.0","registrationNO":"皖F37849（1号除雪车）"},{"longitude":"116.796352","id":"df240f8cffffffc90a40c6256121cb79","speed":"0","terminalNO":"601:14532623241","dealType":"","op":"围栏分析，离开区域：淮北市相山区电子围栏","latitude":"33.926984","gpsTime":"2018-12-24 15:35:16.0","registrationNO":"电动平板车10号"},{"longitude":"116.75308","id":"df240f8cffffffc90a40c625b7b0ee4a","speed":"0","terminalNO":"601:13301444673","dealType":"","op":"围栏分析，离开区域：淮北市相山区电子围栏","latitude":"33.9274","gpsTime":"2018-12-24 15:35:15.0","registrationNO":"皖F35246(15号洗扫车)"},{"longitude":"116.610944","id":"df240f8cffffffc90a40c6253af80bdf","speed":"6","terminalNO":"601:13300597609","dealType":"","op":"围栏分析，离开区域：淮北市相山区电子围栏","latitude":"33.667268","gpsTime":"2018-12-24 15:35:09.0","registrationNO":"粤ACR208(1号雾炮车)"},{"longitude":"116.847648","id":"df240f8cffffffc90a40c625c9e1915f","speed":"69","terminalNO":"601:13301444664","dealType":"","op":"围栏分析，离开区域：淮北市相山区电子围栏","latitude":"33.990568","gpsTime":"2018-12-24 15:35:03.0","registrationNO":"皖F37025(2号压缩车)"}]
     */

    private int code;
    private String msg;
    private int count;
    private List<AlarmDataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<AlarmDataBean> getData() {
        return data;
    }

    public void setData(List<AlarmDataBean> data) {
        this.data = data;
    }
}
