package com.yuefeng.usercenter.event;


import com.common.event.BaseEvent;

/**
 * Created  on 2018-05-29.
 * author:seven
 * email:seven2016s@163.com
 */

public class CollectEvent extends BaseEvent {
    public CollectEvent() {
    }

    public CollectEvent(int what) {
        super(what);
    }

    public CollectEvent(int what, Object data) {
        super(what, data);
    }
}
