package com.hongan.template.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.system.entity.HonganSystemAgreement;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hongan.template.system.query.QueryAgreement;

/**
 * <p>
 * 系统协议表 服务类
 * </p>
 *
 * @author Administrator
 * @since 2024-05-10
 */
public interface IHonganSystemAgreementService extends IService<HonganSystemAgreement> {
    IPage<HonganSystemAgreement> queryPage(QueryAgreement query);

    HonganSystemAgreement selectById(Long id) throws BaseException;

    HonganSystemAgreement selectByType(String type) throws BaseException;

    void update(HonganSystemAgreement data) throws BaseException;

}
