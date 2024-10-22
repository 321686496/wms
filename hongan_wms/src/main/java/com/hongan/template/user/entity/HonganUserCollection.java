package com.hongan.template.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.hongan.template.base.entity.BaseEntity;
import com.hongan.template.enums.SystemGoodsType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户收藏表
 * </p>
 *
 * @author Administrator
 * @since 2023-04-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "PonlayUserCollection对象", description = "用户收藏表")
public class HonganUserCollection extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "类型")
    private SystemGoodsType type;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "值")
    private Long actionId;

    @ApiModelProperty(value = "用户信息")
    @TableField(exist = false)
    private HonganUser user;

}
