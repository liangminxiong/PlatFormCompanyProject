package com.yuefeng.features.event;

import com.common.event.BaseEvent;

public class PersonalListEvent extends BaseEvent {

    public PersonalListEvent(int what) {
        super(what);
    }

    public PersonalListEvent(int what, Object data) {
        super(what, data);
    }

    public PersonalListEvent() {
    }
}