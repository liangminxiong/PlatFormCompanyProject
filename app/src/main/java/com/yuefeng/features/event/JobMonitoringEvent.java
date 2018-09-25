package com.yuefeng.features.event;

import com.common.event.BaseEvent;

public class JobMonitoringEvent extends BaseEvent {

    public JobMonitoringEvent(int what) {
        super(what);
    }

    public JobMonitoringEvent(int what, Object data) {
        super(what, data);
    }

    public JobMonitoringEvent() {
    }
}