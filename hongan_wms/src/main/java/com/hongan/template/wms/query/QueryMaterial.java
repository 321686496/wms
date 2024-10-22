package com.hongan.template.wms.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hongan.template.base.entity.BaseQuery;
import com.hongan.template.wms.entity.HonganWmsMaterial;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class QueryMaterial extends BaseQuery<HonganWmsMaterial> {
    private String typeName;
    @Override
    public QueryWrapper<HonganWmsMaterial> toWrapper() {
        return super.toWrapper()
                .orderByAsc("type_id")
                .orderByDesc("priority","id");
    }
}
