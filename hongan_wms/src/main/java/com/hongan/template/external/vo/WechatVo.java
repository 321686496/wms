package com.hongan.template.external.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * code 临时登录凭证
 * rawData 用户非敏感信息
 * signature 签名
 * encrypteData 用户敏感信息
 * iv 解密算法的向量
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WechatVo {
    //    @NotNull
//    private Integer loginTp;
//    private String invitationCode;
    //用户个人信息
    @NotNull
    private WechatUserInfo userInfo;

    //临时登录凭证Code
    @NotNull
    private String code;
    //邀请码
    private String invitationCode;

//    public boolean check(PonlayUser user) {
//        if (!userInfo.getAvatarUrl().equals(user.getAvatar()) ||
//                !userInfo.getNickName().equals(user.getNickname())) {
//            user.setAvatar(userInfo.getAvatarUrl());
//            user.setNickname(userInfo.getNickName());
//            return true;
//        }
//        return false;
//    }

}
