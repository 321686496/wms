package com.hongan.template.wms.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hongan.template.base.entity.*;
import com.hongan.template.base.vo.BatchOperateResponse;
import com.hongan.template.security.common.TokenUser;
import com.hongan.template.wms.entity.HonganWmsReportOrder;
import com.hongan.template.wms.query.QueryReportOrder;
import com.hongan.template.wms.service.IHonganWmsReportOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 报货单表 前端控制器
 * </p>
 *
 * @author Administrator
 * @since 2024-06-25
 */
@Validated
@Api(tags = "报货单管理")
@RestController
@RequestMapping("/admin/wms/report/order")
public class HonganWmsReportOrderController extends BaseController {
    @Autowired
    private IHonganWmsReportOrderService service;

    @ApiOperation("分页查询")
    @GetMapping
    public BaseResponse query(Authentication auth, QueryReportOrder query) throws BaseException, BaseException {
        TokenUser user = (TokenUser) auth.getPrincipal();
        if (!user.getRole().equals("SUPER")) {
            query.setCreateAdminId(user.getId());
        }
        IPage<HonganWmsReportOrder> page = service.queryPage(query);
        Pagination pagination = new Pagination(page.getCurrent(), page.getSize(), page.getTotal());
        return new BaseResponse().success(page.getRecords()).pagination(pagination);
    }

    @ApiOperation("统计汇总")
    @GetMapping("/statistics")
    public BaseResponse queryStatisticsData(Authentication auth, QueryReportOrder query) throws BaseException {
        TokenUser user = (TokenUser) auth.getPrincipal();
        Map<String, Object> res = service.queryStatisticsData(query);
        return new BaseResponse().success(res);
    }

    @ApiOperation("查询详情")
    @GetMapping("/one")
    public BaseResponse queryDetail(Authentication auth, @NotNull @Min(1) Long id) throws BaseException {
        TokenUser user = (TokenUser) auth.getPrincipal();
        HonganWmsReportOrder data = service.selectById(id);
        return new BaseResponse().success(data);
    }

    @ApiOperation("查询合并订单详情")
    @GetMapping("/one/merge")
    public BaseResponse queryMergeDetail(Authentication auth, @NotNull @Min(1) Long id) throws BaseException {
        TokenUser user = (TokenUser) auth.getPrincipal();
        Map<String, Object> data = service.selectMergeDetailById(id);
        return new BaseResponse().success(data);
    }

    @ApiOperation("查询PDF")
    @GetMapping("/pdf")
    public BaseResponse getPdf(Authentication auth, @NotNull @Min(1) Long id, String language) throws BaseException {
        TokenUser user = (TokenUser) auth.getPrincipal();
        String data = service.getPdf(id, language);
        return new BaseResponse().success(data);
    }

    @ApiOperation("保存")
    @PostMapping
    public BaseResponse save(Authentication auth, @RequestBody HonganWmsReportOrder data) throws BaseException {
        TokenUser user = (TokenUser) auth.getPrincipal();
        if (data.getId() == null) {
            data.setCreateAdminId(user.getId());
            data.setCreateAdminName(user.getRealName());
        }
        Long res = service.saveBill(data, user);
        return new BaseResponse().success(res);
    }

