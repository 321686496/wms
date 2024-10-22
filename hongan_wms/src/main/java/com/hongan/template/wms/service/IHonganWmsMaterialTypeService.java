package com.hongan.template.wms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.wms.entity.HonganWmsMaterial;
import com.hongan.template.wms.entity.HonganWmsMaterialType;
import com.hongan.template.wms.query.QueryMaterial;
import com.hongan.template.wms.query.QueryMaterialType;

import java.util.List;

/**
 * <p>
 * 物料类别表 服务类
 * </p>
 *
 * @author Administrator
 * @since 2024-06-25
 */
public interface IHonganWmsMaterialTypeService extends IService<HonganWmsMaterialType> {
    IPage<HonganWmsMaterialType> queryPage(QueryMaterialType query);

    List<HonganWmsMaterialType> queryList(QueryMaterialType query);

    HonganWmsMaterialType selectById(Long id) throws BaseException;

    void create(HonganWmsMaterialType data) throws BaseException;

    void update(HonganWmsMaterialType data) throws BaseException;

    void delete(Long id);

}
