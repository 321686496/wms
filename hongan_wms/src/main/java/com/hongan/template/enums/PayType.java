package com.hongan.template.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * Created by zyp on 2021/9/17 0017.
 */
public enum PayType {
    WECHAT("WECHAT", "微信支付"),//微信支付
    ACCOUNT("ACCOUNT", "余额支付"),//余额支付
    ALIPAY("ALIPAY", "支付宝支付"),//支付宝支付
    BANK("BANK", "银行卡"),//银行卡
    SYSTEM_SEND("SYSTEM_SEND", "系统赠送"),//系统赠送
    ;
    @EnumValue
    String msg;
    String remark;

    PayType(String msg, String remark) {
        this.msg = msg;
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }

}
