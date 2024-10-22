package com.hongan.template.admin.controller;


import com.hongan.template.admin.entity.HonganMenu;
import com.hongan.template.admin.query.QueryMenu;
import com.hongan.template.admin.service.IHonganMenuService;
import com.hongan.template.base.entity.BaseController;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.base.entity.BaseResponse;
import com.hongan.template.base.entity.Id;
import com.hongan.template.base.vo.TreeDataVo;
import com.hongan.template.security.common.TokenUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author zhangxd
 * @since 2020-02-24
 */
@Api(tags = "管理端-菜单管理")
@Slf4j
@RestController
@RequestMapping("/admin/security/menu")
public class HonganMenuController extends BaseController {

    @Autowired
    IHonganMenuService menuService;

    @ApiOperation("查询租户可用菜单")
    @GetMapping("/tree/tenant")
    public BaseResponse queryMenuTree(Authentication auth, QueryMenu query) throws BaseException {
        TokenUser user = (TokenUser) auth.getPrincipal();
        query.setEnable(true);
        List<TreeDataVo> res = menuService.getTenantMenuTree(query);
        return new BaseResponse().success(res);
    }

    @ApiOperation("查询管理员可用菜单")
    @GetMapping("/tree/admin")
    public BaseResponse queryAdminTree(Authentication auth, QueryMenu query) throws BaseException {
        TokenUser user = (TokenUser) auth.getPrincipal();
        query.setRoleKey(user.getRoleKey());
        query.setEnable(true);
        List<TreeDataVo> res = menuService.getAdminMenuTree(query);
        return new BaseResponse().success(res);
    }

    @ApiOperation("查询系统菜单")
    @GetMapping
    public BaseResponse queryPage(QueryMenu query) throws BaseException {
        List<HonganMenu> list = menuService.queryMenu(query);
        return new BaseResponse().success(list);
    }

    @ApiOperation("查询系统菜单")
    @GetMapping("/list")
    public BaseResponse queryList(QueryMenu query) {
        List<HonganMenu> list = menuService.queryList(query);
        return new BaseResponse().success(list);
    }

    @ApiOperation("查询系统菜单(下拉框数据)")
    @GetMapping("/tree")
    public BaseResponse queryMenuSelectData(QueryMenu query) throws BaseException {
        List<TreeDataVo> list = menuService.queryMenuSelectData(query);
        return new BaseResponse().success(list);
    }

    @ApiOperation("查看菜单详情")
    @GetMapping("/one")
    public BaseResponse getById(@NotNull @Min(1) Long id) throws BaseException {
        HonganMenu menu = menuService.selectById(id);
        return new BaseResponse().success(menu);
    }

    @PostMapping
    @ApiOperation("新增菜单")
    public BaseResponse create(@RequestBody HonganMenu data) {
        menuService.create(data);
        return new BaseResponse().success();
    }

    @PutMapping
    @ApiOperation("修改菜单")
    public BaseResponse update(@RequestBody HonganMenu menu) throws BaseException {
        menuService.update(menu);
        return new BaseResponse().success();
    }

    @PutMapping("/enable")
    @ApiOperation("修改菜单启用/禁用状态")
    public BaseResponse enable(@RequestBody Id id) throws BaseException {
        menuService.updateStatus(id.getId());
        return new BaseResponse().success();
    }

    @ApiOperation("删除单个菜单")
    @DeleteMapping
    public BaseResponse delete(@RequestBody Id id) {
        menuService.delete(id.getId());
        return new BaseResponse().success();
    }




}
