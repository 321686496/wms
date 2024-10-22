package com.hongan.template.config.service;

import com.hongan.template.base.entity.BaseException;
import com.hongan.template.config.entity.HonganConfig;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统参数配置表 服务类
 * </p>
 *
 * @author Administrator
 * @since 2023-07-12
 */
public interface IHonganConfigService extends IService<HonganConfig> {

    String getValue(String key) throws BaseException;

    List<String> getValues(String key);

    Map<String, String> getValues(List<String> keys);
}
