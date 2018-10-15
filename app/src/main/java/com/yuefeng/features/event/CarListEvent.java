package com.yuefeng.features.event;

import com.common.event.BaseEvent;

public class CarListEvent extends BaseEvent {

    public CarListEvent(int what) {
        super(what);
    }

    public CarListEvent(int what, Object data) {
        super(what, data);
    }

    public CarListEvent() {
    }
}