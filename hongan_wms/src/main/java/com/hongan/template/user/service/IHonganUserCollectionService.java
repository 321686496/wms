package com.hongan.template.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.enums.SystemGoodsType;
import com.hongan.template.user.entity.HonganUserCollection;
import com.hongan.template.user.query.QueryCollection;

import java.util.List;

/**
 * <p>
 * 用户收藏表 服务类
 * </p>
 *
 * @author Administrator
 * @since 2023-04-03
 */
public interface IHonganUserCollectionService extends IService<HonganUserCollection> {
    IPage<HonganUserCollection> queryPage(QueryCollection query) throws BaseException;

    Boolean collection(SystemGoodsType type, Long userId, Long actionId) throws BaseException;

    Boolean checkCollection(SystemGoodsType type, Long userId, Long actionId);

    void delete(Long userId,List<Long> ids);

}
