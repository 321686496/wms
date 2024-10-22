package com.hongan.template.system.entity;

import com.hongan.template.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 系统轮播图表
 * </p>
 *
 * @author Administrator
 * @since 2024-05-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "HonganSystemBanner对象", description = "系统轮播图表")
public class HonganSystemBanner extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "图片")
    private String cover;

    @ApiModelProperty(value = "启用状态 (true:启用/false:禁用)")
    private Boolean enable;

    @ApiModelProperty(value = "排序值")
    private Integer priority;


}
