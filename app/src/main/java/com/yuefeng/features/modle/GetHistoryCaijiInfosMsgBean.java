package com.yuefeng.features.modle;

import java.io.Serializable;

public class GetHistoryCaijiInfosMsgBean implements Serializable {
    /**
     * id : d7937fe5ffffee01000d4258bce6e0b9
     * name : 好的咕咚咕咚
     * lnglat : 113.402121,23.154958
     * imgeurl : http://10.0.0.68:8080/webfiles/zgbd_rubbish/map/image/2018110311173977794.png,
     * http://10.0.0.68:8080/webfiles/zgbd_rubbish/map/image/2018110311173954820.png,
     * http://10.0.0.68:8080/webfiles/zgbd_rubbish/map/image/2018110311173982362.png
     * typename : 垃圾点
     * time : 2018-11-03 11:17:39
     */

    private String id;
    private String name;
    private String lnglat;
    private String imgeurl;
    private String typename;
    private String time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLnglat() {
        return lnglat;
    }

    public void setLnglat(String lnglat) {
        this.lnglat = lnglat;
    }

    public String getImgeurl() {
        return imgeurl;
    }

    public void setImgeurl(String imgeurl) {
        this.imgeurl = imgeurl;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
