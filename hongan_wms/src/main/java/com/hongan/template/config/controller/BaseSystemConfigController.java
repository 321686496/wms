package com.hongan.template.config.controller;


import com.hongan.template.base.entity.BaseController;
import com.hongan.template.base.entity.BaseResponse;
import com.hongan.template.config.service.IHonganConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Map;

/**
 * <p>
 * 系统参数配置表 前端控制器
 * </p>
 *
 * @author Administrator
 * @since 2023-07-12
 */
@Api(tags = "公共接口-获取全局系统配置项")
@RestController
@RequestMapping("/base/config")
public class BaseSystemConfigController extends BaseController {

    @Autowired
    private IHonganConfigService configService;

    @ApiOperation("获取全局配置项")
    @GetMapping
    public BaseResponse getGlobalConfig() {
        Map<String, String> res = configService.getValues(new ArrayList<String>() {{
            add("system_name");
            add("system_shortName");
            add("system_logo");
            add("system_title");
            add("system_icp");
            add("system_regex_password");
            add("system_regex_phone");
            add("system_icon");
        }});
        return new BaseResponse().success(res);
    }


}
