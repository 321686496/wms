package com.hongan.template.system.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hongan.template.base.entity.BaseQuery;
import com.hongan.template.system.entity.HonganSystemMessage;
import com.hongan.template.enums.MessageType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class QueryMessage extends BaseQuery<HonganSystemMessage> {
    private MessageType messageType;
    private String orderNo;
    @Override
    public QueryWrapper<HonganSystemMessage> toWrapper() {
        return super.toWrapper();
    }
}
