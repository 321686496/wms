package com.hongan.template.system.service.impl;

import com.hongan.template.system.entity.HonganSystemConfig;
import com.hongan.template.system.mapper.HonganSystemConfigMapper;
import com.hongan.template.system.service.IHonganSystemConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 系统配置表 服务实现类
 * </p>
 *
 * @author Administrator
 * @since 2024-06-12
 */
@Service
public class HonganSystemConfigServiceImpl extends ServiceImpl<HonganSystemConfigMapper, HonganSystemConfig> implements IHonganSystemConfigService {

    @Override
    public HonganSystemConfig getConfig() {
        return baseMapper.selectById(1L);
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public void updateConfig(HonganSystemConfig data) {
        data.setId(1L);
        data.updateById();
    }
}
