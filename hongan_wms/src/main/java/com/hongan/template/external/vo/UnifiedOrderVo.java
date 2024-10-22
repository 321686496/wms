package com.hongan.template.external.vo;

import com.hongan.template.base.utils.ClientType;
import com.hongan.template.enums.PayType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 统一下单
 * orderNo 预支付订单号
 * amount  支付金额(分)
 * invokerIp 小程序端 IP
 * openid 小程序端 openId
 * body  商品描述
 * tradeType  交易付款类型
 * clientType 客户端类型
 */
@Getter
@AllArgsConstructor
@ToString
public class UnifiedOrderVo {
    private final String orderNo;//订单号
    private final String amount;//订单金额
    private final String invokerIp; // 微信H5支付必须上传用户真实ip地址
    private final String openid;//用户openId
    private final String body;//订单内容
    private final ClientType clientType;//客户端类型
    private final PayType payType;//支付类型
    private final String orderMark;//订单类型标识 ： 商品订单 goods  酒会订单 ticket


    public static UnifiedOrderVo formUnifiedOrderVo(
            String orderNo, String amount, String ipAddress, String body,
            String openId, ClientType clientType, PayType payType, String orderMark) {
        return new UnifiedOrderVo(orderNo, amount, ipAddress, openId, body, clientType, payType, orderMark);
    }




}
