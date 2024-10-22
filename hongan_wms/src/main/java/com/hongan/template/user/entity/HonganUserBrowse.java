package com.hongan.template.user.entity;

import com.hongan.template.base.entity.BaseEntity;
import com.hongan.template.enums.SystemGoodsType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户浏览记录表
 * </p>
 *
 * @author Administrator
 * @since 2023-04-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "PonlayUserBrowse对象", description = "用户浏览记录表")
public class HonganUserBrowse extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "类型")
    private SystemGoodsType type;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "商品Id")
    private Long goodsId;

}
