package com.hongan.template.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.user.entity.HonganUserAddress;
import com.hongan.template.user.query.QueryAddress;

import java.util.List;

/**
 * <p>
 * 用户收货地址表 服务类
 * </p>
 *
 * @author Administrator
 * @since 2023-05-09
 */
public interface IHonganUserAddressService extends IService<HonganUserAddress> {
    IPage<HonganUserAddress> queryPage(QueryAddress query);

    /**
     * 查询用户的所有收货地址
     */
    List<HonganUserAddress> queryList(QueryAddress query);

    HonganUserAddress selectById(Long id) throws BaseException;

    HonganUserAddress selectById(Long id, Long userId) throws BaseException;

    void create(HonganUserAddress data) throws BaseException;

    void update(HonganUserAddress data) throws BaseException;

    void setDefault(Long userId, Long id) throws BaseException;


    HonganUserAddress getDefault(Long userId) throws BaseException;

    /**
     * 根据Id删除用户收货地址
     */
    void deleteById(Long id, Long userId) throws BaseException;
}