    @ApiOperation("订单合并")
    @PutMapping("/merge")
    public BaseResponse merge(Authentication auth, @RequestBody Id id) throws BaseException {
        TokenUser user = (TokenUser) auth.getPrincipal();
        Long res = service.mergeReportOrder(id.getIds(), user);
        return new BaseResponse().success(res);
    }

//
//    @ApiOperation("审核")
//    @PutMapping("/audit")
//    @PreAuthorize("hasPermission('stock-instockOther', 'audit', '库存管理-其他入库单管理-审核')")
//    public BaseResponse auditBill(Authentication auth, @RequestBody VerifyVo vo) throws BaseException {
//        TokenUser user = (TokenUser) auth.getPrincipal();
//        vo.setTenantId(user.getTenantId());
//        service.auditBill(vo, user);
//        return new BaseResponse().success();
//    }
//
//    @ApiOperation("批量审核")
//    @PutMapping("/audit/batch")
//    @PreAuthorize("hasPermission('stock-instockOther', 'batchAudit', '库存管理-其他入库单管理-批量审核')")
//    public BaseResponse batchAuditBill(Authentication auth, @RequestBody VerifyVo vo) throws BaseException {
//        TokenUser user = (TokenUser) auth.getPrincipal();
//        vo.setTenantId(user.getTenantId());
//        Integer successCount = 0;
//        Integer errorCount = 0;
//        List<Map<String, Object>> errList = new ArrayList<>();
//        for (Long id : vo.getIds()) {
//            try {
//                vo.setId(id);
//                service.auditBill(vo, user);
//                successCount++;
//            } catch (Exception e) {
//                e.printStackTrace();
//                HonganWmsReportOrder bill = service.selectById(user.getTenantId(), id);
//                errorCount++;
//                errList.add(new HashMap<>() {{
//                    put("data", bill.getBillCode());
//                    put("errorMsg", e.getMessage());
//                }});
//            }
//        }
//        return new BaseResponse().success(new BatchOperateResponse(vo.getIds().size(), successCount, errorCount, errList));
//    }
//
//    @ApiOperation("弃审")
//    @PutMapping("/abandonAudit")
//    @PreAuthorize("hasPermission('stock-instockOther', 'abandonAudit', '库存管理-其他入库单管理-弃审')")
//    public BaseResponse abandonAuditBill(Authentication auth, @RequestBody Id data) throws BaseException {
//        TokenUser user = (TokenUser) auth.getPrincipal();
//        service.abandonAuditBill(user.getTenantId(), data.getId(), user);
//        return new BaseResponse().success();
//    }
//
//    @ApiOperation("批量弃审")
//    @DeleteMapping("/abandonAudit/batch")
//    @PreAuthorize("hasPermission('stock-instockOther', 'batchAbandonAudit', '库存管理-其他入库单管理-批量弃审')")
//    public BaseResponse batchAbandonAuditBill(Authentication auth, @NotNull @RequestBody Id data) throws BaseException {
//        TokenUser user = (TokenUser) auth.getPrincipal();
//        Integer successCount = 0;
//        Integer errorCount = 0;
//        List<Map<String, Object>> errList = new ArrayList<>();
//        for (Long id : data.getIds()) {
//            try {
//                service.abandonAuditBill(user.getTenantId(), id, user);
//                successCount++;
//            } catch (Exception e) {
//                e.printStackTrace();
//                HonganWmsReportOrder bill = service.selectById(user.getTenantId(), id);
//                errorCount++;
//                errList.add(new HashMap<>() {{
//                    put("data", bill.getBillCode());
//                    put("errorMsg", e.getMessage());
//                }});
//            }
//        }
//        return new BaseResponse().success(new BatchOperateResponse(data.getIds().size(), successCount, errorCount, errList));
//    }

    @ApiOperation("删除")
    @DeleteMapping
    public BaseResponse deleteBill(Authentication auth, @RequestBody Id data) throws BaseException {
        TokenUser user = (TokenUser) auth.getPrincipal();
        service.deleteBill(data.getId());
        return new BaseResponse().success();
    }

    @ApiOperation("批量删除")
    @DeleteMapping("/batch")
    public BaseResponse batchDelete(Authentication auth, @NotNull @RequestBody Id data) throws BaseException {
        TokenUser user = (TokenUser) auth.getPrincipal();
        Integer successCount = 0;
        Integer errorCount = 0;
        List<Map<String, Object>> errList = new ArrayList<>();
        for (Long id : data.getIds()) {
            try {
                service.deleteBill(id);
                successCount++;
            } catch (Exception e) {
                e.printStackTrace();
                HonganWmsReportOrder bill = service.selectById(id);
                errorCount++;
                errList.add(new HashMap<>() {{
                    put("data", bill.getBillCode());
                    put("errorMsg", e.getMessage());
                }});
            }
        }
        return new BaseResponse().success(new BatchOperateResponse(data.getIds().size(), successCount, errorCount, errList));
    }
}
