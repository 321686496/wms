use hongan_wms_db;


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

INSERT INTO `hongan_wms_material_type`(classify_name,classify_name_fr) values ('未知','Inconnu');

-- 为物品添加物品类型id并设置外键约束
ALTER TABLE `hongan_wms_material` ADD COLUMN `type_id` bigint DEFAULT 1 COMMENT '产品类别';
ALTER TABLE `hongan_wms_material` ADD CONSTRAINT fk_material_type_id FOREIGN KEY (`type_id`) REFERENCES `hongan_wms_material_type`(`id`) ON UPDATE CASCADE ON DELETE SET NULL;


-- 为hongan_wms_report_order_item表添加类别名称、类别编号、类别名称法冗余字段
ALTER TABLE `hongan_wms_report_order_item` ADD COLUMN `type_id` bigint not null COMMENT '类别编号(冗余字段)';
ALTER TABLE `hongan_wms_report_order_item` ADD COLUMN `type_name` varchar(255) COMMENT '类别名称(冗余字段)';
ALTER TABLE `hongan_wms_report_order_item` ADD COLUMN `type_name_fr` varchar(255) COMMENT '类别名称【法】(冗余字段)';
alter table `hongan_wms_report_order_item` add constraint fk_roi_tid foreign key (type_id) references hongan_wms_material_type(id) on update cascade on delete cascade;
alter table `hongan_wms_report_order_item` add constraint fk_roi_tname foreign key (type_name) references hongan_wms_material_type(classify_name) on update cascade on delete cascade;
alter table `hongan_wms_report_order_item` add constraint fk_roi_tname_fr foreign key (type_name_fr) references hongan_wms_material_type(classify_name_fr) on update cascade on delete cascade;

update hongan_wms_report_order_item set
    type_id = (select id from hongan_wms_material_type where id=(select type_id from hongan_wms_material hwm where hwm.id=material_id)),
    type_name = (select classify_name from hongan_wms_material_type where id=(select type_id from hongan_wms_material hwm where hwm.id=material_id)),
    type_name_fr = (select classify_name_fr from hongan_wms_material_type where id=(select type_id from hongan_wms_material hwm where hwm.id=material_id))
;


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
END;
