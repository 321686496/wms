package com.hongan.template.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hongan.template.base.entity.BaseException;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hongan.template.user.entity.HonganUserBrowse;
import com.hongan.template.user.query.QueryBrowse;

import java.util.List;

/**
 * <p>
 * 用户浏览记录表 服务类
 * </p>
 *
 * @author Administrator
 * @since 2023-04-19
 */
public interface IHonganUserBrowseService extends IService<HonganUserBrowse> {
    IPage<HonganUserBrowse> queryPage(QueryBrowse query) throws BaseException;

    //保存浏览记录
    void saveBrowse(Long userId, Long goodsId) throws BaseException;

    //删除浏览记录
    void delete(Long userId, List<Long> ids);

}
