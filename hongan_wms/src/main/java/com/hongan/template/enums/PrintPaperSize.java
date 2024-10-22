package com.hongan.template.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum PrintPaperSize {
    Size4030("Size4030", "40mm*30mm"),//未结算
    Size6040("Size6040", "60mm*40mm"),//未结算
    Size6080("Size6080", "60mm*80mm"),//未结算
    Size3020("Size3020", "30mm*20mm"),//未结算
    ;

    @EnumValue
    private String msg;
    private String remark;

    PrintPaperSize(String msg, String remark) {
        this.msg = msg;
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }
}
