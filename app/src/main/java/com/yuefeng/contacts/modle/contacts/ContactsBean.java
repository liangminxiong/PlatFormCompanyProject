package com.yuefeng.contacts.modle.contacts;

import android.text.TextUtils;

import com.common.utils.Cn2Spell;

import java.util.List;

public class ContactsBean implements Comparable<ContactsBean> {
//     ,

    private String id;
    private String pid;
    private String name; // 姓名
    private String pinyin; // 姓名对应的拼音
    private String firstLetter; // 拼音的首字母
    private String icon;
    private boolean isorgan;
    private boolean haschild;
    private boolean checked=false;

    private List<ContactsBean> organlist;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public boolean isHaschild() {
        return haschild;
    }

    public void setHaschild(boolean haschild) {
        this.haschild = haschild;
    }


    public ContactsBean() {
    }


    public ContactsBean(String name,boolean checked) {
        this.name = name;
        pinyin = Cn2Spell.getPinYin(name); // 根据姓名获取拼音
        firstLetter = pinyin.substring(0, 1).toUpperCase(); // 获取拼音首字母并转成大写
        if (!firstLetter.matches("[A-Z]")) { // 如果不在A-Z中则默认为“#”
            firstLetter = "#";
        }
        this.checked=checked;
    }
    public boolean isChecked() {
        return checked;
    }
    public void setChecked(boolean checked) {
        this.checked = checked;
    }


    public String getPinyin() {
        return pinyin;
    }

    public String getFirstLetter() {
        return firstLetter;
    }


    @Override
    public int compareTo(ContactsBean another) {
        if (firstLetter.equals("#") && !another.getFirstLetter().equals("#")) {
            return 1;
        } else if (!firstLetter.equals("#") && another.getFirstLetter().equals("#")) {
            return -1;
        } else {
        return pinyin.compareToIgnoreCase(another.getPinyin());
        }
    }


    public String getUserId() {
        if (TextUtils.isEmpty(this.id)) {
            throw new NullPointerException("userId  is null");
        } else {
            return this.id;
        }
    }

    public void setUserId(String userId) {
        this.id = userId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isIsorgan() {
        return isorgan;
    }

    public void setIsorgan(boolean isorgan) {
        this.isorgan = isorgan;
    }

    public List<ContactsBean> getOrganlist() {
        return organlist;
    }

    public void setOrganlist(List<ContactsBean> organlist) {
        this.organlist = organlist;
    }
}
