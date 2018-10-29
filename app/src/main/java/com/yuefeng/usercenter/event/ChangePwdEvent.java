package com.yuefeng.usercenter.event;


import com.common.event.BaseEvent;

/**
 修改密码
 */

public class ChangePwdEvent extends BaseEvent {
    public ChangePwdEvent() {
    }

    public ChangePwdEvent(int what) {
        super(what);
    }

    public ChangePwdEvent(int what, Object data) {
        super(what, data);
    }
}
