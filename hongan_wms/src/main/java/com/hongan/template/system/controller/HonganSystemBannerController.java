package com.hongan.template.system.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hongan.template.system.entity.HonganSystemBanner;
import com.hongan.template.system.query.QueryBanner;
import com.hongan.template.system.service.IHonganSystemBannerService;
import com.hongan.template.base.entity.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 系统轮播图表 前端控制器
 * </p>
 *
 * @author Administrator
 * @since 2024-05-10
 */
@Validated
@Api(tags = "管理端-系统配置-轮播图")
@RestController
@RequestMapping("/admin/system/banner")
public class HonganSystemBannerController extends BaseController {
    @Autowired
    private IHonganSystemBannerService service;

    @ApiOperation("分页查询")
    @GetMapping
    public BaseResponse queryPage(QueryBanner query) throws BaseException {
        IPage<HonganSystemBanner> page = service.queryPage(query);
        Pagination pagination = new Pagination(page.getCurrent(), page.getSize(), page.getTotal());
        return new BaseResponse().success(page.getRecords()).pagination(pagination);
    }

    @ApiOperation("查询单个详情")
    @GetMapping("/one")
    public BaseResponse getOne(@NotNull @Min(1) Long id) throws BaseException {
        HonganSystemBanner data = service.selectById(id);
        return new BaseResponse().success(data);
    }

    @ApiOperation("新增")
    @PostMapping
    public BaseResponse create(@RequestBody HonganSystemBanner category) throws BaseException {
        service.create(category);
        return new BaseResponse().success();
    }

    @ApiOperation("修改")
    @PutMapping
    public BaseResponse update(@RequestBody HonganSystemBanner category) throws BaseException {
        service.update(category);
        return new BaseResponse().success();
    }

    @ApiOperation("修改启用状态")
    @PutMapping("/updateStatus")
    public BaseResponse updateStatus(@RequestBody Id id) throws BaseException {
        service.updateStatus(id.getId());
        return new BaseResponse().success();
    }

    @ApiOperation("删除")
    @DeleteMapping
    public BaseResponse delete(@NotNull @RequestBody Id id) throws BaseException {
        service.delete(id.getId());
        return new BaseResponse().success();
    }
}
