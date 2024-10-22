package com.hongan.template.user.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hongan.template.base.entity.*;
import com.hongan.template.security.common.TokenUser;
import com.hongan.template.user.entity.HonganUserAddress;
import com.hongan.template.user.query.QueryAddress;
import com.hongan.template.user.service.IHonganUserAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 用户地址表 前端控制器
 * </p>
 *
 * @author Administrator
 * @since 2023-04-03
 */
@Api(tags = "用户端-地址管理")
@RestController
@RequestMapping("/user/user/address")
public class UserAddressController extends BaseController {
    @Autowired
    private IHonganUserAddressService addressService;

    @ApiOperation("分页查询用户地址")
    @GetMapping
    public BaseResponse queryPage(Authentication auth, QueryAddress query) {
        TokenUser user = (TokenUser) auth.getPrincipal();
        query.setUserId(user.getId());
        IPage<HonganUserAddress> page = addressService.queryPage(query);
        Pagination pagination = new Pagination(page.getCurrent(), page.getSize(), page.getTotal());
        return new BaseResponse().success(page.getRecords()).pagination(pagination);
    }

    @ApiOperation("查询用户地址列表")
    @GetMapping("/list")
    public BaseResponse selectAddress(Authentication auth, QueryAddress query) {
        TokenUser user = (TokenUser) auth.getPrincipal();
        query.setUserId(user.getId());
        List<HonganUserAddress> list = addressService.queryList(query);
        return new BaseResponse().success(list);
    }

    @ApiOperation("用户新增地址")
    @PostMapping
    public BaseResponse create(Authentication auth, @RequestBody HonganUserAddress address) throws BaseException {
        TokenUser user = (TokenUser) auth.getPrincipal();
        address.setUserId(user.getId());
        addressService.create(address);
        return new BaseResponse().success();
    }

    @ApiOperation("用户修改地址")
    @PutMapping
    public BaseResponse update(Authentication auth, @RequestBody HonganUserAddress address) throws BaseException {
        TokenUser user = (TokenUser) auth.getPrincipal();
        address.setUserId(user.getId());
        addressService.update(address);
        return new BaseResponse().success();
    }

    @ApiOperation("根据Id查询地址具体信息")
    @GetMapping("/one")
    public BaseResponse selectOneById(Authentication auth, @NotNull Long id) throws BaseException {
        TokenUser user = (TokenUser) auth.getPrincipal();
        HonganUserAddress HonganUserAddress = addressService.selectById(id, user.getId());
        return new BaseResponse().success(HonganUserAddress);
    }

    @ApiOperation("设为默认地址")
    @PutMapping("/default")
    public BaseResponse setDefault(Authentication auth, @RequestBody Id id) throws BaseException {
        TokenUser user = (TokenUser) auth.getPrincipal();
        addressService.setDefault(user.getId(), id.getId());
        return new BaseResponse().success();
    }

    @ApiOperation("查询默认地址")
    @GetMapping("/getDefault")
    public BaseResponse getDefault(Authentication auth) throws BaseException {
        TokenUser user = (TokenUser) auth.getPrincipal();
        HonganUserAddress address = addressService.getDefault(user.getId());
        return new BaseResponse().success(address);
    }

    @ApiOperation("根据Id删除地址信息")
    @DeleteMapping
    public BaseResponse deleteById(Authentication auth, @RequestBody Id id) throws BaseException {
        TokenUser user = (TokenUser) auth.getPrincipal();
        addressService.deleteById(id.getId(), user.getId());
        return new BaseResponse().success();
    }
}
