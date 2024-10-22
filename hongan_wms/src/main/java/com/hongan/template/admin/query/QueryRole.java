package com.hongan.template.admin.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hongan.template.admin.entity.HonganRole;
import com.hongan.template.base.entity.BaseQuery;
import lombok.Data;

@Data
public class QueryRole extends BaseQuery<HonganRole> {
    private String description;

    @Override
    public QueryWrapper<HonganRole> toWrapper() {
        return super.toWrapper()
                .like(notEmpty(description), "description", description)
                ;
    }
}
