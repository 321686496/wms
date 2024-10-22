package com.hongan.template.external.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author zyp
 * @version 1.0
 * @date 2020/9/8 18:22
 * @description 退款订单
 */
@Getter
@AllArgsConstructor
@ToString
public class RefundOrderVo {
    //商家订单号
    private final String orderNo;
    //退款订单号
    private final String outRefundNo;
    //原订单金额
    private final String amount;
    //退款金额
    private final String refundFee;
    //退款原因
    private final String reason;

    public static RefundOrderVo formRefundOrderVo(String orderNo, String outRefundNo, String amount, String refundFee, String reason) {
        return new RefundOrderVo(orderNo, outRefundNo, amount, refundFee, reason);
    }


}
