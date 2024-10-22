package com.hongan.template.admin.controller;

import com.hongan.template.admin.entity.HonganMenu;
import com.hongan.template.admin.service.IHonganAdminService;
import com.hongan.template.admin.service.IHonganMenuService;
import com.hongan.template.base.captcha.PhoneServiceCaptcha;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.base.entity.BaseResponse;
import com.hongan.template.base.security.CurrentController;
import com.hongan.template.base.security.IUserService;
import com.hongan.template.security.common.TokenUser;
import com.hongan.template.admin.entity.HonganAdmin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * @CreateTime: 2020-02-31 17:43
 * @Description: CurrentAdminController
 * @Author: zhanxd
 * @Version:
 */

@Slf4j
@Api(tags = "管理端-个人信息")
@RestController
@RequestMapping("/admin/current")
public class AdminCurrentController extends CurrentController<HonganAdmin> {
    @Autowired
    private IHonganMenuService menuService;
    @Autowired
    IHonganAdminService adminService;

    @Autowired
    IUserService<HonganAdmin> userService;

    @Autowired
    public AdminCurrentController(IUserService<HonganAdmin> userService, PhoneServiceCaptcha phoneServiceCaptcha) {
        this.userService = userService;
        this.phoneServiceCaptcha = phoneServiceCaptcha;
    }

//    @GetMapping("/currentAdmin")
//    @ApiOperation("获取登录用户信息")
//    public BaseResponse Current(Authentication auth) {
//        TokenUser tokenUser = (TokenUser) auth.getPrincipal();
//        PonlayAdmin ponlayAdmin = adminService.getById(tokenUser.getId());
//        System.out.println("登录admin：" + ponlayAdmin.toString());
////        List<PonlayMenu> ponlayMenuRole = menuService.getMenuRole(ponlayAdmin.getRoleKey());
////        ponlayAdmin.setMenuList(ponlayMenuRole);
//        return new BaseResponse().success(ponlayAdmin);
//    }

    @ApiOperation(value = "获取个人概要信息")
    @GetMapping("/current")
    public BaseResponse adminCurrent(Authentication auth) {
        TokenUser user = (TokenUser) auth.getPrincipal();
        HonganAdmin admin = adminService.getById(user.getId());
        return new BaseResponse().success(admin);
    }

    @GetMapping("/detail")
    @ApiOperation(value = "获取个人详情信息")
    public BaseResponse adminDetail(Authentication auth) throws BaseException {
        TokenUser user = (TokenUser) auth.getPrincipal();
        HonganAdmin admin = adminService.getDetailById(user.getId());
        return new BaseResponse().success(admin);
    }

    @PutMapping("/updatePassword")
    @ApiOperation(value = "修改密码")
    public BaseResponse adminUpdatePassword(Authentication auth, HttpServletRequest request, @RequestBody UpdatePassword update) throws BaseException {
        this.updatePassword(auth, update);
        //清除登陆状态
        request.getSession().invalidate();
        return new BaseResponse().success();
    }

    @PutMapping("/updateMessage")
    @ApiOperation(value = "修改信息")
    public BaseResponse updateMessage(Authentication auth, HttpServletRequest request, @RequestBody HonganAdmin admin) throws BaseException {
        TokenUser user = (TokenUser) auth.getPrincipal();
        admin.setName(user.getName());
        admin.setId(user.getId());
        admin.setPassword(null);
        admin.setRoleId(null);
        admin.setDeptId(null);
        adminService.updateAdmin(admin);
        return new BaseResponse().success();
    }

    // 角色登录后自动获取自己的菜单
    @ApiOperation(value = "获取菜单列表")
    @GetMapping("/role")
    public BaseResponse getMenuTreeByRole(Authentication auth) throws BaseException {
        TokenUser user = (TokenUser) auth.getPrincipal();
//        long startTime = System.currentTimeMillis();
        List<HonganMenu> list = menuService.getMenuTree(user.getRoleKey());
//        long endTime = System.currentTimeMillis();
//        long executionTime = endTime - startTime;
//        System.out.println("运行时长：" + executionTime + "毫秒");
        return new BaseResponse().success(list);
    }

}
