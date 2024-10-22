package com.hongan.template.config.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hongan.template.base.entity.BaseQuery;
import com.hongan.template.config.entity.HonganConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class QuerySystemConfig extends BaseQuery<HonganConfig> {
    private String configName;
    private String configKey;

    @Override
    public QueryWrapper<HonganConfig> toWrapper() {
        return super.toWrapper()
                .like(notEmpty(configName), "config_name", configName)
                .like(notEmpty(configKey), "config_key", configKey)
                ;
    }
}
