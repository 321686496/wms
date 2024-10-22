package com.hongan.template;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.wms.entity.HonganWmsReportOrder;
import com.hongan.template.wms.query.QueryReportOrder;
import com.hongan.template.wms.service.IHonganWmsMaterialService;
import com.hongan.template.wms.service.IHonganWmsReportOrderItemService;
import com.hongan.template.wms.service.IHonganWmsReportOrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.RestController;

@SpringBootTest(classes= BaseApplication.class)
public class TestReportOrder {

    @Autowired
    private IHonganWmsReportOrderService reportOrderService;

    @Autowired
    private IHonganWmsReportOrderItemService reportOrderItemService;

    @Test
    void testOrderList() throws BaseException {
        QueryReportOrder queryReportOrder = new QueryReportOrder();
        IPage<HonganWmsReportOrder> honganWmsReportOrderIPage = reportOrderService.queryPage(queryReportOrder);
        honganWmsReportOrderIPage.getRecords().forEach(System.out::println);
    }
}
