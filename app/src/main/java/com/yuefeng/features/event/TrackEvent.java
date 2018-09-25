package com.yuefeng.features.event;

import com.common.event.BaseEvent;

public class TrackEvent extends BaseEvent {

    public TrackEvent(int what) {
        super(what);
    }

    public TrackEvent(int what, Object data) {
        super(what, data);
    }

    public TrackEvent() {
    }
}