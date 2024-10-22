package com.hongan.template.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum SystemGoodsType {
    Goods("Goods", "商品"),
    Service("ACCOUNT", "服务"),
    ;
    @EnumValue
    String msg;
    String remark;

    SystemGoodsType(String msg, String remark) {
        this.msg = msg;
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }

}
