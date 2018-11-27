package com.yuefeng.features.event;

import com.common.event.BaseEvent;

public class VechileListEvent extends BaseEvent {

    public VechileListEvent(int what) {
        super(what);
    }

    public VechileListEvent(int what, Object data) {
        super(what, data);
    }

    public VechileListEvent() {
    }
}