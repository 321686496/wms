package com.hongan.template.config.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.config.service.IHonganConfigService;
import com.hongan.template.config.entity.HonganConfig;
import com.hongan.template.config.mapper.HonganConfigMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统参数配置表 服务实现类
 * </p>
 *
 * @author Administrator
 * @since 2023-07-12
 */
@Service
public class HonganConfigServiceImpl extends ServiceImpl<HonganConfigMapper, HonganConfig> implements IHonganConfigService {

    @Override
    public String getValue(String key) throws BaseException {
        HonganConfig config = baseMapper.selectOne(new QueryWrapper<HonganConfig>()
                .eq("config_key", key));
        if (config == null) throw new BaseException("系统参数配置不存在，请检查参数是否有误不存在！");
        return config.getConfigValue();
    }

    @Override
    public List<String> getValues(String key) {
        List<HonganConfig> configList = baseMapper.selectList(new QueryWrapper<HonganConfig>()
                .eq("config_key", key));
        List<String> res = configList.stream()
                .map(HonganConfig::getConfigValue) // 取出每个Person对象的name字段
                .collect(Collectors.toList()); // 将结果收集为List
        return res;
    }

    @Override
    public Map<String, String> getValues(List<String> keys) {
        List<HonganConfig> configList = baseMapper.selectList(new QueryWrapper<HonganConfig>()
                .in("config_key", keys));
        return configList.stream().collect(Collectors.toMap(HonganConfig::getConfigKey, HonganConfig::getConfigValue));
    }
}
