package com.hongan.template.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum MessageLevel {
    System("Hold", "系统级"),
    User("User", "用户级"),
    ;
    @EnumValue
    String msg;
    String remark;

    MessageLevel(String msg, String remark) {
        this.msg = msg;
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }
}
