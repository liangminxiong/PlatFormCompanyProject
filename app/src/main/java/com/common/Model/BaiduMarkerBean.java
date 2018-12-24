package com.common.Model;

public class BaiduMarkerBean {

    private String name;
    private String time;
    private String content;
    private String address;
    private String imageUrl;

    public BaiduMarkerBean(String name, String time, String content, String address, String imageUrl) {
        this.name = name;
        this.time = time;
        this.content = content;
        this.address = address;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
