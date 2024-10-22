package com.hongan.template.system.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hongan.template.base.entity.BaseController;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.base.entity.BaseResponse;
import com.hongan.template.base.entity.Pagination;
import com.hongan.template.system.entity.HonganSystemMessage;
import com.hongan.template.system.query.QueryMessage;
import com.hongan.template.system.service.IHonganSystemMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 系统消息表 前端控制器
 * </p>
 *
 * @author Administrator
 * @since 2024-05-21
 */
@Validated
@Api(tags = "管理端-系统配置-系统消息")
@RestController
@RequestMapping("/admin/system/message")
public class HonganSystemMessageController extends BaseController {
    @Autowired
    private IHonganSystemMessageService service;

    @ApiOperation("分页查询")
    @GetMapping
    public BaseResponse queryPage(QueryMessage query) throws BaseException {
        IPage<HonganSystemMessage> page = service.queryPage(query);
        Pagination pagination = new Pagination(page.getCurrent(), page.getSize(), page.getTotal());
        return new BaseResponse().success(page.getRecords()).pagination(pagination);
    }

    @ApiOperation("查询列表")
    @GetMapping("/list")
    public BaseResponse getOne(QueryMessage query) throws BaseException {
        List<HonganSystemMessage> list = service.queryList(query);
        return new BaseResponse().success(list);
    }

}
