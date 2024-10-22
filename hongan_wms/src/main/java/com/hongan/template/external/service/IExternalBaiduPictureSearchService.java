package com.hongan.template.external.service;

import com.hongan.template.base.entity.BaseException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 百度云相似图片搜索 服务类
 * </p>
 *
 * @author Administrator
 * @since 2023-04-10
 */
public interface IExternalBaiduPictureSearchService {
    //图片入库
    String pictureInStock( String imgUrl, String code) throws BaseException;

    //图片检索
    List<Map<String, String>> pictureSearch( byte[] bytes) throws BaseException, IOException;
}
