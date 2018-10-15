package com.yuefeng.features.event;

import com.common.event.BaseEvent;

public class LllegalWorkEvent extends BaseEvent {

    public LllegalWorkEvent(int what) {
        super(what);
    }

    public LllegalWorkEvent(int what, Object data) {
        super(what, data);
    }

    public LllegalWorkEvent() {
    }
}