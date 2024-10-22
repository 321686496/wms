package com.hongan.template.external.service.impl;

import com.hongan.template.base.entity.BaseException;
import com.hongan.template.config.service.IHonganConfigService;
import com.hongan.template.external.service.IUnifiedPayService;
import com.hongan.template.external.service.IExternalWechatAppletPayService;
import com.hongan.template.external.service.IExternalWechatAppletProxyPayService;
import com.hongan.template.external.vo.RefundOrderVo;
import com.hongan.template.external.vo.UnifiedOrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Map;

@Service
public class UnifiedPayServiceImpl implements IUnifiedPayService {
    @Autowired
    private IExternalWechatAppletPayService wechatAppletPayService;
    @Autowired
    private IExternalWechatAppletProxyPayService wechatAppletProxyPayService;
    @Autowired
    private IHonganConfigService configService;
    @Value("${spring.profiles.active}")
    private String path;

    @Override
    public Object unifiedPay(UnifiedOrderVo vo) throws BaseException {
        Map<String, String> systemConfig = configService.getValues(new ArrayList<String>() {{
            add("external_wechat_pay");
            add("external_wechat_pay_type");
        }});
        if (!systemConfig.get("external_wechat_pay").equals("true")) return null;
        if (systemConfig.get("external_wechat_pay_type").equals("Merchant")) {
            return wechatAppletPayService.unifiedPay(vo);
        } else if (systemConfig.get("external_wechat_pay_type").equals("ServiceMerchant")) {
            return wechatAppletProxyPayService.unifiedPay(vo);
        } else {
            return null;
        }
    }

    @Override
    public Map<String, String> payNotify(HttpServletRequest request) throws BaseException {
        Map<String, String> systemConfig = configService.getValues(new ArrayList<String>() {{
            add("external_wechat_pay");
            add("external_wechat_pay_type");
        }});
        if (!systemConfig.get("external_wechat_pay").equals("true")) return null;
        if (systemConfig.get("external_wechat_pay_type").equals("Merchant")) {
            return wechatAppletPayService.payNotify(request);
        } else if (systemConfig.get("external_wechat_pay_type").equals("ServiceMerchant")) {
            return wechatAppletProxyPayService.payNotify(request);
        } else {
            return null;
        }
    }

    @Override
    public Boolean wechatRefund(RefundOrderVo vo) throws BaseException {
        if (path.startsWith("dev")) return true;
        Map<String, String> systemConfig = configService.getValues(new ArrayList<String>() {{
            add("external_wechat_pay");
            add("external_wechat_pay_type");
        }});
        if (!systemConfig.get("external_wechat_pay").equals("true")) return null;
        if (systemConfig.get("external_wechat_pay_type").equals("Merchant")) {
            return wechatAppletPayService.wechatRefund(vo);
        } else if (systemConfig.get("external_wechat_pay_type").equals("ServiceMerchant")) {
            return wechatAppletProxyPayService.wechatRefund(vo);
        } else {
            return false;
        }
    }
}
