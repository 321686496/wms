package com.hongan.template.wms.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hongan.template.base.entity.*;
import com.hongan.template.base.vo.BatchOperateResponse;
import com.hongan.template.security.common.TokenUser;
import com.hongan.template.wms.entity.HonganWmsMaterial;
import com.hongan.template.wms.entity.HonganWmsMaterialType;
import com.hongan.template.wms.query.QueryMaterial;
import com.hongan.template.wms.query.QueryMaterialType;
import com.hongan.template.wms.service.IHonganWmsMaterialService;
import com.hongan.template.wms.service.IHonganWmsMaterialTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 物料表 前端控制器
 * </p>
 *
 * @author Administrator
 * @since 2024-06-25
 */
@Api(tags = "基础档案-物料类别管理")
@Slf4j
@Validated
@RestController
@RequestMapping("/admin/wms/material/type")
public class HonganWmsMaterialTypeController extends BaseController {
    @Autowired
    private IHonganWmsMaterialTypeService service;

    @ApiOperation("分页查询")
    @GetMapping
    public BaseResponse queryPage(Authentication auth, QueryMaterialType query) throws BaseException {
        TokenUser user = (TokenUser) auth.getPrincipal();
        IPage<HonganWmsMaterialType> page = service.queryPage(query);
        Pagination pagination = new Pagination(page.getCurrent(), page.getSize(), page.getTotal());
        return new BaseResponse().success(page.getRecords()).pagination(pagination);
    }

    @ApiOperation("查询列表")
    @GetMapping("/list")
    public BaseResponse queryList(Authentication auth, QueryMaterialType query) throws BaseException {
        TokenUser user = (TokenUser) auth.getPrincipal();
        List<HonganWmsMaterialType> list = service.queryList(query);
        return new BaseResponse().success(list);
    }

    @ApiOperation("新增")
    @PostMapping
    public BaseResponse create(Authentication auth, @RequestBody HonganWmsMaterialType data) throws BaseException {
        TokenUser user = (TokenUser) auth.getPrincipal();
        service.create(data);
        return new BaseResponse().success();
    }

    @ApiOperation("修改")
    @PutMapping
    public BaseResponse update(Authentication auth, @RequestBody HonganWmsMaterialType data) throws BaseException {
        TokenUser user = (TokenUser) auth.getPrincipal();
        service.update(data);
        return new BaseResponse().success();
    }

    @ApiOperation("删除单个")
    @DeleteMapping
    public BaseResponse delete(Authentication auth, @NotNull @RequestBody Id id) throws BaseException {
        TokenUser user = (TokenUser) auth.getPrincipal();
        service.delete(id.getId());
        return new BaseResponse().success();
    }

    @ApiOperation("批量删除")
    @DeleteMapping("/batch")
    public BaseResponse batchDelete(Authentication auth, @NotNull @RequestBody Id data) throws BaseException {
        TokenUser user = (TokenUser) auth.getPrincipal();
        Integer successCount = 0;
        Integer errorCount = 0;
        List<Map<String, Object>> errList = new ArrayList<>();
        for (Long id : data.getIds()) {
            try {
                service.delete(id);
                successCount++;
            } catch (Exception e) {
                e.printStackTrace();
                errorCount++;
                errList.add(new HashMap<>() {{
                    put("data", "");
                    put("errorMsg", e.getMessage());
                }});
            }
        }
        return new BaseResponse().success(new BatchOperateResponse(data.getIds().size(), successCount, errorCount, errList));
    }
}
