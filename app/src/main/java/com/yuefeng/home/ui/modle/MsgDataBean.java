package com.yuefeng.home.ui.modle;

import java.io.Serializable;

public class MsgDataBean implements Serializable {

    private int imageUrl;
    private String title;
    private String detail;
    private String time;

    @Override
    public String toString() {
        return "MsgDataBean{" +
                "imageUrl=" + imageUrl +
                ", title='" + title + '\'' +
                ", detail='" + detail + '\'' +
                ", time='" + time + '\'' +
                ", count='" + count + '\'' +
                '}';
    }

    private String count;

    public int getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(int imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
