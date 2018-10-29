package com.yuefeng.features.event;

import com.common.event.BaseEvent;

public class PositionAcquisitionEvent extends BaseEvent {

    public PositionAcquisitionEvent(int what) {
        super(what);
    }

    public PositionAcquisitionEvent(int what, Object data) {
        super(what, data);
    }

    public PositionAcquisitionEvent() {
    }
}