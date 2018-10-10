package com.common.network;

import com.yuefeng.features.modle.EventQuestionBean;
import com.yuefeng.features.modle.GetAllPersonalBean;
import com.yuefeng.features.modle.GetEventdetailBean;
import com.yuefeng.features.modle.GetJobMonitotingBean;
import com.yuefeng.features.modle.GetQuestionCountBean;
import com.yuefeng.features.modle.SubmitBean;
import com.yuefeng.features.modle.WheelPathBean;
import com.yuefeng.features.modle.video.VideoEquipmentBean;
import com.yuefeng.login_splash.model.LoginBean;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created  on 2018-03-19.
 * author:seven
 * email:seven2016s@163.com
 */

public interface ApiService {


    String INTERFACEACTION = "Interface.action";
    String MIA = "zgbd_voc/MobileInterface/" + INTERFACEACTION;
    String VIDEO_IP = "120.78.217.251";
    String VIDEO = "zgbd_fireControl/h5/getvideoequipment.action";
    String MIA_HW = "zgbd_hw/MobileInterface2/" + INTERFACEACTION;

    //服务器apk path,这里放了云平台的apk 作为测试
    String APPNAME = "Environmental.apk";
    String apkPath = "http://www.vocsystem.cn/webfiles/android/" + APPNAME;
    String sersionPath = "http://www.vocsystem.cn/webfiles/android/Environmental.txt";

    String LOGIN = "Login";
    /*问题上传*/
    String UPLOADRUBBISHEVENT = "uploadRubbishEvent";
    /*质量巡查*/
    String GETEVENTQUESTION = "getEventquestion";
    /*问题处理过程*/
    String UPDATEQUESTIONS = "updatequestions";
    /*获取数量*/
    String GETQUESTIONCOUNT = "getQuestionCount";
    /*问题详情*/
    String GETEVENTDETAIL = "GetEventdetail";
    /*作业监控*/
    String GETMONITORINFO = "getmonitorinfo";
    /*获取所有主管*/
    String GETALLPERSON = "getallperson";
    /*签到*/
    String QIANDAO = "qiandao";

    /*车辆轨迹*/
    String getGpsDatasByTer = "getGpsDatasByTer";

    String H5URL_DAKA = NetworkUrl.ANDROID_TEST_SERVICE + "zgbd_voc/jsps/html5/zykq.html?";


    /*登录用户*/
    @FormUrlEncoded
    @POST(MIA)
    Observable<LoginBean> login(
            @Field("function") String function,
            @Field("loginname") String username,
            @Field("pwd") String password,
            @Field("client") String client);

    /*问题上报*/
    @POST(MIA_HW)
    Observable<SubmitBean> uploadRubbishEvent(
            @Query("function") String function,
            @Query("userid") String userid,
            @Query("pid") String pid,
            @Query("problem") String problem,//描述
            @Query("address") String address,
            @Query("lng") String lng,
            @Query("lat") String lat,
            @Query("type") String type,//性质
            @Query("imageArrays") String imageArrays);

    /*问题类型处理过程*/
    @POST(MIA_HW)
    Observable<EventQuestionBean> getEventquestion(
            @Query("function") String function,
            @Query("pid") String pid,
            @Query("userid") String userid,
            @Query("type") String type);

    /*问题认领*/
    @POST(MIA_HW)
    Observable<SubmitBean> updatequestions(
            @Query("function") String function,
            @Query("userid") String userid,
            @Query("problemid") String problemid,
            @Query("type") String type,
            @Query("imageArrays") String imageArrays,
            @Query("detail") String detail,
            @Query("pinjia") String pinjia,
            @Query("paifaid") String paifaid);

    /*问题数量*/
    @POST(MIA_HW)
    Observable<GetQuestionCountBean> getQuestionCount(
            @Query("function") String function,
            @Query("pid") String pid,
            @Query("userid") String userid);

    /*问题详情*/
    @POST(MIA_HW)
    Observable<GetEventdetailBean> GetEventdetail(
            @Query("function") String function,
            @Query("problemid") String problemid);

    /*作业监控*/
    @POST(MIA_HW)
    Observable<GetJobMonitotingBean> getmonitorinfo(
            @Query("function") String function,
            @Query("userid") String userid,
            @Query("pid") String pid,
            @Query("isreg") String isreg);

    /*获取主管*/
    @POST(MIA_HW)
    Observable<GetAllPersonalBean> getAllpersonal(
            @Query("function") String function,
            @Query("pid") String pid);

    /*签到*/
    @POST(MIA_HW)
    Observable<SubmitBean> signIn(
            @Query("function") String function,
            @Query("userid") String userid,
            @Query("terflag") String terflag,
            @Query("useridflag") String useridflag,
            @Query("lon") String lon,
            @Query("lat") String lat,
            @Query("address") String address,
            @Query("type") String type);

    /*轨迹*/
    @POST(MIA)
    Observable<WheelPathBean> getGpsDatasByTer(
            @Query("function") String function,
            @Query("terminal") String terminal,
            @Query("startTime") String startTime,
            @Query("endTime") String endTime);

    /*视频*/
    @POST(VIDEO)
    Observable<VideoEquipmentBean> getVideoList(
            @Query("pid") String terminal,
            @Query("type") String endTime);
}
