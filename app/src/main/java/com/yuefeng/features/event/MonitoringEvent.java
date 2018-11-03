package com.yuefeng.features.event;

import com.common.event.BaseEvent;

public class MonitoringEvent extends BaseEvent {

    public MonitoringEvent(int what) {
        super(what);
    }

    public MonitoringEvent(int what, Object data) {
        super(what, data);
    }

    public MonitoringEvent() {
    }
}