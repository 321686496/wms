package com.hongan.template.wms.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hongan.template.base.entity.BaseQuery;
import com.hongan.template.wms.entity.HonganWmsReportOrderItem;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class QueryReportOrderItem extends BaseQuery<HonganWmsReportOrderItem> {
    @Override
    public QueryWrapper<HonganWmsReportOrderItem> toWrapper() {
        return super.toWrapper();
    }
}
