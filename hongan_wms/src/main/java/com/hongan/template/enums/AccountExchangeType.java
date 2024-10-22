package com.hongan.template.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * @Author: zyp
 * @Date: 2022/2/25 0025
 */
public enum AccountExchangeType {
    Income("Income", "收入"),
    Expenditure("Expenditure", "支出"),
    ;
    @EnumValue
    String msg;
    String remark;

    AccountExchangeType(String msg, String remark) {
        this.msg = msg;
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }
}
