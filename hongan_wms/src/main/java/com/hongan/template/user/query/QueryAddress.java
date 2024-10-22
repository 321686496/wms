package com.hongan.template.user.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hongan.template.base.entity.BaseQuery;
import com.hongan.template.user.entity.HonganUserAddress;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class QueryAddress extends BaseQuery<HonganUserAddress> {

    @Override
    public QueryWrapper<HonganUserAddress> toWrapper() {
        return super.toWrapper()
                .orderByDesc("is_default", "id");
    }
}
