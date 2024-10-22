package com.hongan.template.admin.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hongan.template.admin.entity.HonganAdmin;
import com.hongan.template.admin.entity.HonganRole;
import com.hongan.template.admin.query.QueryRole;
import com.hongan.template.admin.service.IHonganRoleService;
import com.hongan.template.base.entity.*;
import com.hongan.template.base.security.IUserService;
import com.hongan.template.base.vo.BatchOperateResponse;
import com.hongan.template.base.vo.TreeDataVo;
import com.hongan.template.security.common.TokenUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zhangxd
 * @since 2020-02-24
 */
@Api(tags = "管理端-角色管理")
@RestController
@RequestMapping("/admin/security/role")
//@AutoAuthorizeModule(value = "security-role", name = "角色管理")
public class HonganRoleController extends BaseController {
    @Autowired
    IHonganRoleService roleService;
    @Autowired

    IUserService<HonganAdmin> userService;

    @ApiOperation("分页查询角色")
    @GetMapping
    @PreAuthorize("hasPermission('security-role', 'query', '系统管理-角色管理-分页查询')")
    public BaseResponse queryPage(Authentication auth, QueryRole query) {
        TokenUser user = (TokenUser) auth.getPrincipal();
        IPage<HonganRole> page = roleService.queryPage(query);
        Pagination pagination = new Pagination(page.getCurrent(), page.getSize(), page.getTotal());
        return new BaseResponse().success(page.getRecords()).pagination(pagination);
    }

    @ApiOperation("查询全部角色")
    @GetMapping("/select")
    public BaseResponse roles(Authentication auth, QueryRole query) {
        TokenUser user = (TokenUser) auth.getPrincipal();
        List<TreeDataVo> list = roleService.queryList(query);
        return new BaseResponse().success(list);
    }

    @ApiOperation("新增")
    @PostMapping
    @PreAuthorize("hasPermission('security-role', 'create', '系统管理-角色管理-新增')")
    public BaseResponse create(Authentication auth, @RequestBody HonganRole role) throws BaseException {
        TokenUser user = (TokenUser) auth.getPrincipal();
        roleService.create(role);
        return new BaseResponse().success();
    }

    @ApiOperation("修改")
    @PutMapping
    @PreAuthorize("hasPermission('security-role', 'update', '系统管理-角色管理-修改')")
    public BaseResponse update(Authentication auth, @RequestBody HonganRole role) throws BaseException {
        TokenUser user = (TokenUser) auth.getPrincipal();
        roleService.update(role);
        //将该角色下的所有用户下线
//        userService.getByPhone()

        return new BaseResponse().success();
    }

    @ApiOperation("复制角色")
    @PutMapping("/copy")
    @PreAuthorize("hasPermission('security-role', 'copy', '系统管理-角色管理-复制角色')")
    public BaseResponse copy(Authentication auth, @RequestBody Id data) throws BaseException {
        TokenUser user = (TokenUser) auth.getPrincipal();
        roleService.copyRole(data.getId());
        //将该角色下的所有用户下线
//        userService.getByPhone()
        return new BaseResponse().success();
    }

    @ApiOperation("批量复制角色")
    @PutMapping("/copy/batch")
    @PreAuthorize("hasPermission('security-role', 'batchCopy', '系统管理-角色管理-批量复制角色')")
    public BaseResponse batchCopy(Authentication auth, @RequestBody Id data) throws BaseException {
        TokenUser user = (TokenUser) auth.getPrincipal();
        Integer successCount = 0;
        Integer errorCount = 0;
        List<Map<String, Object>> errList = new ArrayList<>();
        for (Long id : data.getIds()) {
            try {
                roleService.copyRole(id);
                successCount++;
            } catch (Exception e) {
                e.printStackTrace();
                errorCount++;
                HonganRole role = roleService.getRoleById(id);
                errList.add(new HashMap<>() {{
                    put("data", role.getName());
                    put("errorMsg", e.getMessage());
                }});
            }
        }
        return new BaseResponse().success(new BatchOperateResponse(data.getIds().size(), successCount, errorCount, errList));
    }

    @ApiOperation("删除单个角色")
    @DeleteMapping
    @PreAuthorize("hasPermission('security-role', 'delete', '系统管理-角色管理-单个删除')")
    public BaseResponse delete(Authentication auth, @NotNull @RequestBody Id id) throws BaseException {
        TokenUser user = (TokenUser) auth.getPrincipal();
        roleService.delete( id.getId());
        return new BaseResponse().success();
    }

}
