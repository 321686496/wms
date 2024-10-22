package com.hongan.template.external.vo;

import lombok.Data;

@Data
public class WechatUserInfo {
//    @NotNull
    private String avatarUrl;
//    @NotNull
    private String city;
//    @NotNull
    private String country;
//    @NotNull
    private Integer gender;
//    @NotNull
    private String language;
//    @NotNull
    private String nickName;
//    @NotNull
    private String province;

    private String is_demote;
}
