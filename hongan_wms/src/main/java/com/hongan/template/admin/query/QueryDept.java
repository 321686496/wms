package com.hongan.template.admin.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hongan.template.admin.entity.HonganDept;
import com.hongan.template.base.entity.BaseQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class QueryDept extends BaseQuery<HonganDept> {

    @Override
    public QueryWrapper<HonganDept> toWrapper() {
        return super.toWrapper()
                .orderByDesc("priority", "id");
    }
}
