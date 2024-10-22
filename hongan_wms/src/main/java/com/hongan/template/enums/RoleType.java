package com.hongan.template.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * @Author: zyp
 * @Date: 2021/11/26 0026
 */
public enum RoleType {
    SUPER("SUPER", "超级管理员"),
    ADMIN("ADMIN", "管理员"),
    ;
    @EnumValue
    String msg;
    String remark;

    RoleType(String msg, String remark) {
        this.msg = msg;
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }
}
