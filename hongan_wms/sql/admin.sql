-- ----------------------------
-- 系统菜单表
-- ----------------------------
DROP TABLE IF EXISTS `hongan_menu`;
CREATE TABLE `hongan_menu`
(
    `id`                  bigint           NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `pid`                 bigint           NOT NULL DEFAULT 0 COMMENT '父菜单ID',
    `menu_type`           tinyint          NOT NULL DEFAULT 0 COMMENT '菜单类型(目录/菜单/按钮)',
    `enable`              tinyint          NOT NULL DEFAULT 1 COMMENT '启用标识（true:正常 false:停用）',
    `path`                varchar(255)     NOT NULL DEFAULT '' COMMENT '菜单路径',
    `name`                varchar(255)     NOT NULL DEFAULT '' COMMENT '菜单别名',
    `redirect`            varchar(255)     NOT NULL DEFAULT '' COMMENT '重定向地址',
    `component`           varchar(255)     NOT NULL DEFAULT '' COMMENT '视图文件路径',
    `meta_icon`           varchar(255)     NOT NULL DEFAULT '' COMMENT '菜单图标',
    `applet_meta_icon`    varchar(255)     NOT NULL DEFAULT '' COMMENT '菜单图标(小程序)',
    `applet_path`         varchar(255)     NOT NULL DEFAULT '' COMMENT '菜单路径(小程序)',
    `meta_active_menu`    varchar(255)     NOT NULL DEFAULT '' COMMENT '当前路由为详情页时，需要高亮的菜单',
    `meta_is_link`        tinyint          NOT NULL DEFAULT 0 COMMENT '是否外链',
    `meta_is_hide`        tinyint          NOT NULL DEFAULT 0 COMMENT '是否隐藏',
    `applet_meta_is_hide` tinyint          NOT NULL DEFAULT 0 COMMENT '是否隐藏(小程序)',
    `meta_is_full`        tinyint          NOT NULL DEFAULT 0 COMMENT '是否全屏(示例：数据大屏页面)',
    `meta_is_affix`       tinyint          NOT NULL DEFAULT 1 COMMENT '是否固定在 tabs nav',
    `meta_is_keep_alive`  tinyint          NOT NULL DEFAULT 1 COMMENT '是否缓存',
    `perms`               varchar(500)     NULL     DEFAULT '' COMMENT '权限标识',
    `priority`            int              NOT NULL DEFAULT 0 COMMENT '优先级',
    `created_at`          timestamp        NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`          timestamp        NULL     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`             tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除标识 1：删除 0：未删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_pid` (`pid`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统菜单表'
  ROW_FORMAT = Dynamic;


-- ----------------------------
-- 管理员表
-- ----------------------------
DROP TABLE IF EXISTS `hongan_admin`;
CREATE TABLE `hongan_admin`
(
    `id`              bigint           NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `role_id`         bigint           NOT NULL COMMENT '角色ID',
    `dept_id`         bigint           NOT NULL DEFAULT 0 COMMENT '部门ID',
    `is_initial`      tinyint          NOT NULL DEFAULT 0 COMMENT '初始账号(true:是/false:否)',
    `name`            varchar(255)     NOT NULL COMMENT '登录名',
    `phone`           varchar(20)      NULL     DEFAULT '' COMMENT '手机号',
    `status`          tinyint          NOT NULL DEFAULT 0 COMMENT '状态：(NORMAL:正常/LOCK:禁用)',
    `password`        varchar(128)     NOT NULL COMMENT '密码',
    `real_name`       varchar(255)     NULL     DEFAULT '' COMMENT '真实姓名',
    `avatar`          varchar(255)     NULL     DEFAULT '' COMMENT '头像',
    `sex`             varchar(10)      NOT NULL DEFAULT '男' COMMENT '用户性别',
    `email`           varchar(50)      NOT NULL DEFAULT '' comment '用户邮箱',
    `shop_name`       varchar(255)     NOT NULL DEFAULT '' comment '店铺名称',
    `login_ip`        varchar(128)              DEFAULT '' COMMENT '最后登录IP',
    `login_date`      datetime         NULL COMMENT '最后登录时间',
    `pwd_update_date` datetime         NULL COMMENT '密码最后更新时间',
    `remark`          varchar(255)     NULL     DEFAULT '' COMMENT '备注',
    `priority`            int              NOT NULL DEFAULT 0 COMMENT '优先级',
    `created_at`      timestamp        NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`      timestamp        NULL     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`         tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除标识 1：删除 0：未删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_phone` (`phone`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '管理员表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- 系统角色表
-- ----------------------------
DROP TABLE IF EXISTS `hongan_role`;
CREATE TABLE `hongan_role`
(
    `id`         bigint           NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `is_default` tinyint          NOT NULL DEFAULT 0 COMMENT '是否默认角色(true:是/false:否)',
    `name`       varchar(255)     NOT NULL COMMENT '角色名称',
    `role_type`  varchar(255)     NOT NULL COMMENT '角色类型',
    `role_key`   varchar(255)     NOT NULL COMMENT '角色关键字',
    `remark`     varchar(255)     NULL     DEFAULT NULL COMMENT '备注',
    `created_at` timestamp        NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` timestamp        NULL     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`    tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除标识 1：删除 0：未删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_key` (`role_key`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统角色表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- 角色和菜单关联表
-- ----------------------------
DROP TABLE IF EXISTS `hongan_role_menu`;
CREATE TABLE `hongan_role_menu`
(
    `id`         bigint           NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `role_id`    bigint           NOT NULL COMMENT '角色ID',
    `menu_id`    bigint           NOT NULL COMMENT '菜单ID',
    `created_at` timestamp        NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` timestamp        NULL     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`    tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除标识 1：删除 0：未删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色和菜单关联表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- 系统部门表
-- ----------------------------
DROP TABLE IF EXISTS `hongan_dept`;
CREATE TABLE `hongan_dept`
(
    `id`         bigint           NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `is_default` tinyint          NOT NULL DEFAULT 0 COMMENT '是否默认部门(true:是/false:否)',
    `pid`        bigint           NOT NULL DEFAULT 0 comment '父部门id',
    `ancestors`  varchar(50)      NULL     DEFAULT '' comment '祖级列表',
    `name`       varchar(50)      NULL     DEFAULT '' comment '部门名称',
    `leader`     varchar(20)      NULL     DEFAULT '' null COMMENT '负责人',
    `phone`      varchar(20)      NULL     DEFAULT '' COMMENT '联系电话',
    `email`      varchar(50)      NULL     DEFAULT '' COMMENT '邮箱',
    `enable`     tinyint          NOT NULL DEFAULT 1 COMMENT '启用标识（true:正常 false:停用）',
    `priority`   int              NOT NULL DEFAULT 0 COMMENT '优先级',
    `created_at` timestamp        NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` timestamp        NULL     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`    tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除标识 1：删除 0：未删除',
    primary key (id) USING BTREE
) ENGINE = innodb
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '部门表'
  ROW_FORMAT = Dynamic;