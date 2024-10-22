package com.hongan.template.wms.mapper;

import com.hongan.template.wms.entity.HonganWmsReportOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hongan.template.wms.query.QueryReportOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 报货单表 Mapper 接口
 * </p>
 *
 * @author Administrator
 * @since 2024-06-25
 */
public interface HonganWmsReportOrderMapper extends BaseMapper<HonganWmsReportOrder> {
    Integer getTodayBillCount();
}
