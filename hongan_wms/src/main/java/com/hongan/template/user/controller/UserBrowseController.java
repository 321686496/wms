package com.hongan.template.user.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hongan.template.base.entity.*;
import com.hongan.template.security.common.TokenUser;
import com.hongan.template.user.entity.HonganUserBrowse;
import com.hongan.template.user.query.QueryBrowse;
import com.hongan.template.user.service.IHonganUserBrowseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户浏览记录表 前端控制器
 * </p>
 *
 * @author Administrator
 * @since 2023-04-19
 */
@Api(tags = "用户端-浏览记录")
@RestController
@RequestMapping("/user/user/browse")
public class UserBrowseController extends BaseController {
    @Autowired
    private IHonganUserBrowseService service;

    @ApiOperation("分页查询浏览记录")
    @GetMapping
    public BaseResponse queryPage(Authentication auth, QueryBrowse query) throws BaseException {
        TokenUser user = (TokenUser) auth.getPrincipal();
        query.setUserId(user.getId());
        IPage<HonganUserBrowse> page = service.queryPage(query);
        Pagination pagination = new Pagination(page.getCurrent(), page.getSize(), page.getTotal());
        return new BaseResponse().success(page.getRecords()).pagination(pagination);
    }

    @ApiOperation("保存浏览记录 id为商品Id")
    @PostMapping
    public BaseResponse save(Authentication auth, @RequestBody Id id) throws BaseException {
        TokenUser user = (TokenUser) auth.getPrincipal();
        service.saveBrowse(user.getId(), id.getId());
        return new BaseResponse().success();
    }

    @ApiOperation("批量删除浏览记录")
    @DeleteMapping
    public BaseResponse delete(Authentication auth, @RequestBody Id id) throws BaseException {
        TokenUser user = (TokenUser) auth.getPrincipal();
        service.delete(user.getId(), id.getIds());
        return new BaseResponse().success();
    }


}
