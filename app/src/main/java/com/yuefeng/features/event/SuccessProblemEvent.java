package com.yuefeng.features.event;

import com.common.event.BaseEvent;

public class SuccessProblemEvent extends BaseEvent {

    public SuccessProblemEvent(int what) {
        super(what);
    }

    public SuccessProblemEvent(int what, Object data) {
        super(what, data);
    }

    public SuccessProblemEvent() {
    }
}