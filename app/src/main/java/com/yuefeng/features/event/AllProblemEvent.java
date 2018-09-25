package com.yuefeng.features.event;

import com.common.event.BaseEvent;

public class AllProblemEvent extends BaseEvent {

    public AllProblemEvent(int what) {
        super(what);
    }

    public AllProblemEvent(int what, Object data) {
        super(what, data);
    }

    public AllProblemEvent() {
    }
}