alter table `hongan_admin`
    add `priority` int NOT NULL DEFAULT 0 COMMENT '优先级';

alter table `hongan_wms_report_order_item`
    add `shop_priority` int NOT NULL DEFAULT 0 COMMENT '店铺排序值';
alter table `hongan_wms_report_order_item`
    add `material_priority` int NOT NULL DEFAULT 0 COMMENT '商品排序值';