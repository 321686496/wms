package com.hongan.template.external.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QiniuToken {
    private String token;
    private Integer expired;
    private String domain;
    public static QiniuToken from(String token, Integer expired, String domain) {
        return new QiniuToken(token, expired, domain);
    }
}
