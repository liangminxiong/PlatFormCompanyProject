package com.yuefeng.features.event;

import com.common.event.BaseEvent;

public class AllPersoanlEvent extends BaseEvent {

    public AllPersoanlEvent(int what) {
        super(what);
    }

    public AllPersoanlEvent(int what, Object data) {
        super(what, data);
    }

    public AllPersoanlEvent() {
    }
}