DROP TABLE IF EXISTS `hongan_config`;
CREATE TABLE `hongan_config`
(
    `id`           bigint           NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `config_name`  varchar(255)     NOT NULL DEFAULT '' COMMENT '参数名称',
    `config_key`   varchar(255)     NOT NULL DEFAULT '' COMMENT '参数键名',
    `config_value` varchar(255)     NOT NULL DEFAULT '' COMMENT '参数键值',
    `remark`       varchar(500)              default null comment '备注',
    `created_at`   timestamp        NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`   timestamp        NULL     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`      tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除标识 1：删除 0：未删除',
    primary key (id) USING BTREE,
    INDEX `idx_config_key` (`config_key`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统参数配置表'
  ROW_FORMAT = Dynamic;
