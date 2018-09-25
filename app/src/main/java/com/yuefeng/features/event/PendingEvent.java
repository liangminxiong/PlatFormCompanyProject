package com.yuefeng.features.event;

import com.common.event.BaseEvent;

public class PendingEvent extends BaseEvent {

    public PendingEvent(int what) {
        super(what);
    }

    public PendingEvent(int what, Object data) {
        super(what, data);
    }

    public PendingEvent() {
    }
}