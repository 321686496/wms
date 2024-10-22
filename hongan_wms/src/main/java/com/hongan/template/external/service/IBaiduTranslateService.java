package com.hongan.template.external.service;

import com.hongan.template.base.entity.BaseException;

import java.io.IOException;
import java.util.List;

public interface IBaiduTranslateService {
    /**
     * 翻译
     *
     * @param from  原语言
     * @param to    目标语言
     * @param query 翻译内容
     * @return
     */
    List<String> translate(String from, String to, String query) throws BaseException, IOException, InterruptedException;
}
