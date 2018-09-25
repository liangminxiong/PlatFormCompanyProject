package com.yuefeng.login_splash.event;


import com.common.event.BaseEvent;

/**
 * 签到
 */

public class SignInEvent extends BaseEvent {

    public SignInEvent(int what) {
        super(what);
    }

    public SignInEvent(int what, Object data) {
        super(what, data);
    }

    public SignInEvent() {
    }
}
