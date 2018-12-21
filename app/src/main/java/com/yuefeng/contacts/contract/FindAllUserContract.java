package com.yuefeng.contacts.contract;

import com.common.base.codereview.BaseView;

/*获取所有用户*/
public interface FindAllUserContract {

    interface View extends BaseView<Object> {
    }

    interface Presenter {
        //                        @Field("page") Integer page,//页码，从1开始，不传则取全部
//                @Field("count") Integer count,//每页的条数，不传则取全部
//                @Field("name") String name,//模糊搜索过滤名称
//                @Field("type") );//0所有用户 1已注册融云用户 ,默认不传为0
        void findAllUser(Integer page, Integer count, String name, Integer type);

        //创建
        void groupCreate(String userids, String createuserid, String groupName);
    }
}
