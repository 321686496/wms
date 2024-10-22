package com.hongan.template.external.constant;

/**
 * 交易类型 1-APP 0-JSAPI(小程序)
 */
public enum WeChatTradeType {
  JSAPI,
  APP,
  NATIVE,  // Native支付
  MWEB, // H5支付
  ;
}
