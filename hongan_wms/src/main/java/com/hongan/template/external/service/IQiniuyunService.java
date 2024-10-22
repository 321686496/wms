package com.hongan.template.external.service;

import com.hongan.template.external.vo.QiniuToken;

public interface IQiniuyunService {

    QiniuToken getToken();

    String uploadFile(byte[] file, String key);
}
