package com.yuefeng.contacts.modle.contacts;

import java.io.Serializable;

/*通讯录机构*/
public class OrganPersonalBean implements Serializable {


    /**
     * success : true
     * code : 200
     * msg : 成功
     * text : 说明：organlist为机构和人员数组，isorgan判断是否机构，haschild判断下级是否有机构或人员
     * data : {"id":"dg1168","pid":"","name":"","icon":"","isorgan":true,"haschild":true,"organlist":[{"id":"370036d4ffffffc9651b3c5bdee78715","pid":"dg1168","name":"河北省","icon":"","isorgan":true,"haschild":true,"organlist":[]},{"id":"3717aa7effffffc9651b3c5bd8e582ed","pid":"dg1168","name":"侨银河南省","icon":"","isorgan":true,"haschild":true,"organlist":[]},{"id":"3718ffccffffffc9651b3c5b7a459012","pid":"dg1168","name":"侨银四川省","icon":"","isorgan":true,"haschild":true,"organlist":[]},{"id":"dg1322","pid":"dg1168","name":"梅州","icon":"","isorgan":true,"haschild":true,"organlist":[]},{"id":"dg1335","pid":"dg1168","name":"惠州","icon":"","isorgan":true,"haschild":true,"organlist":[]},{"id":"dg1344","pid":"dg1168","name":"南沙项目","icon":"","isorgan":true,"haschild":true,"organlist":[]},{"id":"dg1379","pid":"dg1168","name":"兴丰污水厂","icon":"","isorgan":true,"haschild":true,"organlist":[]},{"id":"dg1383","pid":"dg1168","name":"公司本部","icon":"","isorgan":true,"haschild":true,"organlist":[]},{"id":"dg1406","pid":"dg1168","name":"湛江国标机","icon":"","isorgan":true,"haschild":true,"organlist":[]},{"id":"dg1411","pid":"dg1168","name":"项目经理","icon":"","isorgan":true,"haschild":true,"organlist":[]},{"id":"dg1468","pid":"dg1168","name":"新光快速","icon":"","isorgan":true,"haschild":false,"organlist":[]},{"id":"dg1493","pid":"dg1168","name":"汕头项目","icon":"","isorgan":true,"haschild":true,"organlist":[]},{"id":"dg1495","pid":"dg1168","name":"增城区","icon":"","isorgan":true,"haschild":true,"organlist":[]},{"id":"dg1510","pid":"dg1168","name":"清远项目","icon":"","isorgan":true,"haschild":true,"organlist":[]},{"id":"dg1549","pid":"dg1168","name":"侨银安徽省","icon":"","isorgan":true,"haschild":true,"organlist":[]},{"id":"dg1561","pid":"dg1168","name":"河源项目","icon":"","isorgan":true,"haschild":true,"organlist":[]},{"id":"dg1562","pid":"dg1168","name":"中山西区保洁","icon":"","isorgan":true,"haschild":true,"organlist":[]},{"id":"dg1590","pid":"dg1168","name":"大石保洁","icon":"","isorgan":true,"haschild":true,"organlist":[]},{"id":"dg1646","pid":"dg1168","name":"阳东项目","icon":"","isorgan":true,"haschild":true,"organlist":[]},{"id":"dg1727","pid":"dg1168","name":"青海德令哈项目","icon":"","isorgan":true,"haschild":true,"organlist":[]},{"id":"dg1732","pid":"dg1168","name":"白云河涌一标","icon":"","isorgan":true,"haschild":true,"organlist":[]},{"id":"dg1738","pid":"dg1168","name":"钟村项目","icon":"","isorgan":true,"haschild":false,"organlist":[]},{"id":"dg1751","pid":"dg1168","name":"肇庆市端州区项目","icon":"","isorgan":true,"haschild":true,"organlist":[]},{"id":"dg1756","pid":"dg1168","name":"侨银浙江省","icon":"","isorgan":true,"haschild":true,"organlist":[]},{"id":"dg1757","pid":"dg1168","name":"侨银贵州","icon":"","isorgan":true,"haschild":true,"organlist":[]},{"id":"dg1786","pid":"dg1168","name":"珠海金湾","icon":"","isorgan":true,"haschild":true,"organlist":[]},{"id":"dg1797","pid":"dg1168","name":"高明项目","icon":"","isorgan":true,"haschild":false,"organlist":[]},{"id":"dg1799","pid":"dg1168","name":"美庭项目","icon":"","isorgan":true,"haschild":true,"organlist":[]},{"id":"dg1800","pid":"dg1168","name":"云南项目","icon":"","isorgan":true,"haschild":true,"organlist":[]},{"id":"dg1806","pid":"dg1168","name":"茂名高州项目","icon":"","isorgan":true,"haschild":true,"organlist":[]},{"id":"dg1807","pid":"dg1168","name":"花都新雅街项目","icon":"","isorgan":true,"haschild":true,"organlist":[]},{"id":"dg1808","pid":"dg1168","name":"水域维修","icon":"","isorgan":true,"haschild":true,"organlist":[]},{"id":"dg1829","pid":"dg1168","name":"水域六标","icon":"","isorgan":true,"haschild":true,"organlist":[]},{"id":"dg1831","pid":"dg1168","name":"从化区","icon":"","isorgan":true,"haschild":true,"organlist":[]},{"id":"dg1832","pid":"dg1168","name":"佛山市各个镇区","icon":"","isorgan":true,"haschild":true,"organlist":[]},{"id":"dg1833","pid":"dg1168","name":"茂名市","icon":"","isorgan":true,"haschild":true,"organlist":[]},{"id":"dg1836","pid":"dg1168","name":"湖南张家界","icon":"","isorgan":true,"haschild":true,"organlist":[]},{"id":"dg1884","pid":"dg1168","name":"白云新城区保洁","icon":"","isorgan":true,"haschild":false,"organlist":[]},{"id":"dg1892","pid":"dg1168","name":"白云城区内环路","icon":"","isorgan":true,"haschild":true,"organlist":[]},{"id":"dg1937","pid":"dg1168","name":"空港项目","icon":"","isorgan":true,"haschild":false,"organlist":[]},{"id":"dg1944","pid":"dg1168","name":"侨银智慧环卫系统","icon":"","isorgan":true,"haschild":true,"organlist":[]},{"id":"dg1947","pid":"dg1168","name":"江西省","icon":"","isorgan":true,"haschild":true,"organlist":[]},{"id":"f27c4017ffffffc91907c73395285eb7","pid":"dg1168","name":"待分配车辆组","icon":"","isorgan":true,"haschild":false,"organlist":[]},{"id":"f8a8f040ffffffc9561c245a06f28e85","pid":"dg1168","name":"村居市政环卫保洁、绿化管养服务资格项目","icon":"","isorgan":true,"haschild":true,"organlist":[]},{"id":"f8a9f8e2ffffffc9561c245a54e60c5f","pid":"dg1168","name":"广州市番禺区人民政府大石街道办事处道路保洁服务采购项","icon":"","isorgan":true,"haschild":false,"organlist":[]},{"id":"10f3ff8fffffffc924aa07ff25b946a1","pid":"dg1168","name":"侨银环保","icon":"","isorgan":false,"haschild":false,"organlist":[]},{"id":"2a5a6f40ffa80103701e7165535ad91d","pid":"dg1168","name":"侨银环保公司","icon":"","isorgan":false,"haschild":false,"organlist":[]},{"id":"7ab6ce29ffffffc9051247ee424738d5","pid":"dg1168","name":"goshgen","icon":"","isorgan":false,"haschild":false,"organlist":[]},{"id":"7ab6f0a5ffffffc9051247eedd72b399","pid":"dg1168","name":"goshgen","icon":"","isorgan":false,"haschild":false,"organlist":[]},{"id":"7abacad4ffffffc9051247eee5c27905","pid":"dg1168","name":"郑成福","icon":"","isorgan":false,"haschild":false,"organlist":[]},{"id":"86c0a590ffffffc915e504697b3abcd9","pid":"dg1168","name":"中山西区人员","icon":"","isorgan":false,"haschild":false,"organlist":[]},{"id":"dg1203","pid":"dg1168","name":"侨银环保admin","icon":"","isorgan":false,"haschild":false,"organlist":[]},{"id":"dg1659","pid":"dg1168","name":"粤东区域项目","icon":"","isorgan":false,"haschild":false,"organlist":[]},{"id":"dg1660","pid":"dg1168","name":"广州区域项目","icon":"","isorgan":false,"haschild":false,"organlist":[]},{"id":"dg1661","pid":"dg1168","name":"粤西区域项目","icon":"","isorgan":false,"haschild":false,"organlist":[]},{"id":"dg1662","pid":"dg1168","name":"佛山区域项目","icon":"","isorgan":false,"haschild":false,"organlist":[]},{"id":"dg1663","pid":"dg1168","name":"省外区域外项目","icon":"","isorgan":false,"haschild":false,"organlist":[]},{"id":"dg1704","pid":"dg1168","name":"新光快速","icon":"","isorgan":false,"haschild":false,"organlist":[]},{"id":"dg1709","pid":"dg1168","name":"从化江埔良口项目","icon":"","isorgan":false,"haschild":false,"organlist":[]},{"id":"dg1716","pid":"dg1168","name":"谭永基","icon":"","isorgan":false,"haschild":false,"organlist":[]},{"id":"dg1717","pid":"dg1168","name":"李建国","icon":"","isorgan":false,"haschild":false,"organlist":[]},{"id":"dg1718","pid":"dg1168","name":"陈丽芹","icon":"","isorgan":false,"haschild":false,"organlist":[]},{"id":"dg1719","pid":"dg1168","name":"王小宇","icon":"","isorgan":false,"haschild":false,"organlist":[]}]}
     */

    private boolean success;
    private int code;
    private String msg;
    private String text;
    private OrganPersonalDataBean data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public OrganPersonalDataBean getData() {
        return data;
    }

    public void setData(OrganPersonalDataBean data) {
        this.data = data;
    }

}
