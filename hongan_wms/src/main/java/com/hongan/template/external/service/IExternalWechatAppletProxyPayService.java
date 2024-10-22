package com.hongan.template.external.service;

import com.hongan.template.base.entity.BaseException;
import com.hongan.template.external.vo.RefundOrderVo;
import com.hongan.template.external.vo.UnifiedOrderVo;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 系统微信支付配置表 服务类
 * </p>
 *
 * @author Administrator
 * @since 2023-04-19
 */
public interface IExternalWechatAppletProxyPayService {
    /**
     * 微信支付预下单
     * <p>
     * orderNo 预支付订单号
     * amount  支付金额(分)
     * invokerIp 小程序端 IP
     * openid 小程序端 openId
     * body  商品描述
     * tradeType 下单类型 微信小程序 or 第三方APP
     * 微信统一预下单 ,微信小程序 和 第三方APP
     */
    Object unifiedPay(UnifiedOrderVo vo) throws BaseException;

    /**
     * 微信支付回调接口
     *
     * @param request
     * @return
     * @throws BaseException
     */
    Map<String, String> payNotify(HttpServletRequest request) throws BaseException;

    Boolean wechatRefund(RefundOrderVo vo) throws BaseException;
}
