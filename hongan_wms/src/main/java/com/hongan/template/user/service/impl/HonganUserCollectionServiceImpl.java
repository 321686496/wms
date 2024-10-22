package com.hongan.template.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hongan.template.enums.SystemGoodsType;
import com.hongan.template.user.entity.HonganUserCollection;
import com.hongan.template.base.entity.BaseError;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.user.mapper.HonganUserCollectionMapper;
import com.hongan.template.user.query.QueryCollection;
import com.hongan.template.user.service.IHonganUserCollectionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 用户收藏表 服务实现类
 * </p>
 *
 * @author Administrator
 * @since 2023-04-03
 */
@Service
public class HonganUserCollectionServiceImpl extends ServiceImpl<HonganUserCollectionMapper, HonganUserCollection> implements IHonganUserCollectionService {


    @Override
    public IPage<HonganUserCollection> queryPage(QueryCollection query) throws BaseException {
        IPage<HonganUserCollection> page = new Page<>(query.getCurrent(), query.getPageSize());
        List<HonganUserCollection> records = baseMapper.selectPage(page, query.toWrapper()).getRecords();
        for (HonganUserCollection record : records) {
        }
        return page;
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Boolean collection(SystemGoodsType type, Long userId, Long actionId) throws BaseException {
        if (type == null || userId == null || actionId == null) throw new BaseException(BaseError.BadRequest);
        HonganUserCollection data = baseMapper.selectOne(new QueryWrapper<HonganUserCollection>()
                .eq("type", type)
                .eq("user_id", userId)
                .eq("action_id", actionId));
        if (data == null) {
            data = new HonganUserCollection();
            data.setType(type);
            data.setUserId(userId);
            data.setActionId(actionId);
            data.insert();
            return true;
        } else {
            data.deleteById();
            return false;
        }
    }

    @Override
    public Boolean checkCollection(SystemGoodsType type, Long userId, Long actionId) {
        if (type == null || userId == null || actionId == null) return false;
        HonganUserCollection data = baseMapper.selectOne(new QueryWrapper<HonganUserCollection>()
                .eq("type", type)
                .eq("user_id", userId)
                .eq("action_id", actionId));
        return data != null;
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public void delete(Long userId, List<Long> ids) {
        if (ids != null && ids.size() > 0) {
            baseMapper.delete(new QueryWrapper<HonganUserCollection>()
                    .eq("user_id", userId)
                    .in("id", ids)
            );
        }
    }
}
