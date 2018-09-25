package com.yuefeng.features.event;

import com.common.event.BaseEvent;

public class JobMonitoringFragmentEvent extends BaseEvent {

    public JobMonitoringFragmentEvent(int what) {
        super(what);
    }

    public JobMonitoringFragmentEvent(int what, Object data) {
        super(what, data);
    }

    public JobMonitoringFragmentEvent() {
    }
}