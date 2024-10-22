package com.hongan.template.system.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hongan.template.base.entity.BaseQuery;
import com.hongan.template.system.entity.HonganSystemAgreement;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class QueryAgreement extends BaseQuery<HonganSystemAgreement> {

    @Override
    public QueryWrapper<HonganSystemAgreement> toWrapper() {
        return super.toWrapper()
                .orderByDesc("id");
    }
}
