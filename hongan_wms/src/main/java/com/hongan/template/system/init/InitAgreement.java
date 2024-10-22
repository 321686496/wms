package com.hongan.template.system.init;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hongan.template.base.security.AutoInitComponent;
import com.hongan.template.system.entity.HonganSystemAgreement;
import com.hongan.template.system.service.IHonganSystemAgreementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 初始化系统配置项目
 */
@Component
public class InitAgreement implements AutoInitComponent {
    @Autowired
    private IHonganSystemAgreementService service;

    @Override
    public void init(ApplicationContext context) {
        //填写初始化的属性
        List<HonganSystemAgreement> list = new ArrayList<>();
        list.add(new HonganSystemAgreement("隐私协议", ""));
        list.add(new HonganSystemAgreement("服务协议", ""));
        list.add(new HonganSystemAgreement("关于我们", ""));
        list.add(new HonganSystemAgreement("用户使用说明", ""));
        for (HonganSystemAgreement data : list) {
            HonganSystemAgreement config = service.getOne(new QueryWrapper<HonganSystemAgreement>()
                    .eq("type", data.getType()));
            if (config == null) {
                data.insert();
            }
        }
    }
}
