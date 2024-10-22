package com.hongan.template.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.hongan.template.admin.entity.HonganAdmin;
import com.hongan.template.base.entity.BaseEntity;
import com.hongan.template.enums.MessageType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 系统消息表
 * </p>
 *
 * @author Administrator
 * @since 2024-05-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "HonganSystemMessage对象", description = "系统消息表")
public class HonganSystemMessage extends BaseEntity {

    private static final long serialVersionUID = 1L;

//    @ApiModelProperty(value = "消息级别(System:系统级/User:用户级)")
//    private MessageLevel messageLevel;
//
//    @ApiModelProperty(value = "用户ID(用户级才有)")
//    private Long adminId;

    @ApiModelProperty(value = "消息类型(MallOrder:商城订单/ServiceOrder:服务订单)")
    private MessageType messageType;

    @ApiModelProperty(value = "用户Id")
    private Long userId;

    @ApiModelProperty(value = "订单Id")
    private Long orderId;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

//    @ApiModelProperty(value = "是否已读")
//    private Boolean isRead;

    @TableField(exist = false)
    private HonganAdmin user;

}
