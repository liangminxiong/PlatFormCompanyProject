package com.yuefeng.features.presenter.zhuguansign;

import java.io.Serializable;
import java.util.List;

/*主管考勤查看轨迹*/
public class ExecutiveAtteanTrackBean implements Serializable {


    /**
     * success : true
     * msgTitle : 成功提示
     * msg : [{"id":"eab2ffacffffffc976ce7286d4054823","time":"2018-12-25 16:27:54.0","lng":"113.402208","lat":"23.155046","name":"","tel":"","address":""},{"id":"eab2ffacffffffc976ce7286d4054823","time":"2018-12-25 16:27:59.0","lng":"113.402323","lat":"23.155062","name":"","tel":"","address":""},{"id":"eab2ffacffffffc976ce7286d4054823","time":"2018-12-25 16:28:04.0","lng":"113.402244","lat":"23.155001","name":"","tel":"","address":""},{"id":"eab2ffacffffffc976ce7286d4054823","time":"2018-12-25 16:28:09.0","lng":"113.402233","lat":"23.154991","name":"","tel":"","address":""},{"id":"eab2ffacffffffc976ce7286d4054823","time":"2018-12-25 16:28:14.0","lng":"113.402233","lat":"23.154991","name":"","tel":"","address":""},{"id":"eab2ffacffffffc976ce7286d4054823","time":"2018-12-25 16:28:19.0","lng":"113.402233","lat":"23.154991","name":"","tel":"","address":""},{"id":"eab2ffacffffffc976ce7286d4054823","time":"2018-12-25 16:28:54.0","lng":"113.402191","lat":"23.155014","name":"","tel":"","address":"广东省广州市天河区新塘大街28"},{"id":"eab2ffacffffffc976ce7286d4054823","time":"2018-12-25 16:28:59.0","lng":"113.402191","lat":"23.155014","name":"","tel":"","address":"广东省广州市天河区新塘大街28"},{"id":"eab2ffacffffffc976ce7286d4054823","time":"2018-12-25 16:29:04.0","lng":"113.402186","lat":"23.154993","name":"","tel":"","address":"广东省广州市天河区新塘大街28"},{"id":"eab2ffacffffffc976ce7286d4054823","time":"2018-12-25 16:29:09.0","lng":"113.402186","lat":"23.154993","name":"","tel":"","address":"广东省广州市天河区新塘大街28"},{"id":"eab2ffacffffffc976ce7286d4054823","time":"2018-12-25 16:29:14.0","lng":"113.402186","lat":"23.154993","name":"","tel":"","address":"广东省广州市天河区新塘大街28"},{"id":"eab2ffacffffffc976ce7286d4054823","time":"2018-12-25 16:29:19.0","lng":"113.402223","lat":"23.155013","name":"","tel":"","address":"广东省广州市天河区新塘大街28"},{"id":"eab2ffacffffffc976ce7286d4054823","time":"2018-12-25 16:29:24.0","lng":"113.402223","lat":"23.155013","name":"","tel":"","address":"广东省广州市天河区新塘大街28"},{"id":"eab2ffacffffffc976ce7286d4054823","time":"2018-12-25 16:29:29.0","lng":"113.402223","lat":"23.155013","name":"","tel":"","address":"广东省广州市天河区新塘大街28"},{"id":"eab2ffacffffffc976ce7286d4054823","time":"2018-12-25 16:29:34.0","lng":"113.402223","lat":"23.155013","name":"","tel":"","address":"广东省广州市天河区新塘大街28"},{"id":"eab2ffacffffffc976ce7286d4054823","time":"2018-12-25 16:29:39.0","lng":"113.402223","lat":"23.155013","name":"","tel":"","address":"广东省广州市天河区新塘大街28"},{"id":"eab2ffacffffffc976ce7286d4054823","time":"2018-12-25 16:29:44.0","lng":"113.402211","lat":"23.155037","name":"","tel":"","address":"广东省广州市天河区新塘大街28"},{"id":"eab2ffacffffffc976ce7286d4054823","time":"2018-12-25 16:29:49.0","lng":"113.402211","lat":"23.155037","name":"","tel":"","address":"广东省广州市天河区新塘大街28"},{"id":"eab2ffacffffffc976ce7286d4054823","time":"2018-12-25 16:29:54.0","lng":"113.402211","lat":"23.155037","name":"","tel":"","address":"广东省广州市天河区新塘大街28"},{"id":"eab2ffacffffffc976ce7286d4054823","time":"2018-12-25 16:29:59.0","lng":"113.402210","lat":"23.155041","name":"","tel":"","address":"广东省广州市天河区新塘大街28"},{"id":"eab2ffacffffffc976ce7286d4054823","time":"2018-12-25 16:30:04.0","lng":"113.402210","lat":"23.155042","name":"","tel":"","address":"广东省广州市天河区新塘大街28"},{"id":"eab2ffacffffffc976ce7286d4054823","time":"2018-12-25 16:30:09.0","lng":"113.402210","lat":"23.155042","name":"","tel":"","address":"广东省广州市天河区新塘大街28"},{"id":"eab2ffacffffffc976ce7286d4054823","time":"2018-12-25 16:30:14.0","lng":"113.402169","lat":"23.155088","name":"","tel":"","address":"广东省广州市天河区新塘大街28"},{"id":"eab2ffacffffffc976ce7286d4054823","time":"2018-12-25 16:30:19.0","lng":"113.402169","lat":"23.155088","name":"","tel":"","address":"广东省广州市天河区新塘大街28"},{"id":"eab2ffacffffffc976ce7286d4054823","time":"2018-12-25 16:30:24.0","lng":"113.402169","lat":"23.155088","name":"","tel":"","address":"广东省广州市天河区新塘大街28"},{"id":"eab2ffacffffffc976ce7286d4054823","time":"2018-12-25 16:30:29.0","lng":"113.402201","lat":"23.155062","name":"","tel":"","address":"广东省广州市天河区新塘大街28"},{"id":"eab2ffacffffffc976ce7286d4054823","time":"2018-12-25 16:30:34.0","lng":"113.402201","lat":"23.155062","name":"","tel":"","address":"广东省广州市天河区新塘大街28"},{"id":"eab2ffacffffffc976ce7286d4054823","time":"2018-12-25 16:30:39.0","lng":"113.402201","lat":"23.155062","name":"","tel":"","address":"广东省广州市天河区新塘大街28"},{"id":"eab2ffacffffffc976ce7286d4054823","time":"2018-12-25 16:30:44.0","lng":"113.402201","lat":"23.155062","name":"","tel":"","address":"广东省广州市天河区新塘大街28"},{"id":"eab2ffacffffffc976ce7286d4054823","time":"2018-12-25 16:30:49.0","lng":"113.402201","lat":"23.155062","name":"","tel":"","address":"广东省广州市天河区新塘大街28"},{"id":"eab2ffacffffffc976ce7286d4054823","time":"2018-12-25 16:30:54.0","lng":"113.402201","lat":"23.155062","name":"","tel":"","address":"广东省广州市天河区新塘大街28"},{"id":"eab2ffacffffffc976ce7286d4054823","time":"2018-12-25 16:30:59.0","lng":"113.402201","lat":"23.155062","name":"","tel":"","address":"广东省广州市天河区新塘大街28"},{"id":"eab2ffacffffffc976ce7286d4054823","time":"2018-12-25 16:31:04.0","lng":"113.402201","lat":"23.155062","name":"","tel":"","address":"广东省广州市天河区新塘大街28"},{"id":"eab2ffacffffffc976ce7286d4054823","time":"2018-12-25 16:31:09.0","lng":"113.402201","lat":"23.155062","name":"","tel":"","address":"广东省广州市天河区新塘大街28"},{"id":"eab2ffacffffffc976ce7286d4054823","time":"2018-12-25 16:31:14.0","lng":"113.402201","lat":"23.155062","name":"","tel":"","address":"广东省广州市天河区新塘大街28"},{"id":"eab2ffacffffffc976ce7286d4054823","time":"2018-12-25 16:31:19.0","lng":"113.402201","lat":"23.155061","name":"","tel":"","address":"广东省广州市天河区新塘大街28"},{"id":"eab2ffacffffffc976ce7286d4054823","time":"2018-12-25 16:31:24.0","lng":"113.402201","lat":"23.155061","name":"","tel":"","address":"广东省广州市天河区新塘大街28"},{"id":"eab2ffacffffffc976ce7286d4054823","time":"2018-12-25 16:31:29.0","lng":"113.402201","lat":"23.155061","name":"","tel":"","address":"广东省广州市天河区新塘大街28"},{"id":"eab2ffacffffffc976ce7286d4054823","time":"2018-12-25 16:31:34.0","lng":"113.402207","lat":"23.155052","name":"","tel":"","address":"广东省广州市天河区新塘大街28"},{"id":"eab2ffacffffffc976ce7286d4054823","time":"2018-12-25 16:31:39.0","lng":"113.402178","lat":"23.155007","name":"","tel":"","address":"广东省广州市天河区新塘大街28"},{"id":"eab2ffacffffffc976ce7286d4054823","time":"2018-12-25 16:31:44.0","lng":"113.402154","lat":"23.154975","name":"","tel":"","address":"广东省广州市天河区新塘大街28"},{"id":"eab2ffacffffffc976ce7286d4054823","time":"2018-12-25 16:31:49.0","lng":"113.402145","lat":"23.154963","name":"","tel":"","address":"广东省广州市天河区新塘大街28"},{"id":"eab2ffacffffffc976ce7286d4054823","time":"2018-12-25 16:31:54.0","lng":"113.402145","lat":"23.154963","name":"","tel":"","address":"广东省广州市天河区新塘大街28"},{"id":"eab2ffacffffffc976ce7286d4054823","time":"2018-12-25 16:31:59.0","lng":"113.402145","lat":"23.154963","name":"","tel":"","address":"广东省广州市天河区新塘大街28"},{"id":"eab2ffacffffffc976ce7286d4054823","time":"2018-12-25 16:32:04.0","lng":"113.402156","lat":"23.154972","name":"","tel":"","address":"广东省广州市天河区新塘大街28"},{"id":"eab2ffacffffffc976ce7286d4054823","time":"2018-12-25 16:32:09.0","lng":"113.402161","lat":"23.154972","name":"","tel":"","address":"广东省广州市天河区新塘大街28"},{"id":"eab2ffacffffffc976ce7286d4054823","time":"2018-12-25 16:32:14.0","lng":"113.402161","lat":"23.154972","name":"","tel":"","address":"广东省广州市天河区新塘大街28"},{"id":"eab2ffacffffffc976ce7286d4054823","time":"2018-12-25 16:32:19.0","lng":"113.402175","lat":"23.154991","name":"","tel":"","address":"广东省广州市天河区新塘大街28"},{"id":"eab2ffacffffffc976ce7286d4054823","time":"2018-12-25 16:32:24.0","lng":"113.402141","lat":"23.154990","name":"","tel":"","address":"广东省广州市天河区新塘大街28"}]
     */

    private boolean success;
    private String msgTitle;
    private List<ExecutiveAtteanTrackMsgBean> msg;

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

    public List<ExecutiveAtteanTrackMsgBean> getMsg() {
        return msg;
    }

    public void setMsg(List<ExecutiveAtteanTrackMsgBean> msg) {
        this.msg = msg;
    }
}
