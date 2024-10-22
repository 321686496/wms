package com.hongan.template.user.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hongan.template.base.entity.*;
import com.hongan.template.enums.SystemGoodsType;
import com.hongan.template.security.common.TokenUser;
import com.hongan.template.user.entity.HonganUserCollection;
import com.hongan.template.user.query.QueryCollection;
import com.hongan.template.user.service.IHonganUserCollectionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 用户收藏表 前端控制器
 * </p>
 *
 * @author Administrator
 * @since 2023-04-03
 */
@Validated
@Api(tags = "用户端-收藏商品")
@RestController
@RequestMapping("/user/user/collection")
public class UserUserController extends BaseController {
    @Autowired
    private IHonganUserCollectionService collectionService;

    @ApiOperation("分页查询")
    @GetMapping
    public BaseResponse queryPage(Authentication auth, QueryCollection query) throws BaseException {
        TokenUser user = (TokenUser) auth.getPrincipal();
        query.setUserId(user.getId());
        IPage<HonganUserCollection> page = collectionService.queryPage(query);
        Pagination pagination = new Pagination(page.getCurrent(), page.getSize(), page.getTotal());
        return new BaseResponse().success(page.getRecords()).pagination(pagination);
    }

    @ApiOperation("收藏")
    @PostMapping
    public BaseResponse likePosts(Authentication auth, @NotNull @RequestBody HonganUserCollection data) throws BaseException {
        if (data.getType() == null || data.getActionId() == null)
            throw new BaseException(BaseError.BadRequest);
        TokenUser user = (TokenUser) auth.getPrincipal();
        data.setUserId(user.getId());
        Boolean res = collectionService.collection(data.getType(), data.getUserId(), data.getActionId());
        return new BaseResponse().success(res);
    }

    @ApiOperation("判断是否收藏")
    @GetMapping("/check")
    public BaseResponse checkLikePosts(Authentication auth, SystemGoodsType type, @NotNull @Min(1) Long actionId) throws BaseException {
        if (actionId == null)
            throw new BaseException(BaseError.BadRequest);
        TokenUser user = (TokenUser) auth.getPrincipal();
        Boolean res = collectionService.checkCollection(type, user.getId(), actionId);
        return new BaseResponse().success(res);
    }

    @ApiOperation("批量删除收藏夹")
    @DeleteMapping
    public BaseResponse delete(Authentication auth, @RequestBody Id id) throws BaseException {
        TokenUser user = (TokenUser) auth.getPrincipal();
        collectionService.delete(user.getId(), id.getIds());
        return new BaseResponse().success();
    }

}
