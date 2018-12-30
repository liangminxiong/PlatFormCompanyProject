package com.yuefeng.login_splash.contract;


import com.common.base.codereview.BaseView;

/**
 * Created  on 2018-05-26.
 * author:seven
 * email:seven2016s@163.com
 */

public interface SignInContract {
    interface View extends BaseView<Object> {

    }

    interface Presenter {
        void signIn(String function, String userid, String terflag, String useridflag,
                    String lon, String lat, String address, String type);

        void getAnnouncementByuserid(String function, String pid, String timestart, String timeend);


        //创建
        void groupCreate(String userids, String createuserid, String groupName);

        //创建
        void groupDismiss(String userid, String groupid);

        /*加入群组*/
        void groupJoin(String userids, String groupid);

        /*获取机构，用户*/
        void findOrganWithID(String id, String name, Integer type);

        /*实时上传经纬度*/
        void uploadLnglat(String function, String type, String lng, String lat, String id, String phone, String address,String isupdate);
        void groupQueryWithUser(String userid);

        //获取token

        void getToken(String userid, String username, String usericon);

        /*获取排班计划时间*/
        void getWorkTime(String function, String userid);

        /*群组消息*/
        void groupQuery(String groupid);

        /*获取个人消息*/
        void findUserWithID(String id);
    }
}
