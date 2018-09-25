package com.yuefeng.features.event;

import com.common.event.BaseEvent;

public class ToClosedEvent extends BaseEvent {

    public ToClosedEvent(int what) {
        super(what);
    }

    public ToClosedEvent(int what, Object data) {
        super(what, data);
    }

    public ToClosedEvent() {
    }
}