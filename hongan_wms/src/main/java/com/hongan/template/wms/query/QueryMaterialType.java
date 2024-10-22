package com.hongan.template.wms.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hongan.template.base.entity.BaseQuery;
import com.hongan.template.wms.entity.HonganWmsMaterial;
import com.hongan.template.wms.entity.HonganWmsMaterialType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class QueryMaterialType extends BaseQuery<HonganWmsMaterialType> {
    @Override
    public QueryWrapper<HonganWmsMaterialType> toWrapper() {
        return super.toWrapper()
                .orderByDesc("id");
    }
}
