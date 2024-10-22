package com.hongan.template.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * @Author: zyp
 * @Date: 2022/2/17 0017
 */
public enum PromoteLotteryRecordStatus {
    NoWin("NoWin", "未中奖"),
    Win("Win", "已中奖"),
    ;
    @EnumValue
    String msg;
    String remark;

    PromoteLotteryRecordStatus(String msg, String remark) {
        this.msg = msg;
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }
}
