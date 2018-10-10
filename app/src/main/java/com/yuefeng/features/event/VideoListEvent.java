package com.yuefeng.features.event;

import com.common.event.BaseEvent;

public class VideoListEvent extends BaseEvent {

    public VideoListEvent(int what) {
        super(what);
    }

    public VideoListEvent(int what, Object data) {
        super(what, data);
    }

    public VideoListEvent() {
    }
}