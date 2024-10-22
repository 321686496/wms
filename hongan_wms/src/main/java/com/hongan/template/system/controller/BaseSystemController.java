package com.hongan.template.system.controller;


import com.hongan.template.base.entity.BaseController;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.base.entity.BaseResponse;
import com.hongan.template.system.entity.HonganSystemAgreement;
import com.hongan.template.system.entity.HonganSystemBanner;
import com.hongan.template.system.query.QueryBanner;
import com.hongan.template.system.service.IHonganSystemAgreementService;
import com.hongan.template.system.service.IHonganSystemBannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 系统轮播图表 前端控制器
 * </p>
 *
 * @author Administrator
 * @since 2024-05-10
 */
@Validated
@Api(tags = "公共接口-系统配置")
@RestController
@RequestMapping("/base/system")
public class BaseSystemController extends BaseController {
    @Autowired
    private IHonganSystemBannerService bannerService;
    @Autowired
    private IHonganSystemAgreementService agreementService;

    @ApiOperation("查询轮播图列表")
    @GetMapping("/banner")
    public BaseResponse queryPage(QueryBanner query) throws BaseException {
        query.setEnable(true);
        List<HonganSystemBanner> list = bannerService.queryList(query);
        return new BaseResponse().success(list);
    }

    @ApiOperation("查询系统协议详情")
    @GetMapping("/agreement/type")
    public BaseResponse getOne(@NotNull String type) throws BaseException {
        HonganSystemAgreement data = agreementService.selectByType(type);
        return new BaseResponse().success(data);
    }

}
