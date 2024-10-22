package com.hongan.template.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hongan.template.user.entity.HonganUserAddress;
import com.hongan.template.user.mapper.HonganUserAddressMapper;
import com.hongan.template.base.entity.BaseError;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.user.query.QueryAddress;
import com.hongan.template.user.service.IHonganUserAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 用户收货地址表 服务实现类
 * </p>
 *
 * @author Administrator
 * @since 2023-05-09
 */
@Service
public class HonganUserAddressServiceImpl extends ServiceImpl<HonganUserAddressMapper, HonganUserAddress> implements IHonganUserAddressService {
    @Override
    public IPage<HonganUserAddress> queryPage(QueryAddress query) {
        Page<HonganUserAddress> page = new Page<>(query.getCurrent(), query.getPageSize());
        return baseMapper.selectPage(page, query.toWrapper());
    }

    @Override
    public List<HonganUserAddress> queryList(QueryAddress query) {
        return baseMapper.selectList(query.toWrapper());
    }

    @Override
    public HonganUserAddress selectById(Long id) throws BaseException {
        if (null == id) throw new BaseException(BaseError.BadRequest);
        HonganUserAddress data = baseMapper.selectById(id);
        if (null == data) throw new BaseException(BaseError.DataNotExist);
        return data;
    }

    @Override
    public HonganUserAddress selectById(Long id, Long userId) throws BaseException {
        if (null == id || userId == null) throw new BaseException(BaseError.BadRequest);
        HonganUserAddress data = baseMapper.selectOne(new QueryWrapper<HonganUserAddress>()
                .eq("id", id)
                .eq("user_id", userId));
        if (null == data) throw new BaseException(BaseError.DataNotExist);
        return data;
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public void create(HonganUserAddress data) throws BaseException {
        checkDate(data);

        //如果没有其他地址  改地址则为默认地址
        Long count = baseMapper.selectCount(new QueryWrapper<HonganUserAddress>().eq("user_id", data.getUserId()));
        if (count < 1) {
            data.setIsDefault(true);
        } else {
            if (data.getIsDefault() == true) {
                HonganUserAddress PonlayUserAddress = baseMapper.selectOne(new QueryWrapper<HonganUserAddress>()
                        .eq("user_id", data.getUserId())
                        .eq("is_default", true));
                if (PonlayUserAddress != null) {
                    PonlayUserAddress.setIsDefault(false);
                    PonlayUserAddress.updateById();
                }
            }
        }
        baseMapper.insert(data);
    }


    private void checkDate(HonganUserAddress data) throws BaseException {
    }

    @Override
    public void update(HonganUserAddress data) throws BaseException {
        checkDate(data);

        if (null == data.getId())
            throw new BaseException(BaseError.BadRequest);
        if (data.getIsDefault()) {
            HonganUserAddress PonlayUserAddress = baseMapper.selectOne(new QueryWrapper<HonganUserAddress>()
                    .eq("user_id", data.getUserId())
                    .eq("is_default", true));
            if (PonlayUserAddress != null) {
                PonlayUserAddress.setIsDefault(false);
                PonlayUserAddress.updateById();
            }
        }
        baseMapper.updateById(data);
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    public void setDefault(Long userId, Long id) throws BaseException {
        if (null == id) throw new BaseException(BaseError.BadRequest);
        HonganUserAddress address = new HonganUserAddress();
        address.setIsDefault(false);
        baseMapper.update(address, new QueryWrapper<HonganUserAddress>()
                .eq("user_id", userId)
                .eq("is_default", true));
        address.setId(id);
        address.setIsDefault(true);
        baseMapper.updateById(address);
    }

    @Override
    public HonganUserAddress getDefault(Long userId) throws BaseException {
        return baseMapper.selectOne(new QueryWrapper<HonganUserAddress>()
                .eq("user_id", userId)
                .eq("is_default", true));
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    public void deleteById(Long id, Long userId) throws BaseException {
        HonganUserAddress address = selectById(id, userId);
        if (address.getIsDefault()) {
            List<HonganUserAddress> list = baseMapper.selectList(new QueryWrapper<HonganUserAddress>()
                    .eq("user_id", userId)
                    .ne("id", id));
            if (list.size() > 0) {
                HonganUserAddress userAddress = list.get(0);
                userAddress.setIsDefault(true);
                userAddress.updateById();
            }
        }
        baseMapper.deleteById(id);
    }
}
