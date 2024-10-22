package com.hongan.template.wms.controller;


import com.hongan.template.base.entity.BaseException;
import com.hongan.template.base.entity.BaseResponse;
import com.hongan.template.wms.entity.HonganWmsReportOrderItem;
import com.hongan.template.wms.query.QueryReportOrderItem;
import com.hongan.template.wms.service.IHonganWmsReportOrderItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.hongan.template.base.entity.BaseController;

import java.util.List;

/**
 * <p>
 * 报货单明细表 前端控制器
 * </p>
 *
 * @author Administrator
 * @since 2024-06-25
 */
@Validated
@Api(tags = "报货单明细")
@RestController
@RequestMapping("/admin/wms/report/order/item")
public class HonganWmsReportOrderItemController extends BaseController {
    @Autowired
    private IHonganWmsReportOrderItemService itemService;

    @ApiOperation("查询明细列表")
    @GetMapping
    public BaseResponse queryList(QueryReportOrderItem query) throws BaseException {
        List<HonganWmsReportOrderItem> list = itemService.queryList(query);
        return new BaseResponse().success(list);
    }

}
