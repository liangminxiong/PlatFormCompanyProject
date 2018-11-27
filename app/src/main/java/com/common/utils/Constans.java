package com.common.utils;


public class Constans {
    /*
     * 网络监听
     * */
    public static final int NET_WORK_AVAILABLE = 0;//有网络
    public static final int NET_WORK_DISABLED = 1;//没网络

    public static final String COUNT_ZERO = "0";
    public static final int TWO = 2;
    public static final int THREE = 3;
    public static final int FOUR = 4;
    public static final int COMMON = 5;//公共
    public static final int TEN = 10;//公共
    public static final int TWENTY = 20;
    /*
     * home模块 100~200
     * */
    public static final int HOMEDATA = 101;//获取首页数据成功
    public static final int HOMEBANNER = 102;//获取首页banner
    public static final int HOMEERROR = 100;//首页请求数据失败
    public static final int HOMEDASUCCESS = 103;//banner,homedata请求数据成功，取消loading
    public static final int BDLOCATION_TIME = 10000;//定位时间间隔
    public static final String COOKIE_PREF = "cookie_pref";//cookie保存
    public static final String HAVE_USER_DATAS = "have_user_datas";//数据
    /*
     * 登录注册用户相关10-20
     * */

    public static final int REGISTER = 10;//注册成功
    public static final int LOGIN = 11;//登录成功
    public static final int RELOGIN = 13;//重新登录
    public static final int USERERROR = 12;//用户相关错误
    public static final int UPDATAAPP_SUCCESS = 14;//获取更新信息成功
    public static final int BAIDU_ZOOM_FOUTTEEN = 14;
    public static final int BAIDU_ZOOM_EIGHTEEN = 18;
    public static final int BAIDU_ZOOM_TWENTY_ONE = 21;
    public static final String USERNAME = "user_name";//存储用户名
    public static final String USERNAME_N = "userName";//存储用户名
    public static final String USERPASSWORD = "user_pwd";//存储用户名密码
    public static final String ANDROID = "android";
    public static final String ORGID = "orgId";
    public static final String ID = "id";
    public static final String EMAIL = "Email";
    public static final String ISREG = "Isreg";
    public static final String TELNUM = "Telnum";
    public static final int COLLECTSIZE = 41;//获取收藏size
    public static final int COLLECTSIZEERROR = 42;//获取收藏size失败
    /*是否签到或者上报*/
    public static final String ISSINGIN = "isSngin";
    public static final String STARTTIME = "startTime";
    public static final String ENDTIME = "endTime";
    public static final String STARTADDRESS = "startAddress";
    public static final String ENDADDRESS = "endAddress";
    public static final String ADDRESS = "mAddress";

    /*可选图片数量*/
    public static final int IMAGES_SIX = 6;//
    /*
     * webview模块 20-40
     * */
    public static final int GOBACK = 20;//
    public static final int COLLECTERROR = 22;//

    /*字数 100 200 ...*/
    public static final int ONT_HUNDRED = 100;
    public static final int TWO_HUNDRED = 200;
    public static final int THREE_HUNDRED = 300;


    /*
     * 问题上报 历史上报 40-50
     * */
    public static final int UPLOADSUCESS = 40;//问题上报成功
    public static final int EQUIPMENTCOUNTSUCESS = 41;//获取报警数量成功
    public static final int MARQUEESUCESS = 42;//获取报警数量失败
    public static final int COMPANYINFOSSUCESS = 43;//获取公司信息成功
    public static final int WEBH5SUCESS = 44;//h5成功/*
    public static final int MARQUEEERROR = 45;//获取报警数量失败
    public static final int PICSTURESUCESS = 46;//图片成功
    public static final int UPLOAD_HISTORY_SUCESS = 47;//历史问题上报成功
    public static final int UPLOAD_HISTORY_ERROR = 48;


    /*
     * 问题处理 50-70
     * */
    public static final int COUNT_SUCESS = 50;//获取类型数量成功
    public static final int GETCOUNT_ERROR = 51;//获取类型数量失败
    public static final int COUNT_AGAIN_SUCESS = 52;//再次获取类型数量成功

    public static final int SUBMITERROR = 53;//获取全部问题类型失败
    public static final int ALL_SSUCESS = 54;//q全部类型

    public static final int DAI_SSUCESS = 57;//获取待处理类型成功
    public static final int DAI_ERROR = 58;//获取待处理类型失败

    public static final int CHULIZHONG_SSUCESS = 59;//获取处理中类型成功
    public static final int CHULIZHONG_ERROR = 60;//获取处理中类型失败

    public static final int TOCLOSED_SSUCESS = 61;//获取待关闭类型成功
    public static final int TOCLOSED_ERROR = 62;//待关闭类型失败

    public static final int CLAIM_SUCESS = 63;//认领成功
    public static final int CLAIM_ERROR = 64;//认领失败

    public static final int CARRY_SUCESS = 65;//完成成功
    public static final int CARRY_ERROR = 66;//完成失败

