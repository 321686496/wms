package com.hongan.template.system.init;

import com.hongan.template.base.security.AutoInitComponent;
import com.hongan.template.system.entity.HonganSystemConfig;
import com.hongan.template.system.service.IHonganSystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * 初始化系统配置项目
 */
@Component
public class InitSystemConfig implements AutoInitComponent {
    @Autowired
    private IHonganSystemConfigService service;

    @Override
    public void init(ApplicationContext context) {
        HonganSystemConfig config = service.getById(1);
        if (config == null) {
            config = new HonganSystemConfig();
            config.setId(1L);
            config.setUserDefaultAvatar("https://beijingqizhe-mall.ponlay.com/imgs/b2c14646-1e1b-4764-af6a-bd59c3965bc7.png");
            config.setUserDefaultNicknamePrefix("用户-");
            config.insert();
        }
    }
}
