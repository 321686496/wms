package com.hongan.template.batch;

import com.hongan.template.BaseApplication;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.security.common.TokenUser;
import com.hongan.template.wms.entity.HonganWmsMaterial;
import com.hongan.template.wms.entity.HonganWmsReportOrder;
import com.hongan.template.wms.entity.HonganWmsReportOrderItem;
import com.hongan.template.wms.service.IHonganWmsMaterialService;
import com.hongan.template.wms.service.IHonganWmsReportOrderService;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes= BaseApplication.class)
public class TestReportOrderBatch {

//    @Autowired
//    private

    @Autowired
    private IHonganWmsReportOrderService reportOrderService;

    @Autowired
    private IHonganWmsMaterialService materialService;

    @Test
    void testBatchAdd(){
        batchAdd(200);
    }

    @Test
    void testBatchMergeOrder() throws BaseException {
        List<Long> ids = new ArrayList<>();
        for (long i = 200; i < 250; i++) {
            ids.add(i);
        }
        TokenUser tokenUser = new TokenUser();
        tokenUser.setId(1l);
        tokenUser.setRealName("admin");
        reportOrderService.mergeReportOrder(ids,tokenUser);
    }


    void batchAdd(int count){
        for(int i=0;i<count;i++){
            try {
                add();
            } catch (BaseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    void add() throws BaseException {
        // 获取所有商品
        List<HonganWmsMaterial> list = materialService.list();
//        Random random = new Random();

        // 随机的商品个数（0~商品个数）
        int randomCount = RandomUtils.nextInt(1, list.size());
        // 随机商品选择
        List<HonganWmsMaterial> materialList = new ArrayList<>();
        for (int i = 0; i < randomCount; i++) {
            while(true){
                HonganWmsMaterial honganWmsMaterial = list.get(RandomUtils.nextInt(0,list.size()));
                // 判断改物料是否已存在
                if(materialList.stream().noneMatch(item->item.getId().equals(honganWmsMaterial.getId()))){
                    materialList.add(honganWmsMaterial);
                    break;
                }
            }
        }
        // 添加订单
        List<HonganWmsReportOrderItem> honganWmsReportOrderItems = new ArrayList<>();
        for (HonganWmsMaterial honganWmsMaterial : materialList) {
            // 随机的商品库存量（1~1000）
            long randomStockNumber = RandomUtils.nextInt(1, 1000);
            // 随机的商品报货量（1~1000）
            long randomReportNumber = RandomUtils.nextInt(1, 1000);
            // 填写数据
            HonganWmsReportOrderItem honganWmsReportOrderItem = new HonganWmsReportOrderItem();
            honganWmsReportOrderItem.setMaterialId(honganWmsMaterial.getId());
            honganWmsReportOrderItem.setStockNumber(BigDecimal.valueOf(randomStockNumber));
            honganWmsReportOrderItem.setReportNumber(BigDecimal.valueOf(randomReportNumber));
            honganWmsReportOrderItems.add(honganWmsReportOrderItem);
        }
        HonganWmsReportOrder honganWmsReportOrder = new HonganWmsReportOrder();
        honganWmsReportOrder.setItemList(honganWmsReportOrderItems);
        honganWmsReportOrder.setBillDate(LocalDate.now());
        TokenUser tokenUser = new TokenUser();
        tokenUser.setId(1l);
        reportOrderService.saveBill(honganWmsReportOrder,tokenUser);
    }




    // 登录
    void login(){

    }
}