    public static final int CLOSED_SSUCESS = 55;//关闭问题成功
    //
    public static final int ZHUANFA_SSUCESS = 56;//转发问题成功


    public static final int ETIDEXT_SUCESS = 69;//100数字倒计时

    public static final int DETAIL_SSUCESS = 70;//问题详情成功


    /*
     * 获取主管 轨迹 70-80
     * */
    public static final int GETPERSONAL_SSUCESS = 70;//获取主管成功
    public static final int GETPERSONAL_ERROR = 71;//获取主管失败
    public static final int JOB_SSUCESS = 72;//定位信息成功
    public static final int JOB_P_SSUCESS = 721;//人员成功
    public static final int JOB_V_SSUCESS = 722;//车辆成功
    public static final int JOB_Q_SSUCESS = 723;//问题成功
    public static final int JOB_ERROR = 73;//定位信息失败

    public static final int PERSONAL_SSUCESS = 74;//点击人员
    public static final int VEHICLE_SSUCESS = 75;//点击车辆
    public static final int PROBLEM_SSUCESS = 76;//点击问题
    public static final int PERSONAL_SSUCESS_LIST = 741;//点击人员
    public static final int VEHICLE_SSUCESS_LIST = 751;//点击车辆
    public static final int PROBLEM_SSUCESS_LIST = 761;//点击问题
    public static final int ALL_SSUCESS_LIST = 763;//全部

    public static final int TRACK_SSUCESS = 77;//
    public static final int TRACK_ERROR = 78;//
    public static final int ADDRESS_SSUCESS = 79;//地址

    /*
     * 视频监控  车辆列表 人员列表 车辆违规 人员违规 80-90
     * */
    public static final int VIDEO_SSUCESS = 80;//轨迹成功
    public static final int VIDEO_ERROR = 81;//轨迹失败
    public static final int CARLIST_SSUCESS = 82;//车辆列表成功
    public static final int CARLIST_SSUCESS_NEW = 821;//车辆列表成功
    public static final int CARLIST_ERROR = 83;//车辆列表失败

    public static final int CARLLEGAL_SSUCESS = 84;//车辆违规成功
    public static final int PERSONALLLEGAL_SSUCESS = 841;//车辆，人员违规成功
    public static final int PERSONALLLEGAL_ERROR = 851;//人员违规失败
    public static final int CARLLEGAL_ERROR = 85;//车辆违规失败
    public static final int PERSONAL_ID = 86;//人员id
    public static final int VECHIL_ID = 87;//车辆ID
    public static final int PERSONALLIST_SSUCESS = 88;//人员列表成功
    public static final int PERSONALLIST_ERROR = 89;//人员列表失败

    /*
     * 作业考勤 信息采集 90-100
     * */
    public static final int SNGNIN_SSUCESS = 90;//考勤成功
    public static final int SNGNIN_ERROR = 91;//考勤失败
    public static final int MSGCOLECTION_SSUCESS = 92;//上传信息采集成功
    public static final int MSGCOLECTION_ERROR = 93;//失败

    public static final int GETCAIJI_SSUCESS = 94;//获取采集类型成功
    public static final int GETCAIJI_ERROR = 95;//

    public static final int GETCAIJIHISTORY_SSUCESS = 96;//获取历史采集成功
    public static final int GETCAIJIHISTORY_ERROR = 97;//失败

    /*作业监察  100 - 110
     * */
    public static final int MONITORINGSIGNIN_SSUCESS = 100;//监察签到成功
    public static final int MONITORINGSIGNIN_ERROR = 101;//失败

    /*修改密码 110 */
    public static final int CHANGEPWD_SSUCESS = 110;//修改密码成功
    public static final int CHANGEPWD_ERROR = 111;//失败


    /*导航*/
    public static final String APP_FOLDER_NAME = "BNSDKSimpleDemo";
    public static final String ROUTE_PLAN_NODE = "routePlanNode";
    public static final int NOTIFICATION_BASE_NUMBER = 110;
    //    public static final String BAIDU_APPID = "11959239";
    public static final String BAIDU_APPID = "14268406";


    public static final String TYPE_ZERO = "0";//个人代签到/车辆违规查询
    public static final String TYPE_ONE = "1";//H5 主管代签到/人员违规查询
    public static final String TYPE_TWO = "2";//手机主管代签到

    /*消息  120 -130 */
    public static final int ONE_MSG_REPLY = 120;//回复刷新
    public static final int MSG_LIST_SSUCESS = 121;//消息列表成功
    public static final int MSG_LIST_ERROR = 122;
    public static final int MSG_DETAIL_SSUCESS = 123;//消息详情成功
    public static final int MSG_DETAIL_ERROR = 124;
    public static final int MSG_REPLY_SSUCESS = 125;//回复消息成功
    public static final int MSG_REPLY_ERROR = 126;
    public static final int ANMENT_LIST_SSUCESS = 127;//公告成功
    public static final int ANMENT_LIST_ERROR = 128;
    public static final int NEW_MSG_SUCCESS = 129;
    public static final int NEW_MSG_ERROR = 130;

}
