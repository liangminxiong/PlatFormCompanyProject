package com.yuefeng.features.event;

import com.common.event.BaseEvent;

public class QualityDetailEvent extends BaseEvent {

    public QualityDetailEvent(int what) {
        super(what);
    }

    public QualityDetailEvent(int what, Object data) {
        super(what, data);
    }

    public QualityDetailEvent() {
    }
}