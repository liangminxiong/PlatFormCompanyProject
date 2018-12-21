package com.yuefeng.contacts.modle.contacts;

import java.io.Serializable;
import java.util.List;


public class OrganPersonalRongDataBean implements Serializable {
    /**
     * id : dg1168
     * haschild : true
     * organlist : [{"id":"370036d4ffffffc9651b3c5bdee78715","pid":"dg1168","name":"河北省","haschild":true},{"id":"3717aa7effffffc9651b3c5bd8e582ed","pid":"dg1168","name":"侨银河南省","haschild":true},{"id":"3718ffccffffffc9651b3c5b7a459012","pid":"dg1168","name":"侨银四川省","haschild":true},{"id":"dg1322","pid":"dg1168","name":"梅州","haschild":true},{"id":"dg1335","pid":"dg1168","name":"惠州","haschild":true},{"id":"dg1344","pid":"dg1168","name":"南沙项目","haschild":true},{"id":"dg1379","pid":"dg1168","name":"兴丰污水厂","haschild":true},{"id":"dg1383","pid":"dg1168","name":"公司本部","haschild":true},{"id":"dg1406","pid":"dg1168","name":"湛江国标机","haschild":true},{"id":"dg1411","pid":"dg1168","name":"项目经理","haschild":false},{"id":"dg1468","pid":"dg1168","name":"新光快速","haschild":false},{"id":"dg1493","pid":"dg1168","name":"汕头项目","haschild":false},{"id":"dg1495","pid":"dg1168","name":"增城区","haschild":true},{"id":"dg1510","pid":"dg1168","name":"清远项目","haschild":false},{"id":"dg1549","pid":"dg1168","name":"侨银安徽省","haschild":true},{"id":"dg1561","pid":"dg1168","name":"河源项目","haschild":false},{"id":"dg1562","pid":"dg1168","name":"中山西区保洁","haschild":false},{"id":"dg1590","pid":"dg1168","name":"大石保洁","haschild":false},{"id":"dg1646","pid":"dg1168","name":"阳东项目","haschild":false},{"id":"dg1727","pid":"dg1168","name":"青海德令哈项目","haschild":false},{"id":"dg1732","pid":"dg1168","name":"白云河涌一标","haschild":false},{"id":"dg1738","pid":"dg1168","name":"钟村项目","haschild":false},{"id":"dg1751","pid":"dg1168","name":"肇庆市端州区项目","haschild":false},{"id":"dg1756","pid":"dg1168","name":"侨银浙江省","haschild":true},{"id":"dg1757","pid":"dg1168","name":"侨银贵州","haschild":true},{"id":"dg1786","pid":"dg1168","name":"珠海金湾","haschild":false},{"id":"dg1797","pid":"dg1168","name":"高明项目","haschild":false},{"id":"dg1799","pid":"dg1168","name":"美庭项目","haschild":true},{"id":"dg1800","pid":"dg1168","name":"云南项目","haschild":true},{"id":"dg1806","pid":"dg1168","name":"茂名高州项目","haschild":false},{"id":"dg1807","pid":"dg1168","name":"花都新雅街项目","haschild":false},{"id":"dg1808","pid":"dg1168","name":"水域维修","haschild":false},{"id":"dg1829","pid":"dg1168","name":"水域六标","haschild":false},{"id":"dg1831","pid":"dg1168","name":"从化区","haschild":true},{"id":"dg1832","pid":"dg1168","name":"佛山市各个镇区","haschild":true},{"id":"dg1833","pid":"dg1168","name":"茂名市","haschild":true},{"id":"dg1836","pid":"dg1168","name":"湖南张家界","haschild":false},{"id":"dg1884","pid":"dg1168","name":"白云新城区保洁","haschild":false},{"id":"dg1892","pid":"dg1168","name":"白云城区内环路","haschild":false},{"id":"dg1937","pid":"dg1168","name":"空港项目","haschild":false},{"id":"dg1944","pid":"dg1168","name":"侨银智慧环卫系统","haschild":false},{"id":"dg1947","pid":"dg1168","name":"江西省","haschild":true},{"id":"f27c4017ffffffc91907c73395285eb7","pid":"dg1168","name":"待分配车辆组","haschild":false},{"id":"f8a8f040ffffffc9561c245a06f28e85","pid":"dg1168","name":"村居市政环卫保洁、绿化管养服务资格项目","haschild":false},{"id":"f8a9f8e2ffffffc9561c245a54e60c5f","pid":"dg1168","name":"广州市番禺区人民政府大石街道办事处道路保洁服务采购项","haschild":false}]
     */

    private String id;
    private boolean haschild;
    private List<ContactsBean> organlist;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isHaschild() {
        return haschild;
    }

    public void setHaschild(boolean haschild) {
        this.haschild = haschild;
    }

    public List<ContactsBean> getOrganlist() {
        return organlist;
    }

    public void setOrganlist(List<ContactsBean> organlist) {
        this.organlist = organlist;
    }
}
