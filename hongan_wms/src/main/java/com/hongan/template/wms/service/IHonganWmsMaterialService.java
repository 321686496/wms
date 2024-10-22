package com.hongan.template.wms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.wms.entity.HonganWmsMaterial;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hongan.template.wms.query.QueryMaterial;
import com.hongan.template.wms.vo.HonganWmsMaterialVO;

import java.util.List;

/**
 * <p>
 * 物料表 服务类
 * </p>
 *
 * @author Administrator
 * @since 2024-06-25
 */
public interface IHonganWmsMaterialService extends IService<HonganWmsMaterial> {
    IPage<HonganWmsMaterial> queryPage(QueryMaterial query);

    List<HonganWmsMaterial> queryList(QueryMaterial query);

    IPage<HonganWmsMaterialVO> queryPageVO(QueryMaterial query);

    List<HonganWmsMaterialVO> queryListVO(QueryMaterial query);

    HonganWmsMaterial selectById(Long id) throws BaseException;

    void create(HonganWmsMaterial data) throws BaseException;

    void update(HonganWmsMaterial data) throws BaseException;

    void delete(Long id);

}
