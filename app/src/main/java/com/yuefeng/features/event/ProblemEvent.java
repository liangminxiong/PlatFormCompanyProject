package com.yuefeng.features.event;

import com.common.event.BaseEvent;

public class ProblemEvent extends BaseEvent {

    public ProblemEvent(int what) {
        super(what);
    }

    public ProblemEvent(int what, Object data) {
        super(what, data);
    }

    public ProblemEvent() {
    }
}