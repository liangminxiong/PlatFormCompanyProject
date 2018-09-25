package com.yuefeng.features.event;

import com.common.event.BaseEvent;

public class EvaluationEvent extends BaseEvent {

    public EvaluationEvent(int what) {
        super(what);
    }

    public EvaluationEvent(int what, Object data) {
        super(what, data);
    }

    public EvaluationEvent() {
    }
}