package com.hongan.template.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hongan.template.base.entity.BaseError;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.user.entity.HonganUserBrowse;
import com.hongan.template.user.mapper.HonganUserBrowseMapper;
import com.hongan.template.user.query.QueryBrowse;
import com.hongan.template.user.service.IHonganUserBrowseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 用户浏览记录表 服务实现类
 * </p>
 *
 * @author Administrator
 * @since 2023-04-19
 */
@Service
public class HonganUserBrowseServiceImpl extends ServiceImpl<HonganUserBrowseMapper, HonganUserBrowse> implements IHonganUserBrowseService {

    @Override
    public IPage<HonganUserBrowse> queryPage(QueryBrowse query) throws BaseException {
        IPage<HonganUserBrowse> page = new Page<>(query.getCurrent(), query.getPageSize());
        List<HonganUserBrowse> records = baseMapper.selectPage(page, query.toWrapper()).getRecords();
        for (HonganUserBrowse record : records) {
        }
        return page;
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public void saveBrowse(Long userId, Long goodsId) throws BaseException {
        if (null == userId || null == goodsId) throw new BaseException(BaseError.BadRequest);
        HonganUserBrowse browse = baseMapper.selectOne(new QueryWrapper<HonganUserBrowse>()
                .eq("user_id", userId)
                .eq("goods_id", goodsId));
        if (browse == null) {
            browse = new HonganUserBrowse();
            browse.setUserId(userId);
            browse.setGoodsId(goodsId);
            browse.insert();
        } else {
            browse.setUpdatedAt(LocalDateTime.now());
            browse.updateById();
        }
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public void delete(Long userId, List<Long> ids) {
        if (ids != null && ids.size() > 0) {
            baseMapper.delete(new QueryWrapper<HonganUserBrowse>()
                    .eq("user_id", userId)
                    .in("id", ids)
            );
        }
    }
}
