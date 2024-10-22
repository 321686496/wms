package com.hongan.template.wms.service;

import com.hongan.template.base.entity.BaseException;
import com.hongan.template.wms.entity.HonganWmsReportOrderItem;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hongan.template.wms.query.QueryReportOrderItem;

import java.util.List;

/**
 * <p>
 * 报货单明细表 服务类
 * </p>
 *
 * @author Administrator
 * @since 2024-06-25
 */
public interface IHonganWmsReportOrderItemService extends IService<HonganWmsReportOrderItem> {
    List<HonganWmsReportOrderItem> queryList(QueryReportOrderItem query) throws BaseException;

    List<HonganWmsReportOrderItem> queryListByPid(Long pid) throws BaseException;

    HonganWmsReportOrderItem selectById(Long pid, Long id) throws BaseException;

    void delete(Long pid, Long id) throws BaseException;

    void deleteByPid(Long pid) throws BaseException;

}
