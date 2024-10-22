package com.hongan.template.system.service;

import com.hongan.template.system.entity.HonganSystemConfig;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 系统配置表 服务类
 * </p>
 *
 * @author Administrator
 * @since 2024-06-12
 */
public interface IHonganSystemConfigService extends IService<HonganSystemConfig> {

    HonganSystemConfig getConfig();

    //修改系统配置
    void updateConfig(HonganSystemConfig data);

}
