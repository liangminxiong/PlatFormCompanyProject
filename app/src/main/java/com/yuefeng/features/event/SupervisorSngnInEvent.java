package com.yuefeng.features.event;

import com.common.event.BaseEvent;

/*主管打卡*/
public class SupervisorSngnInEvent extends BaseEvent {

    public SupervisorSngnInEvent(int what) {
        super(what);
    }

    public SupervisorSngnInEvent(int what, Object data) {
        super(what, data);
    }

    public SupervisorSngnInEvent() {
    }
}