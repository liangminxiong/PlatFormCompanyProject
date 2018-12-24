package com.common.network;

import com.yuefeng.contacts.modle.CheckOnlineBean;
import com.yuefeng.contacts.modle.TokenBean;
import com.yuefeng.contacts.modle.UpdateUserBean;
import com.yuefeng.contacts.modle.UserDeatailInfosBean;
import com.yuefeng.contacts.modle.contacts.OrganPersonalBean;
import com.yuefeng.contacts.modle.groupanduser.GroupQueryWithUserBean;
import com.yuefeng.contacts.modle.groupchat.AllUserContactsBean;
import com.yuefeng.contacts.modle.groupchat.GroupCreateBean;
import com.yuefeng.features.modle.AlarmListBean;
import com.yuefeng.features.modle.EventQuestionBean;
import com.yuefeng.features.modle.GetAllPersonalBean;
import com.yuefeng.features.modle.GetEventdetailBean;
import com.yuefeng.features.modle.GetHistoryCaijiInfosBean;
import com.yuefeng.features.modle.GetJobMonitotingBean;
import com.yuefeng.features.modle.GetKaoqinSumBean;
import com.yuefeng.features.modle.GetMonitoringHistoryBean;
import com.yuefeng.features.modle.GetMonitoringPlanCountBean;
import com.yuefeng.features.modle.GetQuestionCountBean;
import com.yuefeng.features.modle.HistorySngnInDataBean;
import com.yuefeng.features.modle.LllegalworkBean;
import com.yuefeng.features.modle.SubmitBean;
import com.yuefeng.features.modle.WheelPathBean;
import com.yuefeng.features.modle.carlist.CarListInfosBean;
import com.yuefeng.features.modle.carlist.OldCarListInfosBean;
import com.yuefeng.features.modle.video.GetCaijiTypeBean;
import com.yuefeng.features.modle.video.VideoEquipmentBean;
import com.yuefeng.home.modle.AnnouncementDataBean;
import com.yuefeng.home.modle.AnnouncementDeBean;
import com.yuefeng.home.modle.AppVersionDetailBean;
import com.yuefeng.home.modle.HistoryAppVersionBean;
import com.yuefeng.home.modle.MsgDataBean;
import com.yuefeng.home.modle.MsgDataDetailBean;
import com.yuefeng.home.modle.NewMsgDataBean;
import com.yuefeng.home.modle.NewRemindNorDetailBean;
import com.yuefeng.home.modle.ReplyContentBean;
import com.yuefeng.login_splash.model.LoginBean;
import com.yuefeng.personaltree.model.PersoanlTreeListBean;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
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
    String MIA_HW_bus = "zgbd_hw/business/review/";
    String MIA_HW_SYSTEM = "zgbd_hw/system/upgrade/";
    //小蔡本地
    String MIA_CHAT = "zgbd_voc/rongyun/";
    /*京东*/
    String MIA_HAO = "zgbd_voc_test/rongyun/";
    /*报警*/
    String MIA_ALARM = "zgbd_hw/alarm/";

    String MIA_HW_RELEASE = "zgbd_hw/release/";
    String MIA_HW_BUSINESS = "zgbd_hw/web/business/";

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
    /*车辆列表*/
    String LOADVEHICLELIST = "LoadVehicleList3";
    String GETVEHICLETREE = "getvehicletree";
    /*人员列表*/
    String GETPERSONTREE = "getpersontree";

    /*视频列表*/
    String GETVIDEOTREE = "getvideotree";

    /*车辆轨迹*/
    String getGpsDatasByTer = "getGpsDatasByTer";

    String H5URL_DAKA = NetworkUrl.ANDROID_TEST_SERVICE + "zgbd_voc/jsps/html5/zykq.html?";
    /*修改密码*/
    String UPDATEPASSWORD = "updatepassword";
    /*获取采集类型*/
    String GETCAIJITYPE = "getcaijitype";
    /*上传采集数据*/
    String UPLOADMAPINFO = "Uploadmapinfo";
    /*车辆，人员违规*/
    String GETWEIGUI = "getweigui";
    /*签到信息数量*/
    String GETKAOQINSUM = "getkaoqinsum";

    /*历史采集信息*/
    String GETMAPINFO = "getmapinfo";

    /*监察签到*/
    String UPLOADWORKSIGN = "Uploadworksign";
    /*签到历史*/
    String GETAPPWORKSIGN = "getAppworksign";
    /*监察上报*/
    String UPLOADJIANCHA = "Uploadjiancha";
    /*监察历史*/
    String GETWORKJIANCHA = "getworkjiancha";

    /*监察计划*/
    String GETJIANCHACOUNT = "getjianchacount";
    /*历史问题上报*/
    String GETEVEQUESTIONBYUSERID = "getEveQuestionByuserid";
    /*获取消息列表公告*/
    String GETDATAACTION = "getData.action";
    /*报警信息列表*/
    String GETALARMPAGE = "getalarmpage.action";
    /*报警详情*/
    String GETALARMEDIT = "getalarmedit.action";
    /*消息详情*/
    String GETMSGDETAIL = "getDetail.action";
    /*消息回复*/
    String DOREVIEW = "doReview.action";
    /*获取公告*/
    String GETAACTION = "getData.action";
    String GETDATA = "getData";
    /*公告详情*/
    String GETDETAILACTION = "getDetail.action";
    String GETDETAIL = "getDetail";
    /*最新消息*/
    String GETANNOUNCEMENTBYUSERID = "getAnnouncementByuserid";
    /*实时上传经纬度*/
    String UPLOADLNGLAT = "uploadlnglat";


    String H5URL_BUSINESSEDIT = NetworkUrl.ANDROID_TEST_SERVICE + MIA_HW_BUSINESS + "businessedit.html?userpid=";
    String H5URL_APPLYEDIT = NetworkUrl.ANDROID_TEST_SERVICE + MIA_HW_BUSINESS + "applyedit.html?pid=";
    String UPLOADFILE = NetworkUrl.ANDROID_TEST_SERVICE + "zgbd_hw/business/FileUpload.action";

    /*登录用户*/
    @Headers({"urlname:ali"})
    @FormUrlEncoded
    @POST(MIA)
    Observable<LoginBean> login(
            @Field("function") String function,
            @Field("loginname") String username,
            @Field("pwd") String password,
            @Field("client") String client);

    /*问题上报*/
    @Headers({"urlname:ali"})
    @FormUrlEncoded()
    @POST(MIA_HW)
    Observable<SubmitBean> uploadRubbishEvent(
            @Field("function") String function,
            @Field("userid") String userid,
            @Field("pid") String pid,
            @Field("problem") String problem,//描述
            @Field("address") String address,
            @Field("lng") String lng,
            @Field("lat") String lat,
            @Field("type") String type,//性质
            @Field("imageArrays") String imageArrays);

    /*问题类型处理过程*/
    @Headers({"urlname:ali"})
    @POST(MIA_HW)
    Observable<EventQuestionBean> getEventquestion(
            @Query("function") String function,
            @Query("pid") String pid,
            @Query("userid") String userid,
            @Query("type") String type);

    /*问题认领*/
    @Headers({"urlname:ali"})
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
    @Headers({"urlname:ali"})
    @POST(MIA_HW)
    Observable<GetQuestionCountBean> getQuestionCount(
            @Query("function") String function,
            @Query("pid") String pid,
            @Query("userid") String userid);

    /*问题详情*/
    @Headers({"urlname:ali"})
    @POST(MIA_HW)
    Observable<GetEventdetailBean> GetEventdetail(
            @Query("function") String function,
            @Query("problemid") String problemid);

    /*作业监控*/
    @Headers({"urlname:ali"})
    @POST(MIA_HW)
    Observable<GetJobMonitotingBean> getmonitorinfo(
            @Query("function") String function,
            @Query("userid") String userid,
            @Query("pid") String pid,
            @Query("isreg") String isreg);

    /*获取主管*/
    @Headers({"urlname:ali"})
    @POST(MIA_HW)
    Observable<GetAllPersonalBean> getAllpersonal(
            @Query("function") String function,
            @Query("pid") String pid);

    /*个人签到*/
    @Headers({"urlname:ali"})
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

    /*主管签到*/
    @Headers({"urlname:ali"})
    @POST(MIA_HW)
    Observable<SubmitBean> spSignIn(
            @Query("function") String function,
            @Query("userid") String userid,
            @Query("terflag") String terflag,
            @Query("useridflag") String useridflag,
            @Query("lon") String lon,
            @Query("lat") String lat,
            @Query("address") String address,
            @Query("type") String type,
            @Query("memo") String memo,
            @Query("imageArrays") String imageArrays);

    /*轨迹*/
    @Headers({"urlname:ali"})
    @POST(MIA)
    Observable<WheelPathBean> getGpsDatasByTer(
            @Query("function") String function,
            @Query("terminal") String terminal,
            @Query("startTime") String startTime,
            @Query("endTime") String endTime);

    /*视频*/
    @Headers({"urlname:ali"})
    @POST(VIDEO)
    Observable<VideoEquipmentBean> getVideoList(
            @Query("pid") String terminal,
            @Query("type") String endTime);

    /*车辆列表*/
    @Headers({"urlname:ali"})
    @POST(MIA)
    Observable<OldCarListInfosBean> getCarListInfos(
            @Query("function") String function,
            @Query("organid") String organid,
            @Query("userid") String userid,
            @Query("isreg") String isreg);

    /*车辆列表*/
    @Headers({"urlname:ali"})
    @POST(MIA)
    Observable<CarListInfosBean> getCarListInfosNew(
            @Query("function") String function,
            @Query("organid") String organid,
            @Query("userid") String userid,
            @Query("isreg") String isreg);

    /*视频列表*/
    @Headers({"urlname:ali"})
    @POST(MIA)
    Observable<VideoEquipmentBean> getVideoTree(
            @Query("function") String function,
            @Query("organid") String organid,
            @Query("userid") String userid,
            @Query("isreg") String isreg);

    /*问题类型处理过程*/
    @Headers({"urlname:ali"})
    @POST(MIA)
    Observable<SubmitBean> changePwd(
            @Query("function") String function,
            @Query("id") String id,
            @Query("oldpassword") String oldpassword,
            @Query("newpassword") String newpassword);

    /*签到*/
    @Headers({"urlname:ali"})
    @POST(MIA_HW)
    Observable<PersoanlTreeListBean> getPersontree(
            @Query("function") String function,
            @Query("userid") String userid,
            @Query("pid") String pid);

    /*签到总信息*/
    @Headers({"urlname:ali"})
    @POST(MIA_HW)
    Observable<GetKaoqinSumBean> getKaoqinSum(
            @Query("function") String function,
            @Query("userid") String userid,
            @Query("timestart") String timestart,
            @Query("timeend") String timeend);

    /*获取信息采集类型*/
    @Headers({"urlname:ali"})
    @POST(MIA_HW)
    Observable<GetCaijiTypeBean> getCaijiType(
            @Query("function") String function);

    /*历史采集信息*/
    @Headers({"urlname:ali"})
    @POST(MIA_HW)
    Observable<GetHistoryCaijiInfosBean> getHistoryCaijiInfo(
            @Query("function") String function,
            @Query("userid") String userid,
            @Query("timestart") String timestart,
            @Query("timeend") String timeend);

    /*上传采集数据*/
    @Headers({"urlname:ali"})
    @POST(MIA_HW)
    Observable<SubmitBean> upLoadmapInfo(
            @Query("function") String function,
            @Query("pid") String pid,
            @Query("userid") String userid,
            @Query("typeid") String typeid,
            @Query("typename") String typename,
            @Query("name") String name,
            @Query("lnglat") String lnglat,//经纬集合字符串
            @Query("area") String area,
            @Query("imageArrays") String imageArrays,
            @Query("id") String id);

    /*获取违规*/
    @Headers({"urlname:ali"})
    @POST(MIA_HW)
    Observable<LllegalworkBean> getWeigui(
            @Query("function") String function,
            @Query("pid") String pid,
            @Query("timestatr") String timestatr,
            @Query("timeend") String timeend,
            @Query("vid") String vid,
            @Query("type") String type);

    /*作业监察签到*/
    @Headers({"urlname:ali"})
    @POST(MIA_HW)
    Observable<SubmitBean> uploadWorkSign(
            @Query("function") String function,
            @Query("pid") String pid,
            @Query("userid") String userid,
            @Query("address") String address,
            @Query("lat") String lat,
            @Query("lon") String lon,
            @Query("personids") String personids,
            @Query("imageArrays") String imageArrays,
            @Query("memo") String memo);

    /*作业监察上报*/
    @Headers({"urlname:ali"})
    @POST(MIA_HW)
    Observable<SubmitBean> uploadJianCcha(
            @Query("function") String function,
            @Query("userid") String userid,
            @Query("pid") String pid,
            @Query("timestart") String timestart,
            @Query("timeend") String timeend,
            @Query("timesum") String timesum,
            @Query("id") String lnglat,
            @Query("startaddress") String startAddress,
            @Query("endaddress") String endAddress);

    /*作业监察历史*/
    @Headers({"urlname:ali"})
    @POST(MIA_HW)
    Observable<GetMonitoringHistoryBean> getWorkJianCha(
            @Query("function") String function,
            @Query("userid") String userid,
            @Query("timestart") String timestart,
            @Query("timeend") String timeend);

    /*作业监察历史*/
    @Headers({"urlname:ali"})
    @POST(MIA_HW)
    Observable<GetMonitoringPlanCountBean> getJianChaCount(
            @Query("function") String function,
            @Query("userid") String userid,
            @Query("timestart") String timestart,
            @Query("timeend") String timeend);

    /*历史问题上报*/
    @Headers({"urlname:ali"})
    @POST(MIA_HW)
    Observable<EventQuestionBean> getEveQuestionByuserid(
            @Query("function") String function,
            @Query("userid") String userid,
            @Query("timestart") String timestart,
            @Query("timeend") String timeend);

    /*消息列表*/
    @Headers({"urlname:ali"})
    @POST(MIA_HW_bus + GETDATAACTION)
    Observable<MsgDataBean> getAnMentDataList(
            @Query("pid") String userid,
            @Query("page") int timestart,
            @Query("limit") int timeend);

    /*消息列表（时间）*/
    @Headers({"urlname:ali"})
    @POST(MIA_HW_bus + GETDATAACTION)
    Observable<MsgDataBean> getAnMentDataList(
            @Query("function") String function,
            @Query("pid") String userid,
            @Query("timestart") String timestart,
            @Query("timeend") String timeend,
            @Query("page") int page,
            @Query("limit") int limit);

    /*消息详情*/
    @Headers({"urlname:ali"})
    @POST(MIA_HW_bus + GETMSGDETAIL)
    Observable<MsgDataDetailBean> getMsgDetail(
            @Query("reviewid") String reviewid);

    /*消息列表*/
    @Headers({"urlname:ali"})
    @POST(MIA_HW_bus + GETMSGDETAIL)
    Observable<MsgDataDetailBean> getMsgDetail(
            @Query("function") String function,
            @Query("reviewid") String reviewid);

    /*消息列表*/
    @Headers({"urlname:ali"})
    @FormUrlEncoded()
    @POST(MIA_HW_bus + DOREVIEW)
    Observable<ReplyContentBean> doReview(
            @Field("pid") String pid,
            @Field("reviewid") String reviewid,
            @Field("reviewpersonel") String reviewpersonel,
            @Field("reviewcontent") String reviewcontent,
            @Field("imageurls") String imageurls);

    /*最新消息列表*/
    @Headers({"urlname:qiu"})
    @FormUrlEncoded()
    @POST(MIA_HW)
    Observable<NewMsgDataBean> getAnnouncementByuserid(
            @Field("function") String function,
            @Field("pid") String pid,
            @Field("timestart") String timestart,
            @Field("timeend") String timeend);

    /*报警消息列表*/
    @Headers({"urlname:qiu"})
    @FormUrlEncoded()
    @POST(MIA_ALARM + GETALARMPAGE)
    Observable<AlarmListBean> getalarmpage(
            @Field("pid") String pid,
            @Field("datetimestart") String timestart,
            @Field("datetimeend") String timeend,
            @Field("page") String page,
            @Field("limit") String limit);

    /*报警消息详情*/
    @Headers({"urlname:qiu"})
    @FormUrlEncoded()
    @POST(MIA_ALARM + GETALARMEDIT)
    Observable<NewRemindNorDetailBean> getAlarmedit(
            @Field("id") String id,
            @Field("datetimestart") String timestart,
            @Field("datetimeend") String timeend);

    /*公告*/
    @Headers({"urlname:ali"})
    @POST(MIA_HW_RELEASE + GETAACTION)
    Observable<AnnouncementDataBean> getAnnouncementByuserid(
            @Query("function") String function,
            @Query("pid") String pid,
            @Query("timestart") String timestart,
            @Query("timeend") String timeend,
            @Query("page") int page,
            @Query("limit") int limit);

    /*包含最新消息列表*/
    @Headers({"urlname:ali"})
    @POST(MIA_HW_RELEASE + GETDETAILACTION)
    Observable<AnnouncementDeBean> getAnnouncementDetail(
            @Query("function") String function,
            @Query("id") String id);

    /*获取App历史版本*/
    @Headers({"urlname:ali"})
    @FormUrlEncoded()
    @POST(MIA_HW_SYSTEM + GETDATAACTION)
    Observable<HistoryAppVersionBean> getAppHistoryVersion(
            @Field("page") int page,
            @Field("limit") int limit,
            @Field("timestart") String timestart,
            @Field("timeend") String timeend);

    /*获取App历史版本详情*/
    @Headers({"urlname:ali"})
    @FormUrlEncoded()
    @POST(MIA_HW_SYSTEM + GETDETAILACTION)
    Observable<AppVersionDetailBean> getAppVersionDetail(
            @Field("id") String id);

    /*获取监察签到历史*/
    @Headers({"urlname:ali"})
    @FormUrlEncoded()
    @POST(MIA_HW)
    Observable<HistorySngnInDataBean> getAppWorkSign(
            @Query("function") String function,
            @Query("userid") String userid,
            @Field("timestart") String timestart,
            @Field("timeend") String timeend);

    /*实时上传经纬度*/
    @Headers({"urlname:ali"})
    @FormUrlEncoded()
    @POST(MIA_HW)
    Observable<SubmitBean> uploadLnglat(
            @Query("function") String function,
            @Query("type") String type,
            @Field("lng") String lng,
            @Field("lat") String lat,
            @Field("id") String id);

    /*获取token*/
    String GETTOKEN = "login.action";
    /*修改用户信息*/
    String UPDATEUSER = "updateUser.action";
    /*监察是否在线*/
    String CHECKUSERONLINE = "checkUserOnline.action";
    /*创建群组*/
    String GROUPCREATE = "groupCreate.action";
    /*加入群组*/
    String GROUPJOIN = "groupJoin.action";
    /*解散群组*/
    String GROUPDISMISS = "groupDismiss.action";

    /*通过用户id获取用户所有群组*/
    String GROUPQUERYWITHUSER = "groupQueryWithUser.action";

    /*通过id获取群组消息*/
    String GROUPQUERY = "groupQuery.action";


    /*获取token*/

    @Headers({"urlname:hao"})
    @FormUrlEncoded()
    @POST(MIA_HAO + GETTOKEN)
    Observable<TokenBean> getToken(
            @Field("userid") String userid,
            @Field("username") String username,
            @Field("usericon") String usericon);

    /*修改用户信息*/
    @Headers({"urlname:hao"})
    @FormUrlEncoded()
    @POST(MIA_HAO + UPDATEUSER)
    Observable<UpdateUserBean> updateUser(
            @Field("userid") String userid,
            @Field("username") String username,
            @Field("usericon") String usericon);

    /*通过用户id获取用户所有群组*/
    @Headers({"urlname:hao"})
    @FormUrlEncoded()
    @POST(MIA_HAO + GROUPQUERYWITHUSER)
    Observable<GroupQueryWithUserBean> groupQueryWithUser(
            @Field("userid") String userid);

    /*通过id获取群组消息*/
    @Headers({"urlname:hao"})
    @FormUrlEncoded()
    @POST(MIA_HAO + GROUPQUERY)
    Observable<GroupQueryWithUserBean> groupQuery(
            @Field("groupid") String groupid);

    /*监察是否在线*/
    @Headers({"urlname:hao"})
    @FormUrlEncoded()
    @POST(MIA_HAO + CHECKUSERONLINE)
    Observable<CheckOnlineBean> checkUserOnline(
            @Field("userid") String userid);

    /*创建群组*/
    @Headers({"urlname:hao"})
    @FormUrlEncoded()
    @POST(MIA_HAO + GROUPCREATE)
    Observable<GroupCreateBean> groupCreate(
            @Field("userids") String userids,
            @Field("createuserid") String createuserid,
            @Field("groupName") String groupName);

    /*加入群组*/
    @Headers({"urlname:hao"})
    @FormUrlEncoded()
    @POST(MIA_HAO + GROUPJOIN)
    Observable<GroupCreateBean> groupJoin(
            @Field("userids") String userids,
            @Field("groupid") String groupid);

    /*解散群组*/
    @Headers({"urlname:hao"})
    @FormUrlEncoded()
    @POST(MIA_HAO + GROUPDISMISS)
    Observable<GroupCreateBean> groupDismiss(
            @Field("userid") String userid,
            @Field("groupid") String groupid);


    //    通过机构id获取下级机构和用户
    String FINDORGANWITHID = "findOrganWithID.action";
    //获取所有融云注册用户
    String FINDALLUSER = "findAllUser.action";
    /*通讯录用户详情*/
    String FINDUSERWITHID = "findUserWithID.action";


    //    通过机构id获取下级机构和用户
    @Headers({"urlname:hao"})
    @FormUrlEncoded()
    @POST(MIA_HAO + FINDORGANWITHID)
    Observable<OrganPersonalBean> findOrganWithID(
            @Field("id") String id,
            @Field("name") String name,
            @Field("type") Integer type);

    //获取所有融云注册用户
    @Headers({"urlname:hao"})
    @FormUrlEncoded()
    @POST(MIA_HAO + FINDALLUSER)
    Observable<AllUserContactsBean> findAllUser(
            @Field("page") Integer page,//页码，从1开始，不传则取全部
            @Field("count") Integer count,//每页的条数，不传则取全部
            @Field("name") String name,//模糊搜索过滤名称
            @Field("type") Integer type,//0所有用户 1已注册融云用户 ,默认不传为0
            @Field("userid") String userid);//

    // 获取所有融云注册用户
    @Headers({"urlname:hao"})
    @FormUrlEncoded()
    @POST(MIA_HAO + FINDUSERWITHID)
    Observable<UserDeatailInfosBean> findUserWithID(
            @Field("id") String id);
}
