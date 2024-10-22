package com.hongan.template.external.service;

import com.hongan.template.base.entity.BaseException;
import com.hongan.template.external.vo.RefundOrderVo;
import com.hongan.template.external.vo.UnifiedOrderVo;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface IUnifiedPayService {
    /**
     * 调起支付
     */
    Object unifiedPay(UnifiedOrderVo vo) throws BaseException;

    /**
     * 微信支付回调接口
     */
    Map<String, String> payNotify(HttpServletRequest request) throws BaseException;

    /**
     * 退款
     */
    Boolean wechatRefund(RefundOrderVo vo) throws BaseException;
}
