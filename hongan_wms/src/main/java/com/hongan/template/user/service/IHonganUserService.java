package com.hongan.template.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.external.vo.WechatPhoneEnData;
import com.hongan.template.external.vo.WechatVo;
import com.hongan.template.user.entity.HonganUser;
import com.hongan.template.user.query.QueryUser;

import java.io.IOException;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author admin
 * @since 2020-07-30
 */
public interface IHonganUserService extends IService<HonganUser> {

    //根据Id查询用户信息 且校验是否存在
    HonganUser selectById(Long id, String... keys) throws BaseException;

    //根据手机号查询用户信息
    HonganUser getByPhone(String phone, String... keys);

    HonganUser getByName(String name, String... keys);

    HonganUser getByWxOpenId(String wxOpenId, String... keys);

    //根据Id查询用户详情
    HonganUser getDetailById(Long userId, String... keys) throws BaseException;

    HonganUser getShowUserMessage(Long userId);

    IPage<HonganUser> queryPage(QueryUser query);

    /**
     * 微信授权登陆
     *
     * @param wechatVo
     * @return
     */
    HonganUser wechatLogin(WechatVo wechatVo) throws BaseException;

    HonganUser updateWechatPhone(String name, WechatPhoneEnData en) throws BaseException, IOException, InterruptedException;

    //用户修改个人信息
    void userUpdateMessage(HonganUser user) throws BaseException;

    //更新用户状态(启用/禁用)
    void updateUserStatus(Long id) throws BaseException, BaseException;


}
