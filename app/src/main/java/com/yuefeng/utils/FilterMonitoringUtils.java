package com.yuefeng.utils;

/*人员，车辆，问题工具类*/

import com.common.utils.AppUtils;
import com.common.utils.Constans;
import com.common.utils.PreferencesUtils;
import com.yuefeng.cartreeList.common.Node;
import com.yuefeng.features.modle.GetJobMonitotingMsgBean;
import com.yuefeng.features.modle.PersonalinfoListBean;
import com.yuefeng.features.modle.QuestionListBean;
import com.yuefeng.features.modle.VehicleinfoListBean;

import java.util.ArrayList;
import java.util.List;

public class FilterMonitoringUtils {

    public static String First = "first";
    public static String parentId = "parentId";
    public static String personalId = "personalId";
    public static String vehicleId = "vehicleId";
    public static String questionId = "questionId";

    public static List<Node> showFilterDatas(GetJobMonitotingMsgBean msgBean) {
        List<Node> mlistNode = new ArrayList<>();
        mlistNode.clear();
        String parentName = PreferencesUtils.getString(AppUtils.getContext(), Constans.USERNAME_N, "");

        int personalnum = msgBean.getPersonalnum();
        int vehiclenum = msgBean.getVehiclenum();
        int questionnum = msgBean.getQuestionnum();
        int sum = personalnum + vehiclenum + questionnum;


        mlistNode.add(new Node(parentId, First, parentName + "(" + sum + ")", parentId));

        mlistNode.add(new Node(personalId, parentId, "人员(" + personalnum + ")", personalId));
        mlistNode.add(new Node(vehicleId, parentId, "车辆(" + vehiclenum + ")", vehicleId));
        mlistNode.add(new Node(questionId, parentId, "问题(" + questionnum + ")", questionId));

//        List<QuestionListBean> commonPList = new ArrayList<>();
//        List<QuestionListBean> commonCList = new ArrayList<>();


        List<PersonalinfoListBean> personalinfoList = msgBean.getPersonalinfoList();
        for (PersonalinfoListBean bean : personalinfoList) {
            String id = bean.getId();
            mlistNode.add(new Node(id, personalId, "", bean));
        }
        List<VehicleinfoListBean> vehicleinfoList = msgBean.getVehicleinfoList();
        for (VehicleinfoListBean bean : vehicleinfoList) {
            String id = bean.getId();
            mlistNode.add(new Node(id, vehicleId, "", bean));
        }


        List<QuestionListBean> questionList = msgBean.getQuestionList();
        for (QuestionListBean bean : questionList) {
            String id = bean.getId();
            mlistNode.add(new Node(id, questionId, "", bean));
        }

        return mlistNode;
    }

    /*filter data*/
    public static List<Node> retureSelectData(List<Node> listData, String key) {
        List<Node> mListData = new ArrayList<>();
        mListData.clear();
        for (Node node : listData) {
            String pid = (String) node.getpId();
            if (pid.equals(personalId)) {
                PersonalinfoListBean bean = (PersonalinfoListBean) node.getBean();
                String name = bean.getName();
                String id = bean.getId();
                if (name.contains(key)) {
                    mListData.add(new Node(id, personalId, "", bean));
                }
            } else if (pid.equals(vehicleId)) {
                VehicleinfoListBean bean = (VehicleinfoListBean) node.getBean();
                String name = bean.getRegistrationNO();
                String id = bean.getId();
                if (name.contains(key)) {
                    mListData.add(new Node(id, vehicleId, "", bean));
                }

            } else if (pid.equals(questionId)) {
                QuestionListBean bean = (QuestionListBean) node.getBean();
                String name = bean.getProblem();
                String id = bean.getId();
                if (name.contains(key)) {
                    mListData.add(new Node(id, questionId, "", bean));
                }
            }
        }
        return mListData;
    }
}
