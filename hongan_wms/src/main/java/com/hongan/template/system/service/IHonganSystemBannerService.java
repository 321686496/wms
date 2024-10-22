package com.hongan.template.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.system.entity.HonganSystemBanner;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hongan.template.system.query.QueryBanner;

import java.util.List;

/**
 * <p>
 * 系统轮播图表 服务类
 * </p>
 *
 * @author Administrator
 * @since 2024-05-10
 */
public interface IHonganSystemBannerService extends IService<HonganSystemBanner> {
    IPage<HonganSystemBanner> queryPage(QueryBanner query) throws BaseException;

    List<HonganSystemBanner> queryList(QueryBanner query);

    HonganSystemBanner selectById(Long id, String... keys) throws BaseException;

    void create(HonganSystemBanner data) throws BaseException;

    void update(HonganSystemBanner data) throws BaseException;

    void updateStatus(Long id) throws BaseException;

    void delete(Long id) throws BaseException;
}
