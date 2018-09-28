package com.yuefeng.login_splash.model;

import java.io.Serializable;

public class AppUpdataBean implements Serializable {
    /**
     * VerName : V1.2.1
     * VerCode : 21
     * Description : �޸�����Ѳ��bug,�Ż��˲������ܡ�
     */
    /*更新信息*/

    /**
     * VerName : V1.2.1
     * VerCode : 21
     * Description : 修复了质量巡查bug,优化了部分性能。
     */

    private String VerName;
    private String VerCode;
    private String Description;

    public String getVerName() {
        return VerName;
    }

    public void setVerName(String VerName) {
        this.VerName = VerName;
    }

    public String getVerCode() {
        return VerCode;
    }

    public void setVerCode(String VerCode) {
        this.VerCode = VerCode;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

}
