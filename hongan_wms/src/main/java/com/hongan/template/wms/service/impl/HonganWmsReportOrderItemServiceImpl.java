package com.hongan.template.wms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hongan.template.base.entity.BaseError;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.wms.entity.HonganWmsReportOrderItem;
import com.hongan.template.wms.mapper.HonganWmsReportOrderItemMapper;
import com.hongan.template.wms.query.QueryReportOrderItem;
import com.hongan.template.wms.service.IHonganWmsReportOrderItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 报货单明细表 服务实现类
 * </p>
 *
 * @author Administrator
 * @since 2024-06-25
 */
@Service
public class HonganWmsReportOrderItemServiceImpl extends ServiceImpl<HonganWmsReportOrderItemMapper, HonganWmsReportOrderItem> implements IHonganWmsReportOrderItemService {

    @Override
    public List<HonganWmsReportOrderItem> queryList(QueryReportOrderItem query) throws BaseException {
        if (null == query.getPid()) throw new BaseException(BaseError.BadRequest);
        List<HonganWmsReportOrderItem> list = baseMapper.selectList(query.toWrapper());
        return list;
    }

    @Override
    public List<HonganWmsReportOrderItem> queryListByPid(Long pid) throws BaseException {
        if (null == pid) throw new BaseException(BaseError.BadRequest);
        List<HonganWmsReportOrderItem> list = baseMapper.selectList(new QueryWrapper<HonganWmsReportOrderItem>()
                .eq("pid", pid)
                .orderByAsc("type_id")
        );
        return list;
    }

    @Override
    public HonganWmsReportOrderItem selectById(Long pid, Long id) throws BaseException {
        if (null == pid || null == id) throw new BaseException(BaseError.BadRequest);
        HonganWmsReportOrderItem data = baseMapper.selectOne(new QueryWrapper<HonganWmsReportOrderItem>()
                .eq("id", id)
                .eq("pid", pid));
        if (data == null) throw new BaseException(BaseError.DataNotExist);
        return data;
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public void delete(Long pid, Long id) throws BaseException {
        selectById(pid, id);
        baseMapper.deleteById(id);
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public void deleteByPid(Long pid) throws BaseException {
        baseMapper.delete(new QueryWrapper<HonganWmsReportOrderItem>()
                .eq("pid", pid));
    }
}
