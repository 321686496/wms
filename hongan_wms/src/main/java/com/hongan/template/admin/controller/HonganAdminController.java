package com.hongan.template.admin.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hongan.template.admin.query.QueryAdmin;
import com.hongan.template.admin.service.IHonganAdminService;
import com.hongan.template.admin.service.IHonganRoleService;
import com.hongan.template.base.security.IUserService;
import com.hongan.template.base.vo.TreeDataVo;
import com.hongan.template.security.common.TokenUser;
import com.hongan.template.enums.AdminStatus;
import com.hongan.template.admin.entity.HonganAdmin;
import com.hongan.template.base.entity.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author zhangxd
 * @since 2020-02-24
 */

@Api(tags = "管理端-员工管理")
@Slf4j
@RestController
@RequestMapping("/admin/security/admin")
//@AutoAuthorizeModule(value = "security-admin", name = "员工管理")
public class HonganAdminController extends BaseController {
    @Autowired
    IHonganAdminService adminService;
    @Autowired
    IUserService<HonganAdmin> userService;
    @Autowired
    IHonganRoleService roleService;

    @ApiOperation("分页查询")
    @GetMapping
    @PreAuthorize("hasPermission('security-admin', 'query', '系统管理-员工管理-查询员工列表')")
    public BaseResponse queryPage(Authentication auth, QueryAdmin query) throws BaseException {
        TokenUser user = (TokenUser) auth.getPrincipal();
        IPage<HonganAdmin> list = adminService.queryPage(query);
        Pagination pagination = new Pagination(list.getCurrent(), list.getSize(), list.getTotal());
        return new BaseResponse().success(list.getRecords()).pagination(pagination);
    }

    @ApiOperation("分页查询")
    @GetMapping("/list")
    public BaseResponse queryPageList(Authentication auth, QueryAdmin query) throws BaseException {
        TokenUser user = (TokenUser) auth.getPrincipal();
        IPage<HonganAdmin> list = adminService.queryPage(query);
        Pagination pagination = new Pagination(list.getCurrent(), list.getSize(), list.getTotal());
        return new BaseResponse().success(list.getRecords()).pagination(pagination);
    }

    @ApiOperation("查询下拉数据")
    @GetMapping("/select")
    public BaseResponse roles(Authentication auth, QueryAdmin query) throws BaseException {
        TokenUser user = (TokenUser) auth.getPrincipal();
        List<TreeDataVo> list = adminService.querySelectList(query);
        return new BaseResponse().success(list);
    }

    @ApiOperation("查询详情")
    @GetMapping("/one")
    @PreAuthorize("hasPermission('security-admin', 'detail', '系统管理-员工管理-查询员工详情')")
    public BaseResponse getById(@NotNull @Min(1) Long id) {
        HonganAdmin honganAdmin = adminService.getById(id);
        return new BaseResponse().success(honganAdmin);
    }

    @ApiOperation("新增员工")
    @PostMapping
    @PreAuthorize("hasPermission('security-admin', 'insert', '系统管理-员工管理-新增员工')")
    public BaseResponse insert(Authentication auth, @Validated @RequestBody HonganAdmin admin) throws Exception {
        TokenUser user = (TokenUser) auth.getPrincipal();
        String password = adminService.insert(admin);
        return new BaseResponse().success(password);
    }

//    final FindByIndexNameSessionRepository<? extends Session> sessionRepository;
//
//    final RedisOperationsSessionRepository redisOperationsSessionRepository;

    @ApiOperation("修改员工")
    @PutMapping
    @PreAuthorize("hasPermission('security-admin', 'update', '系统管理-员工管理-修改员工')")
    public BaseResponse update(Authentication auth, @Validated @RequestBody HonganAdmin admin) throws Exception {
        TokenUser user = (TokenUser) auth.getPrincipal();
        adminService.update(admin);
        if (admin.getStatus() == AdminStatus.NORMAL) {
            //员工下线
//            //将用户踢下线
//            Map<String, ? extends Session> userSessions = sessionRepository.findByIndexNameAndIndexValue(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME, admin.getPhone());
//            // 移除用户的 session 信息
//            List<String> sessionIds = new ArrayList<>(userSessions.keySet());
//            for (String session : sessionIds) {
//                redisOperationsSessionRepository.deleteById(session);
//            }
        }
        return new BaseResponse().success();
    }

    @ApiOperation("启用/禁用员工账号")
    @PutMapping("/enable")
    @PreAuthorize("hasPermission('security-admin', 'enable',  '系统管理-员工管理-启用/禁用员工账号')")
    public BaseResponse enable(Authentication auth, @Validated @RequestBody Id id) throws BaseException {
        TokenUser user = (TokenUser) auth.getPrincipal();
        HonganAdmin admin = adminService.updateStatus(id.getId());
        return new BaseResponse().success();
    }

    @ApiOperation("删除单个员工账号")
    @DeleteMapping
    @PreAuthorize("hasPermission('security-admin', 'delete','系统管理-员工管理-单个删除')")
    public BaseResponse deleteById(Authentication auth, @RequestBody Id id) throws Exception {
        TokenUser user = (TokenUser) auth.getPrincipal();
        adminService.delete(id.getId());
        return new BaseResponse().success();
    }


    @ApiOperation("重置密码")
    @PutMapping("/reset")
    @PreAuthorize("hasPermission('security-admin', 'resetPassword', '系统管理-员工管理-重置密码')")
    public BaseResponse resetPassword(Authentication auth, @RequestBody HonganAdmin admin) throws BaseException {
        TokenUser user = (TokenUser) auth.getPrincipal();
        String password = adminService.resetPassword(admin);
        return new BaseResponse().success(password);
    }

}
