package com.yuefeng.contacts.modle;

import java.io.Serializable;


/*通讯录人个人详情*/
public class UserDeatailInfosBean implements Serializable {


    /**
     * success : true
     * code : 200
     * msg : 成功
     * text : 说明：organ为企业，organname为所属机构，projectname为项目组，teamname为班组
     * data : {"id":"eac99227ffffffc976ce72867fb4d7f8","telNum":"13760895045","teamname":"八路一标","username":"小古","organ":"侨银环保科技有限公司","phone":"","projectname":"淮北项目","icon":"","organname":"环卫测试"}
     */

    private boolean success;
    private int code;
    private String msg;
    private String text;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : eac99227ffffffc976ce72867fb4d7f8
         * telNum : 13760895045
         * teamname : 八路一标
         * username : 小古
         * organ : 侨银环保科技有限公司
         * phone :
         * projectname : 淮北项目
         * icon :
         * organname : 环卫测试
         */

        private String id;
        private String telNum;
        private String teamname;
        private String username;
        private String organ;
        private String phone;
        private String projectname;
        private String icon;
        private String organname;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTelNum() {
            return telNum;
        }

        public void setTelNum(String telNum) {
            this.telNum = telNum;
        }

        public String getTeamname() {
            return teamname;
        }

        public void setTeamname(String teamname) {
            this.teamname = teamname;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getOrgan() {
            return organ;
        }

        public void setOrgan(String organ) {
            this.organ = organ;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getProjectname() {
            return projectname;
        }

        public void setProjectname(String projectname) {
            this.projectname = projectname;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getOrganname() {
            return organname;
        }

        public void setOrganname(String organname) {
            this.organname = organname;
        }
    }
}
