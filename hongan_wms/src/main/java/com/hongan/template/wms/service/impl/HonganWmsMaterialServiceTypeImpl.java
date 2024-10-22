package com.hongan.template.wms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hongan.template.base.entity.BaseError;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.base.utils.DataCheckUtils;
import com.hongan.template.base.utils.RandomUtils;
import com.hongan.template.wms.entity.HonganWmsMaterial;
import com.hongan.template.wms.entity.HonganWmsMaterialType;
import com.hongan.template.wms.mapper.HonganWmsMaterialMapper;
import com.hongan.template.wms.mapper.HonganWmsMaterialTypeMapper;
import com.hongan.template.wms.query.QueryMaterial;
import com.hongan.template.wms.query.QueryMaterialType;
import com.hongan.template.wms.service.IHonganWmsMaterialService;
import com.hongan.template.wms.service.IHonganWmsMaterialTypeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 物料表 服务实现类
 * </p>
 *
 * @author Administrator
 * @since 2024-06-25
 */
@Service
public class HonganWmsMaterialServiceTypeImpl extends ServiceImpl<HonganWmsMaterialTypeMapper, HonganWmsMaterialType> implements IHonganWmsMaterialTypeService {
    @Override
    public IPage<HonganWmsMaterialType> queryPage(QueryMaterialType query) {
        IPage<HonganWmsMaterialType> page = new Page<>(query.getCurrent(), query.getPageSize());
        return baseMapper.selectPage(page, query.toWrapper());
    }

    @Override
    public List<HonganWmsMaterialType> queryList(QueryMaterialType query) {
        return baseMapper.selectList(query.toWrapper());
    }

    @Override
    public HonganWmsMaterialType selectById(Long id) throws BaseException {
        if (null == id) throw new BaseException(BaseError.BadRequest);
        HonganWmsMaterialType data = baseMapper.selectById(id);
        if (data == null) throw new BaseException(BaseError.DataNotExist);
        return data;
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public void create(HonganWmsMaterialType data) throws BaseException {
        checkData(data);
        //查询名称是否重复
        Long count = baseMapper.selectCount(new QueryWrapper<HonganWmsMaterialType>()
                .eq("classify_name", data.getClassifyName()));
        if (count > 0) {
            throw new BaseException(BaseError.NameRepeat);
        }
        data.insert();
    }

    private void checkData(HonganWmsMaterialType data) throws BaseException {
        DataCheckUtils.checkParam(data.getClassifyName(), BaseError.PleaseInputName);
        if (StringUtils.isNotEmpty(data.getClassifyName())) data.setClassifyName(data.getClassifyName().trim());
//        if (StringUtils.isNotEmpty(data.getCode())) data.setCode(data.getCode().trim());
//        if (data.getPriority() == null) data.setPriority(0);
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public void update(HonganWmsMaterialType data) throws BaseException {
        checkData(data);
        HonganWmsMaterialType material = selectById(data.getId());
        //查询名称是否重复
        Long count = baseMapper.selectCount(new QueryWrapper<HonganWmsMaterialType>()
                .ne("id", data.getId())
                .eq("classify_name", data.getClassifyName()));
        if (count > 0) {
            throw new BaseException(BaseError.NameRepeat);
        }
        material.setClassifyName(data.getClassifyName());
        data.updateById();
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public void delete(Long id) {
        baseMapper.deleteById(id);
    }
}
