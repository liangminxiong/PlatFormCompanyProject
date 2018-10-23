package com.yuefeng.features.event;

import com.common.event.BaseEvent;

/*作业考勤*/
public class JobAttendanceEvent extends BaseEvent {

    public JobAttendanceEvent(int what) {
        super(what);
    }

    public JobAttendanceEvent(int what, Object data) {
        super(what, data);
    }

    public JobAttendanceEvent() {
    }
}