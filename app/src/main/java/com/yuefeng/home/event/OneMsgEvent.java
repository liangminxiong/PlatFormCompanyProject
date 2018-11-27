package com.yuefeng.home.event;

import com.common.event.BaseEvent;

/*消息详情*/
public class OneMsgEvent extends BaseEvent {

    public OneMsgEvent(int what) {
        super(what);
    }

    public OneMsgEvent(int what, Object data) {
        super(what, data);
    }

    public OneMsgEvent() {
    }
}