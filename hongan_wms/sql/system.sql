DROP TABLE IF EXISTS `hongan_system_config`;
CREATE TABLE `hongan_system_config`
(
    `id`                           bigint           NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_default_avatar`          varchar(255)     NOT NULL DEFAULT '' COMMENT '用户默认头像',
    `user_default_nickname_prefix` varchar(255)     NOT NULL DEFAULT '' COMMENT '用户默认昵称前缀',
    `enable_account_recharge`      tinyint          NOT NULL DEFAULT 0 COMMENT '启用账户充值(true:启用/false:禁用)',
    `enable_promote_coupon`        tinyint          NOT NULL DEFAULT 0 COMMENT '启用优惠券(营销)(true:启用/false:禁用)',
    `enable_promote_recharge`      tinyint          NOT NULL DEFAULT 0 COMMENT '启用充值套餐(营销)(true:启用/false:禁用)',
    `enable_promote_lottery`       tinyint          NOT NULL DEFAULT 0 COMMENT '启用抽奖活动(营销)(true:启用/false:禁用)',
    `created_at`                   timestamp        NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`                   timestamp        NULL     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`                      tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除标识 1：删除 0：未删除',
    primary key (id) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统配置表'
  ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `hongan_system_banner`;
CREATE TABLE `hongan_system_banner`
(
    `id`         bigint           NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `cover`      varchar(255)     NOT NULL DEFAULT '' COMMENT '图片',
    `enable`     tinyint          NOT NULL DEFAULT 1 COMMENT '启用状态 (true:启用/false:禁用)',
    `priority`   int              NOT NULL DEFAULT 0 COMMENT '排序值',
    `created_at` timestamp        NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` timestamp        NULL     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`    tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除标识 1：删除 0：未删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统轮播图表'
  ROW_FORMAT = Dynamic;


DROP TABLE IF EXISTS `hongan_system_agreement`;
CREATE TABLE `hongan_system_agreement`
(
    `id`         bigint           NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `type`       varchar(255)     NOT NULL DEFAULT '' COMMENT '协议类型(隐私协议/服务协议/关于我们/用户使用说明/技师隐私协议/技师用户协议/技师注销协议)',
    `content`    text             NULL COMMENT '协议内容',
    `created_at` TIMESTAMP        NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP        NULL     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`    TINYINT UNSIGNED NOT NULL DEFAULT '0' COMMENT '删除标识 1：删除 0：未删除',
    PRIMARY KEY (`id`)
) ENGINE = INNODB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
    COMMENT = '系统协议表';

# DROP TABLE IF EXISTS `hongan_system_message`;
# CREATE TABLE `hongan_system_message`
# (
#     `id`            bigint           NOT NULL AUTO_INCREMENT COMMENT 'ID',
#     `message_level` varchar(255)     NOT NULL DEFAULT '' COMMENT '消息级别(System:系统级/User:用户级)',
#     `admin_id`      bigint           NOT NULL DEFAULT 0 COMMENT '用户ID(用户级才有)',
#     `message_type`  varchar(255)     NOT NULL DEFAULT '' COMMENT '消息类型(MallOrder:商城订单/ServiceOrder:服务订单)',
#     `user_id`       bigint           NOT NULL COMMENT '用户Id',
#     `order_id`      bigint           NOT NULL DEFAULT 0 COMMENT '订单Id',
#     `order_no`      varchar(255)     NOT NULL DEFAULT '' COMMENT '订单号',
#     `is_read`       tinyint          NOT NULL DEFAULT 0 COMMENT '是否已读(true:是/false:否)',
#     `created_at`    timestamp        NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
#     `updated_at`    timestamp        NULL     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
#     `deleted`       tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除标识 1：删除 0：未删除',
#     primary key (id) USING BTREE
# ) ENGINE = InnoDB
#   AUTO_INCREMENT = 1
#   CHARACTER SET = utf8mb4
#   COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统消息表'
#   ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `hongan_system_message`;
CREATE TABLE `hongan_system_message`
(
    `id`           bigint           NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `message_type` varchar(255)     NOT NULL DEFAULT '' COMMENT '消息类型(MallOrder:商城订单/ServiceOrder:服务订单)',
    `user_id`      bigint           NOT NULL COMMENT '用户Id',
    `order_id`     bigint           NOT NULL DEFAULT 0 COMMENT '订单Id',
    `order_no`     varchar(255)     NOT NULL DEFAULT '' COMMENT '订单号',
    `created_at`   timestamp        NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`   timestamp        NULL     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`      tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除标识 1：删除 0：未删除',
    primary key (id) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统消息表'
  ROW_FORMAT = Dynamic;
