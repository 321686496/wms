package com.hongan.template.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hongan.template.base.entity.BaseError;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.base.utils.StringUtils;
import com.hongan.template.system.entity.HonganSystemAgreement;
import com.hongan.template.system.mapper.HonganSystemAgreementMapper;
import com.hongan.template.system.query.QueryAgreement;
import com.hongan.template.system.service.IHonganSystemAgreementService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 系统协议表 服务实现类
 * </p>
 *
 * @author Administrator
 * @since 2024-05-10
 */
@Service
public class HonganSystemAgreementServiceImpl extends ServiceImpl<HonganSystemAgreementMapper, HonganSystemAgreement> implements IHonganSystemAgreementService {
    @Override
    public IPage<HonganSystemAgreement> queryPage(QueryAgreement query) {
        IPage<HonganSystemAgreement> page = new Page<>(query.getCurrent(), query.getPageSize());
        return baseMapper.selectPage(page, query.toWrapper());
    }

    @Override
    public HonganSystemAgreement selectById(Long id) throws BaseException {
        if (null == id) throw new BaseException(BaseError.BadRequest);
        return baseMapper.selectById(id);
    }

    @Override
    public HonganSystemAgreement selectByType(String type) throws BaseException {
        if (StringUtils.isEmpty(type)) throw new BaseException(BaseError.BadRequest);
        return baseMapper.selectOne(new QueryWrapper<HonganSystemAgreement>().eq("type", type));
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public void update(HonganSystemAgreement data) throws BaseException {
        if (null == data.getId()) throw new BaseException(BaseError.BadRequest);
        baseMapper.updateById(data);
    }
}
