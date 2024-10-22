package com.hongan.template.system.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hongan.template.base.entity.BaseQuery;
import com.hongan.template.system.entity.HonganSystemBanner;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class QueryBanner extends BaseQuery<HonganSystemBanner> {

    @Override
    public QueryWrapper<HonganSystemBanner> toWrapper() {
        return super.toWrapper()
                .orderByDesc("priority", "id");
    }
}
