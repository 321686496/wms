package com.hongan.template.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum UserRegisterSourceType {
    PlatformCreate("PlatformCreate", "平台添加"),
    SelfRegister("SelfRegister", "自己注册"),
    SelfRegisterApplet("SelfRegisterApplet", "自己注册(小程序)"),
    ;
    @EnumValue
    String msg;
    String remark;

    UserRegisterSourceType(String msg, String remark) {
        this.msg = msg;
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }
}
