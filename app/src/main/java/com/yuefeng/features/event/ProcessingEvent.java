package com.yuefeng.features.event;

import com.common.event.BaseEvent;

public class ProcessingEvent extends BaseEvent {

    public ProcessingEvent(int what) {
        super(what);
    }

    public ProcessingEvent(int what, Object data) {
        super(what, data);
    }

    public ProcessingEvent() {
    }
}