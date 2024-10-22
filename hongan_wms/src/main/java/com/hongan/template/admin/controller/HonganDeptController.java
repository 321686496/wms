package com.hongan.template.admin.controller;


import com.hongan.template.admin.entity.HonganDept;
import com.hongan.template.admin.query.QueryDept;
import com.hongan.template.admin.service.IHonganDeptService;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.base.entity.BaseResponse;
import com.hongan.template.base.entity.Id;
import com.hongan.template.base.vo.TreeDataVo;
import com.hongan.template.security.common.TokenUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.hongan.template.base.entity.BaseController;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 部门表 前端控制器
 * </p>
 *
 * @author Administrator
 * @since 2023-07-12
 */
@Api(tags = "系统管理-部门管理")
@Slf4j
@RestController
@Validated
@RequestMapping("/admin/security/dept")
public class HonganDeptController extends BaseController {
    @Autowired
    IHonganDeptService deptService;

    @ApiOperation("查询系统部门")
    @GetMapping
    @PreAuthorize("hasPermission('security-dept', 'query', '系统管理-部门管理-查询部门列表')")
    public BaseResponse queryPage(Authentication auth, QueryDept query) {
        TokenUser user = (TokenUser) auth.getPrincipal();
        List<HonganDept> list = deptService.queryData(query);
        return new BaseResponse().success(list);
    }

    @ApiOperation("查询系统部门(树状下拉框数据)")
    @GetMapping("/tree")
    public BaseResponse queryTreeData(Authentication auth, QueryDept query) {
        TokenUser user = (TokenUser) auth.getPrincipal();
        query.setEnable(true);
        List<TreeDataVo> list = deptService.queryTreeData(query);
        return new BaseResponse().success(list);
    }

    @ApiOperation("查询系统部门(树状下拉框数据)(检索框使用)")
    @GetMapping("/tree/select")
    public BaseResponse queryTreeSelectData(Authentication auth, QueryDept query) {
        TokenUser user = (TokenUser) auth.getPrincipal();
        query.setEnable(true);
        List<TreeDataVo> list = deptService.queryTreeDataSelect(query, user);
//        List<TreeDataVo> list = deptService.queryTreeData(query);
        return new BaseResponse().success(list);
    }

    @ApiOperation("查看部门详情")
    @GetMapping("/one")
    public BaseResponse getById(Authentication auth, @NotNull @Min(1) Long id) throws BaseException {
        TokenUser user = (TokenUser) auth.getPrincipal();
        HonganDept data = deptService.selectById(id);
        return new BaseResponse().success(data);
    }

    @PostMapping
    @ApiOperation("新增部门")
    @PreAuthorize("hasPermission('security-dept', 'save', '系统管理-部门管理-新增')")
    public BaseResponse create(Authentication auth, @RequestBody HonganDept data) throws BaseException {
        TokenUser user = (TokenUser) auth.getPrincipal();
        deptService.create(data);
        return new BaseResponse().success();
    }

    @PutMapping
    @ApiOperation("修改部门")
    @PreAuthorize("hasPermission('security-dept', 'update', '系统管理-部门管理-修改')")
    public BaseResponse update(Authentication auth, @RequestBody HonganDept data) throws BaseException {
        TokenUser user = (TokenUser) auth.getPrincipal();
        deptService.update(data);
        return new BaseResponse().success();
    }


    @PutMapping("/enable")
    @ApiOperation("修改部门启用/禁用状态")
    @PreAuthorize("hasPermission('security-dept', 'enable', '系统管理-部门管理-启用/禁用')")
    public BaseResponse enable(Authentication auth, @RequestBody Id id) throws BaseException {
        TokenUser user = (TokenUser) auth.getPrincipal();
        deptService.updateStatus(id.getId());
        return new BaseResponse().success();
    }

    @ApiOperation("删除单个部门")
    @DeleteMapping
    @PreAuthorize("hasPermission('security-dept', 'delete', '系统管理-部门管理-删除')")
    public BaseResponse delete(Authentication auth, @RequestBody Id id) throws BaseException {
        TokenUser user = (TokenUser) auth.getPrincipal();
        deptService.delete(id.getId());
        return new BaseResponse().success();
    }
}
