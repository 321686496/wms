package com.hongan.template.system.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hongan.template.base.entity.BaseController;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.base.entity.BaseResponse;
import com.hongan.template.base.entity.Pagination;
import com.hongan.template.system.entity.HonganSystemAgreement;
import com.hongan.template.system.query.QueryAgreement;
import com.hongan.template.system.service.IHonganSystemAgreementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 系统协议表 前端控制器
 * </p>
 *
 * @author Administrator
 * @since 2024-05-10
 */

@Validated
@Api(tags = "管理端-系统管理-系统协议")
@RestController
@RequestMapping("/admin/system/agreement")
public class HonganSystemAgreementController extends BaseController {
    @Autowired
    private IHonganSystemAgreementService service;

    @ApiOperation("分页查询")
    @GetMapping
    public BaseResponse queryPage(QueryAgreement query) {
        IPage<HonganSystemAgreement> page = service.queryPage(query);
        Pagination pagination = new Pagination(page.getCurrent(), page.getSize(), page.getTotal());
        return new BaseResponse().success(page.getRecords()).pagination(pagination);
    }

    @ApiOperation("查询单个详情")
    @GetMapping("/one")
    public BaseResponse getOne(@NotNull @Min(1) Long id) throws BaseException {
        HonganSystemAgreement data = service.selectById(id);
        return new BaseResponse().success(data);
    }

    @ApiOperation("修改内容")
    @PutMapping
    public BaseResponse update(@RequestBody HonganSystemAgreement data) throws BaseException {
        service.update(data);
        return new BaseResponse().success();
    }
}
