package com.hongan.template.system.controller;


import com.hongan.template.base.entity.BaseResponse;
import com.hongan.template.system.entity.HonganSystemConfig;
import com.hongan.template.system.service.IHonganSystemConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.hongan.template.base.entity.BaseController;

/**
 * <p>
 * 系统配置表 前端控制器
 * </p>
 *
 * @author Administrator
 * @since 2024-06-12
 */

@Validated
@Api(tags = "管理端-系统配置")
@RestController
@RequestMapping("/admin/system/config")
public class HonganSystemConfigController extends BaseController {
    @Autowired
    private IHonganSystemConfigService service;

    @ApiOperation("查询系统配置")
    @GetMapping
    public BaseResponse getConfig() {
        HonganSystemConfig config = service.getConfig();
        return new BaseResponse().success(config);
    }

    @ApiOperation("修改系统配置")
    @PutMapping
    public BaseResponse updateConfig(@RequestBody HonganSystemConfig data) {
        service.updateConfig(data);
        return new BaseResponse().success();
    }

}
