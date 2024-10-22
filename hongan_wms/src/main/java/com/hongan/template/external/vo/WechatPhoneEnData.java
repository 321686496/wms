package com.hongan.template.external.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WechatPhoneEnData {
    private Long proxyId;
//    @NotNull
//    private String iv;
//    @NotNull
//    private String encryptedData;
    @NotNull
    private String code;
}
