package com.yuefeng.home.modle;

import java.util.List;

public class TestBean {


    /**
     * reason : true
     * result : [{"ID":301651,"ClientID":0,"ClientName":"诺亚科技","ClientContact":"","ClientTel":null,"LineName":"","LineID":0,"StartPoint":"","EndPoint":"","TeamTravel":"8：00棠下公交站（好又多正门口）8：02棠东骏景花园八匹马前50米8：05车陂地铁口8：08东圃天河广场前8：10东圃客运站公交站8：20大沙地黄埔派出所门口－诺亚公司18：10分返（返程线路：东圃客运站\u2014东圃BRT站旁\u2014车陂地铁口\u2014棠东牌坊\u2014棠下牌坊）","Price":890,"HaveIncome":0,"FapiaoPay":0,"LuQiaoFei":"我方付","TingCheFei":"我方付","PayMode":"挂账","VehicleID":1035,"Vehiclename":"粤AP6869","Number":51,"Driver1_ID":1237,"Driver2_ID":0,"OperatorName":"洪俊彬","OrderTypeName":"内部订单","Reason":"班车接送","EstimateGoTime":"2018/12/13","EstimateBackTime":"2018/12/13","UseDays":1,"FreeTime":"","ExpectedMileage":0,"InvoiceType":0,"Rebates":0,"InServiceFee":0,"TravelFee":0,"StopsFee":0,"OilFee":0,"MealFee":0,"AccommodationFee":0,"otherFei":"0","Driver1_Salary":0,"Driver1_Commission":140,"IsDriver1_Salary":0,"Driver2_Salary":0,"Driver2_Commission":0,"IsDriver2_Salary":0,"StartMileage":0,"EndMileage":0},{"ID":301968,"ClientID":0,"ClientName":"肇庆外校","ClientContact":"洛溪百佳1号车","ClientTel":null,"LineName":"","LineID":0,"StartPoint":"","EndPoint":"","TeamTravel":"11：30分到四会肇庆外校接人－单送番禺区洛溪百佳超市后停车场，13:00分准时发车走","Price":1400,"HaveIncome":0,"FapiaoPay":0,"LuQiaoFei":"我方付","TingCheFei":"我方付","PayMode":"挂账","VehicleID":1035,"Vehiclename":"粤AP6869","Number":49,"Driver1_ID":1237,"Driver2_ID":0,"OperatorName":"李伟娇","OrderTypeName":"内部订单","Reason":"二次用车","EstimateGoTime":"2018/12/14","EstimateBackTime":"2018/12/14","UseDays":1,"FreeTime":"","ExpectedMileage":0,"InvoiceType":0,"Rebates":0,"InServiceFee":0,"TravelFee":0,"StopsFee":0,"OilFee":0,"MealFee":0,"AccommodationFee":0,"otherFei":"0","Driver1_Salary":0,"Driver1_Commission":0,"IsDriver1_Salary":0,"Driver2_Salary":0,"Driver2_Commission":0,"IsDriver2_Salary":0,"StartMileage":0,"EndMileage":0},{"ID":301969,"ClientID":0,"ClientName":"诺亚科技","ClientContact":"温小姐13418135512","ClientTel":null,"LineName":"","LineID":0,"StartPoint":"","EndPoint":"","TeamTravel":"8：00棠下公交站（好又多正门口）8：02棠东骏景花园八匹马前50米8：05车陂地铁口8：08东圃天河广场前8：10东圃客运站公交站8：20大沙地黄埔派出所门口－诺亚公司18：10分返（返程线路：东圃客运站\u2014东圃BRT站旁\u2014车陂地铁口\u2014棠东牌坊\u2014棠下牌坊）","Price":890,"HaveIncome":0,"FapiaoPay":0,"LuQiaoFei":"我方付","TingCheFei":"我方付","PayMode":"挂账","VehicleID":1035,"Vehiclename":"粤AP6869","Number":51,"Driver1_ID":1237,"Driver2_ID":0,"OperatorName":"洪俊彬","OrderTypeName":"内部订单","Reason":"班车接送","EstimateGoTime":"2018/12/14","EstimateBackTime":"2018/12/14","UseDays":1,"FreeTime":"","ExpectedMileage":110,"InvoiceType":0,"Rebates":0,"InServiceFee":0,"TravelFee":0,"StopsFee":0,"OilFee":0,"MealFee":0,"AccommodationFee":0,"otherFei":"0","Driver1_Salary":0,"Driver1_Commission":130,"IsDriver1_Salary":0,"Driver2_Salary":0,"Driver2_Commission":0,"IsDriver2_Salary":0,"StartMileage":0,"EndMileage":0},{"ID":302036,"ClientID":0,"ClientName":"诺亚科技","ClientContact":"林生13527687875","ClientTel":null,"LineName":"","LineID":0,"StartPoint":"","EndPoint":"","TeamTravel":"8：00小北地铁A口8：10火车东站地铁J口（干休所8：20棠下公交站好又多正门口8：20骏景花园八匹马雕像前50米8：25车陂地铁口8：30东圃天河广场（国美电器）8:32东圃客运站公交站－诺亚公司18：00分返（返程线路：东圃客运站\u2014东圃BRT站旁\u2014车陂地铁口\u2014棠东牌坊（骏景花园对面）\u2014棠下牌坊\u2014嘉鸿华美达酒店前500米\u2014小北地铁口）","Price":890,"HaveIncome":0,"FapiaoPay":0,"LuQiaoFei":"我方付","TingCheFei":"我方付","PayMode":"挂账","VehicleID":1035,"Vehiclename":"粤AP6869","Number":53,"Driver1_ID":1237,"Driver2_ID":0,"OperatorName":"洪俊彬","OrderTypeName":"内部订单","Reason":"班车接送","EstimateGoTime":"2018/12/15","EstimateBackTime":"2018/12/15","UseDays":1,"FreeTime":"","ExpectedMileage":0,"InvoiceType":0,"Rebates":0,"InServiceFee":0,"TravelFee":0,"StopsFee":0,"OilFee":0,"MealFee":0,"AccommodationFee":0,"otherFei":"0","Driver1_Salary":0,"Driver1_Commission":140,"IsDriver1_Salary":0,"Driver2_Salary":0,"Driver2_Commission":0,"IsDriver2_Salary":0,"StartMileage":0,"EndMileage":0}]
     * error :
     */

    private boolean reason;
    private String error;
    private List<ResultBean> result;

    public boolean isReason() {
        return reason;
    }

    public void setReason(boolean reason) {
        this.reason = reason;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
    }
}
