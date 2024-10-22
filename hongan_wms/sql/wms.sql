DROP TABLE IF EXISTS `hongan_wms_report_order_item`;
DROP TABLE IF EXISTS `hongan_wms_material`;
DROP TABLE IF EXISTS `hongan_wms_material_type`;
CREATE TABLE `hongan_wms_material_type`
(
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `classify_name` varchar(255) NOT NULL unique COMMENT '分类名称',
    `classify_name_fr` varchar(255) NOT NULL unique COMMENT '分类名称【法语】',
    `created_at` timestamp        NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` timestamp        NULL     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`    tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除标识 1：删除 0：未删除',
    primary key (id) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '物料类别表'
  ROW_FORMAT = Dynamic;
# COLLATE = utf8mb4_0900_ai_ci COMMENT = '物料类别表'

INSERT INTO `hongan_wms_material_type`(classify_name,classify_name_fr) values ('未知','Inconnu');


DROP TABLE IF EXISTS `hongan_wms_material`;
CREATE TABLE `hongan_wms_material`
(
    `id`         bigint           NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `name`       varchar(255)     NOT NULL DEFAULT '' COMMENT '名称',
    `code`       varchar(255)     NOT NULL DEFAULT '' COMMENT '编码',
    `type_id`    bigint             DEFAULT 1 COMMENT '产品类型',
    `enable`     tinyint          NOT NULL DEFAULT 1 COMMENT '启用标识（true:正常 false:停用）',
    `remark`     varchar(255)     NULL     DEFAULT '' COMMENT '备注',
    `priority`   int              NOT NULL DEFAULT 0 COMMENT '优先级',
    `created_at` timestamp        NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` timestamp        NULL     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`    tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除标识 1：删除 0：未删除',
    constraint fk_material_type_id foreign key (type_id) references hongan_wms_material_type(id) on update cascade on delete set null,
    primary key (id) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '物料表'
  ROW_FORMAT = Dynamic;
# COLLATE = utf8mb4_0900_ai_ci COMMENT = '物料表'

# 为原hongan_wms_material表添加type_id字段
# alter table hongan_wms_material add type_id bigint default 1 comment '产品类别';
# ALTER TABLE hongan_wms_material ADD constraint fk_material_type_id foreign key (type_id) references hongan_wms_material_type(id) on update cascade on delete set null;

# DROP TABLE IF EXISTS `hongan_wms_material_type_classify`;
# CREATE TABLE `hongan_wms_material_type_classify`
# (
#    `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
#    `classify_id` bigint NOT NULL COMMENT '分类Id',
#    `material_id` bigint NOT NULL COMMENT '物料ID',
#    `created_at` timestamp        NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
#    `updated_at` timestamp        NULL     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
#    `deleted`    tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除标识 1：删除 0：未删除',
#    constraint fk_mtc_cid foreign key (classify_id) references hongan_wms_material_type(id),
#    constraint fk_mtc_mid foreign key (material_id) references hongan_wms_material(id),
#    primary key (id) USING BTREE
# ) ENGINE = InnoDB
#   AUTO_INCREMENT = 1
#   CHARACTER SET = utf8mb4
#   COLLATE = utf8mb4_0900_ai_ci COMMENT = '物料类别分类表'
#   ROW_FORMAT = Dynamic;



DROP TABLE IF EXISTS `hongan_wms_report_order`;
CREATE TABLE `hongan_wms_report_order`
(
    `id`                  bigint           NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `create_admin_id`     bigint           NOT NULL COMMENT '创建人Id',
    `create_admin_name`   varchar(255)     NOT NULL DEFAULT '' COMMENT '创建人名称(冗余字段)',
    `bill_date`           datetime         NULL COMMENT '单据日期',
    `bill_code`           varchar(255)     NOT NULL DEFAULT '' COMMENT '单据编号(XSDD+年月日+6位排序号)',
    `status`              varchar(100)     NOT NULL DEFAULT '' COMMENT '状态',
    `is_merge`            tinyint          NOT NULL DEFAULT 0 COMMENT '是否是合并订单',
    `stock_number_total`  decimal(20, 6)   NOT NULL DEFAULT 0 COMMENT '库存总数量',
    `report_number_total` decimal(20, 6)   NOT NULL DEFAULT 0 COMMENT '报货总数量',
    `attachment`          json             NULL COMMENT '附件',
    `remark`              varchar(255)     NOT NULL DEFAULT '' COMMENT '备注',
    `created_at`          TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`          TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`             TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除标识 1：删除 0：未删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_bill_code` (`bill_code`) USING BTREE,
    INDEX `idx_bill_date` (`bill_date`) USING BTREE
) ENGINE = INNODB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '报货单表'
  ROW_FORMAT = DYNAMIC;

DROP TABLE IF EXISTS `hongan_wms_report_order_item`;
CREATE TABLE `hongan_wms_report_order_item`
(
    `id`            bigint           NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `pid`           bigint           NOT NULL COMMENT '上级单据ID',
    `material_id`   bigint           NOT NULL COMMENT '物料Id',
    `material_name` varchar(255)     NOT NULL COMMENT '物料名称(冗余字段)',
    `material_priority` int     NOT NULL DEFAULT 0 COMMENT '物料排序值',
    `material_code` varchar(255)     NOT NULL DEFAULT '' COMMENT 'Sku编码(冗余字段)',
    `type_id` bigint     not null COMMENT '物料类别(冗余字段)',
    `type_name` varchar(255) COMMENT '类别名称(冗余字段)',
    `type_name_fr` varchar(255) COMMENT '类别名称【法】(冗余字段)',
    `shop_name`     varchar(255)     NOT NULL DEFAULT '' COMMENT '店铺名称',
    `shop_priority`     int     NOT NULL DEFAULT 0 COMMENT '店铺排序值',
    `stock_number`  decimal(20, 6)   NOT NULL DEFAULT 0 COMMENT '库存量',
    `report_number` decimal(20, 6)   NOT NULL DEFAULT 0 COMMENT '报货量',
    `remark`        varchar(255)     NOT NULL DEFAULT '' COMMENT '备注',
    `created_at`    TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`    TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`       TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除标识 1：删除 0：未删除',
    constraint fk_roi_mid foreign key (material_id) references hongan_wms_material(id) on update cascade on delete cascade ,
    constraint fk_roi_tid foreign key (type_id) references hongan_wms_material_type(id) on update cascade on delete cascade ,
    constraint fk_roi_tname foreign key (type_name) references hongan_wms_material_type(classify_name) on update cascade on delete cascade ,
    constraint fk_roi_tname_fr foreign key (type_name_fr) references hongan_wms_material_type(classify_name_fr) on update cascade on delete cascade ,
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_pid` (`pid`) USING BTREE
) ENGINE = INNODB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '报货单明细表'
  ROW_FORMAT = DYNAMIC;

# 创建触发器（每当物料表的type_id字段发生变化，则修改hongan_wms_report_order_item表的type_id字段与type_name字段）
drop trigger if exists trigger_hwroi_type_id_update;

CREATE TRIGGER trigger_hwroi_type_id_update
    AFTER UPDATE ON hongan_wms_material
    FOR EACH ROW
BEGIN
    DECLARE mt_id BIGINT;
    DECLARE t_name VARCHAR(255);
    DECLARE t_name_fr VARCHAR(255);

    SET mt_id = NEW.type_id;
    SET t_name = (SELECT classify_name FROM hongan_wms_material_type WHERE id = mt_id);
    SET t_name_fr = (SELECT classify_name_fr FROM hongan_wms_material_type WHERE id = mt_id);
#     SET t_name = (SELECT classify_name, classify_name_fr INTO t_name,t_name_fr FROM hongan_wms_material_type WHERE id = mt_id);

    UPDATE hongan_wms_report_order_item
    SET
        type_id = mt_id,
        type_name = t_name,
        type_name_fr = t_name_fr
    WHERE
        material_id = NEW.id;
END

