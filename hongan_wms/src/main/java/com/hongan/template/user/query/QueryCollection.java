package com.hongan.template.user.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hongan.template.base.entity.BaseQuery;
import com.hongan.template.enums.SystemGoodsType;
import com.hongan.template.user.entity.HonganUserCollection;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class QueryCollection extends BaseQuery<HonganUserCollection> {
    private SystemGoodsType type;
    private Long actionId;

    @Override
    public QueryWrapper<HonganUserCollection> toWrapper() {
        return super.toWrapper()
                .eq(type != null, "type", type)
                .eq(notEmpty(actionId), "action_id", actionId)
                .orderByDesc("id")
                ;
    }
}
