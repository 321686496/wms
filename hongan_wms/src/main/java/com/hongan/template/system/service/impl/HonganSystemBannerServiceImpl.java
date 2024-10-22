package com.hongan.template.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hongan.template.base.entity.BaseError;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.system.mapper.HonganSystemBannerMapper;
import com.hongan.template.system.service.IHonganSystemBannerService;
import com.hongan.template.system.entity.HonganSystemBanner;
import com.hongan.template.system.query.QueryBanner;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 系统轮播图表 服务实现类
 * </p>
 *
 * @author Administrator
 * @since 2024-05-10
 */
@Service
public class HonganSystemBannerServiceImpl extends ServiceImpl<HonganSystemBannerMapper, HonganSystemBanner> implements IHonganSystemBannerService {

    @Override
    public IPage<HonganSystemBanner> queryPage(QueryBanner query) throws BaseException {
        IPage<HonganSystemBanner> page = new Page<>(query.getCurrent(), query.getPageSize());
        return baseMapper.selectPage(page, query.toWrapper());
    }

    @Override
    public List<HonganSystemBanner> queryList(QueryBanner query) {
        return baseMapper.selectList(query.toWrapper());
    }

    @Override
    public HonganSystemBanner selectById(Long id, String... keys) throws BaseException {
        if (null == id) throw new BaseException(BaseError.BadRequest);
        if (keys == null || keys.length < 1) keys = new String[]{"*"};
        HonganSystemBanner banner = baseMapper.selectOne(new QueryWrapper<HonganSystemBanner>()
                .eq("id", id)
                .select(keys));
        if (banner == null)
            throw new BaseException(BaseError.DataNotExist);
        return banner;
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public void create(HonganSystemBanner data) throws BaseException {
        baseMapper.insert(data);
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public void update(HonganSystemBanner data) throws BaseException {
        if (null == data.getId())
            throw new BaseException(BaseError.BadRequest);
        baseMapper.updateById(data);
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public void updateStatus(Long id) throws BaseException {
        HonganSystemBanner banner = selectById(id);
        banner.setEnable(!banner.getEnable());
        banner.updateById();
    }


    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public void delete(Long id) throws BaseException {
        selectById(id);
        baseMapper.deleteById(id);
    }
}
