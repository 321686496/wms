package com.hongan.template.external.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 返回给前端用于支付调用的信息(适用于小程序)
 */
@Data
public class WechatMiniAppVo {

  @JsonProperty("appId")
  public String appId;

  @JsonProperty("nonceStr")
  public String nonceStr;

  @JsonProperty("package")
  public String packageInfo; // prepay_id=wx2017033010242291fcfe0db70013231072

  @JsonProperty("signType")
  public String signType; // HMAC-SHA256

  @JsonProperty("timeStamp")
  public String timeStamp;

  @JsonProperty("paySign")
  public String paySign; // 签名

}
