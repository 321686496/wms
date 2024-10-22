package com.hongan.template.enums;


import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * @Author: zyp
 * @Date: 2022/12/9 0009
 */
public enum VerifyStatus {
    Verify("Verify", "审核中"),
    Pass("Pass", "已通过"),
    Refuse("Refuse", "已拒绝"),
    Cancel("Cancel", "已取消"),
    ;
    @EnumValue
    String msg;
    String remark;

    VerifyStatus(String msg, String remark) {
        this.msg = msg;
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }
}
