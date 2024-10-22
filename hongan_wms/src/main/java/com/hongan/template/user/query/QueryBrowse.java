package com.hongan.template.user.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hongan.template.base.entity.BaseQuery;
import com.hongan.template.enums.SystemGoodsType;
import com.hongan.template.user.entity.HonganUserBrowse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class QueryBrowse extends BaseQuery<HonganUserBrowse> {
    private SystemGoodsType type;


    @Override
    public QueryWrapper<HonganUserBrowse> toWrapper() {
        return super.toWrapper()
                .eq(type != null, "type", type)
                .orderByDesc("updated_at");
    }
}
