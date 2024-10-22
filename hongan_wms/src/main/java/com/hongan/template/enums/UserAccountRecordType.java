package com.hongan.template.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * @Author: zyp
 * @Date: 2022/2/25 0025
 */
public enum UserAccountRecordType {
    AccountRecharge("AccountRecharge", "余额充值"),
    AccountWithdrawal("AccountWithdrawal", "余额提现"),
    LotteryPrize("LotteryPrize", "抽奖派奖"),
    OrderConsumption("OrderConsumption", "订单消费"),
    OrderRefund("OrderRefund", "订单退款"),
    OrderReward("OrderReward", "订单打赏"),
    ;
    @EnumValue
    String msg;
    String remark;

    UserAccountRecordType(String msg, String remark) {
        this.msg = msg;
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }
}
