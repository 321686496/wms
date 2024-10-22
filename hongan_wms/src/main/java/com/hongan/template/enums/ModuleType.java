package com.hongan.template.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * 模块类型标识
 */
public enum ModuleType {
    Basic("Basic", "基础版本"),//基础档案
    ;
    @EnumValue
    String msg;
    String remark;

    ModuleType(String msg, String remark) {
        this.msg = msg;
        this.remark = remark;
    }

    public String getMsg() {
        return msg;
    }

    public String getRemark() {
        return remark;
    }
}
