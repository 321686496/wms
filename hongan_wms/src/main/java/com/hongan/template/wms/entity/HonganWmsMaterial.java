package com.hongan.template.wms.entity;

import com.hongan.template.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 物料表
 * </p>
 *
 * @author Administrator
 * @since 2024-06-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "HonganWmsMaterial对象", description = "物料表")
public class HonganWmsMaterial extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "编码")
    private String code;

    @ApiModelProperty(value = "产品类型编号")
    private Long typeId;

    @ApiModelProperty(value = "启用标识（true:正常 false:停用）")
    private Boolean enable;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "优先级")
    private Integer priority;


}
