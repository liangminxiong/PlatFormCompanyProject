package com.yuefeng.features.event;

import com.common.event.BaseEvent;

public class ProblemListEvent extends BaseEvent {

    public ProblemListEvent(int what) {
        super(what);
    }

    public ProblemListEvent(int what, Object data) {
        super(what, data);
    }

    public ProblemListEvent() {
    }
}