package com.hongan.template.wms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.security.common.TokenUser;
import com.hongan.template.wms.entity.HonganWmsReportOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hongan.template.wms.query.QueryReportOrder;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 报货单表 服务类
 * </p>
 *
 * @author Administrator
 * @since 2024-06-25
 */
public interface IHonganWmsReportOrderService extends IService<HonganWmsReportOrder> {
    IPage<HonganWmsReportOrder> queryPage(QueryReportOrder query) throws BaseException;

    Map<String, Object> queryStatisticsData(QueryReportOrder query);

    List<HonganWmsReportOrder> queryListExport(QueryReportOrder query) throws BaseException;

    HonganWmsReportOrder selectById(Long id) throws BaseException;

    HonganWmsReportOrder selectDetailById(Long id) throws BaseException;

    Map<String, Object> selectMergeDetailById(Long id) throws BaseException;

    String getBillCode();

    //保存/修改单据
    Long saveBill(HonganWmsReportOrder data, TokenUser user) throws BaseException;

//    //保存/修改并提交单据
//    Long saveAndSubmitBill(HonganWmsReportOrder data, TokenUser user) throws BaseException;
//
//    //审核单据
//    void auditBill(VerifyVo data, TokenUser user) throws BaseException;
//
//    //弃审单据
//    void abandonAuditBill(Long tenantId, Long id, TokenUser user) throws BaseException;

    //删除单据
    void deleteBill(Long id) throws BaseException;

    Long mergeReportOrder(List<Long> ids, TokenUser user) throws BaseException;

    String getPdf(Long id, String language) throws BaseException;
}
