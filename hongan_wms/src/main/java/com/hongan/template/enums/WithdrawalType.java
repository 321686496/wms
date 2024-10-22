package com.hongan.template.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum WithdrawalType {
    WECHAT("WECHAT", "微信"),
    ALIPAY("ALIPAY", "支付宝"),
    BANK("BANK", "银行卡"),
    ;
    @EnumValue
    String msg;
    String remark;

    WithdrawalType(String msg, String remark) {
        this.msg = msg;
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }
}
