DROP TABLE IF EXISTS `hongan_user`;
CREATE TABLE `hongan_user`
(
    `id`              bigint(20)       NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `name`            varchar(255)     NOT NULL COMMENT '登录名',
    `phone`           varchar(20)      NOT NULL DEFAULT '' COMMENT '手机号',
    `password`        varchar(128)     NOT NULL COMMENT '密码',
    `nickname`        varchar(128)     NULL     DEFAULT '' COMMENT '昵称',
    `avatar`          varchar(255)     NULL     DEFAULT '' COMMENT '头像',
    `status`          tinyint(3)       NOT NULL DEFAULT 0 COMMENT '状态：0 正常 -1 禁用',
    `gender`          varchar(255)     NOT NULL DEFAULT '男' COMMENT '性別(男/女)',
    `wx_open_id`      varchar(255)     NULL     DEFAULT '' COMMENT '微信OpenId',
    `pid`             bigint           NOT NULL DEFAULT 0 COMMENT '上级Id',
    `invitation_code` varchar(255)     NOT NULL DEFAULT '' COMMENT '邀请码',
    `created_at`      timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`      timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`         tinyint unsigned NOT NULL DEFAULT '0' COMMENT '删除标识 1：删除 0：未删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
    COMMENT = '用户信息表';

DROP TABLE IF EXISTS `hongan_user_address`;
CREATE TABLE `hongan_user_address`
(
    `id`             bigint         NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id`        bigint              DEFAULT NULL COMMENT '用户表id',
    `name`           varchar(30)         DEFAULT NULL COMMENT '姓名',
    `phone`          varchar(30)         DEFAULT NULL COMMENT '手机号',
    `zip`            varchar(8)          DEFAULT NULL COMMENT '邮编',
    `province`       varchar(20)         DEFAULT NULL COMMENT '省份',
    `city`           varchar(20)         DEFAULT NULL COMMENT '市',
    `district`       varchar(20)         DEFAULT NULL COMMENT '区',
    `longitude`      decimal(10, 6) NULL DEFAULT 0 COMMENT '经度',
    `latitude`       decimal(10, 6) NULL DEFAULT 0 COMMENT '纬度',
    `address`        varchar(255)        DEFAULT NULL COMMENT '地址',
    `address_detail` varchar(1024)       DEFAULT NULL COMMENT '详细地址',
    `is_default`     tinyint(1)     NULL DEFAULT 1 COMMENT '是否默认 0：默认 1：不默认',
    `created_at`     timestamp(0)   NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`     timestamp(0)   NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`        tinyint(1)     NULL DEFAULT 0 COMMENT '删除标识 1：删除 0：未删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
    COMMENT = '用户收货地址表'
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


DROP TABLE IF EXISTS `hongan_user_collection`;
CREATE TABLE `hongan_user_collection`
(
    `id`         bigint           NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `type`       varchar(255)     NOT NULL DEFAULT 'Goods' COMMENT '类型(商品/服务)',
    `user_id`    bigint           NOT NULL COMMENT '用户id',
    `action_id`  bigint           NOT NULL COMMENT '值(商品Id/自提点Id)',
    `created_at` TIMESTAMP        NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP        NULL     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`    TINYINT UNSIGNED NOT NULL DEFAULT '0' COMMENT '删除标识 1：删除 0：未删除',
    PRIMARY KEY (`id`)
) ENGINE = INNODB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
    COMMENT = '用户收藏表';


DROP TABLE IF EXISTS `hongan_user_browse`;
CREATE TABLE `hongan_user_browse`
(
    `id`         bigint           NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `type`       varchar(255)     NOT NULL DEFAULT 'Goods' COMMENT '类型(商品/服务)',
    `user_id`    bigint           NOT NULL COMMENT '用户id',
    `goods_id`   bigint           NOT NULL COMMENT '商品Id',
    `created_at` TIMESTAMP        NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP        NULL     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`    TINYINT UNSIGNED NOT NULL DEFAULT '0' COMMENT '删除标识 1：删除 0：未删除',
    PRIMARY KEY (`id`)
) ENGINE = INNODB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
    COMMENT = '用户浏览记录表';



DROP TABLE IF EXISTS `hongan_user_message`;
CREATE TABLE `hongan_user_message`
(
    `id`                  bigint           NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id`             bigint           NOT NULL COMMENT '用户ID',
    `user_delete`         tinyint          NOT NULL DEFAULT 0 COMMENT '是否删除',
    `receive_user_id`     bigint           NOT NULL COMMENT '接收用户ID',
    `receive_user_delete` tinyint          NOT NULL DEFAULT 0 COMMENT '接收者是否删除',
    `last_time`           datetime         NULL COMMENT '最后一次沟通时间',
    `created_at`          timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`          timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`             tinyint unsigned NOT NULL DEFAULT '0' COMMENT '删除标识 1：删除 0：未删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;



DROP TABLE IF EXISTS `hongan_user_message_record`;
CREATE TABLE `hongan_user_message_record`
(
    `id`              bigint           NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `pid`             bigint           NOT NULL DEFAULT 0 COMMENT '上级ID',
    `user_id`         bigint           NOT NULL COMMENT '用户ID',
    `receive_user_id` bigint           NOT NULL COMMENT '接收用户ID',
    `message`         text             NULL COMMENT '',
    `is_read`         bigint           NOT NULL DEFAULT 0 COMMENT '是否已读(true:是/false:否)',
    `created_at`      timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`      timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`         tinyint unsigned NOT NULL DEFAULT '0' COMMENT '删除标识 1：删除 0：未删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;





