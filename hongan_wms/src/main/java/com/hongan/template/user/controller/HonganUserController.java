package com.hongan.template.user.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hongan.template.base.entity.*;
import com.hongan.template.user.entity.HonganUser;
import com.hongan.template.user.query.QueryUser;
import com.hongan.template.user.service.IHonganUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author admin
 * @since 2020-07-30
 */
@Api(tags = "管理端-用户管理")
@RestController
@Slf4j
@RequestMapping("/admin/user/user")
public class HonganUserController extends BaseController {

    @Autowired
    IHonganUserService userService;

    @ApiOperation("分页查询用户列表")
    @GetMapping
    public BaseResponse queryPage(QueryUser query) {
        IPage<HonganUser> list = userService.queryPage(query);
        Pagination pagination = new Pagination(list.getCurrent(), list.getSize(), list.getTotal());
        return new BaseResponse().success(list.getRecords()).pagination(pagination);
    }

    @ApiOperation("根据id查询单个用户信息")
    @GetMapping("/one")
    public BaseResponse getUser(@NotNull @Min(1) Long id) throws BaseException {
        HonganUser user = userService.getDetailById(id);
        return new BaseResponse().success(user);
    }

    @ApiOperation("修改用户状态")
    @PutMapping("/updateStatus")
    public BaseResponse getUser(@NotNull @RequestBody Id id) throws BaseException {
        userService.updateUserStatus(id.getId());
        return new BaseResponse().success();
    }
}
