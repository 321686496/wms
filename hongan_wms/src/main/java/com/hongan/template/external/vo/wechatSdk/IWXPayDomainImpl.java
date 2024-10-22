package com.hongan.template.external.vo.wechatSdk;

/**
 * 域名管理，实现主备域名自动切换
 */
public class IWXPayDomainImpl implements IWXPayDomain {
    @Override
    public void report(String domain, long elapsedTimeMillis, Exception ex) {

    }

    @Override
    public DomainInfo getDomain(WXPayConfig config) {
        return new DomainInfo("api.mch.weixin.qq.com", true);
    }
}