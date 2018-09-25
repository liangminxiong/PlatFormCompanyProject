package com.common.event;


/**
 * Created  on 2018-05-26.
 * author:seven
 * email:seven2016s@163.com
 */

public class CommonEvent extends BaseEvent {

    public CommonEvent(int what) {
        super(what);
    }

    public CommonEvent(int what, Object data) {
        super(what, data);
    }

    public CommonEvent() {
    }
}
