package com.hongan.template.wms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName(autoResultMap = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "HonganWmsMaterialType对象", description = "物料类别表")
public class HonganWmsMaterialType extends BaseEntity {
    private static final long serialVersionUID = 1L;

//    @ApiModelProperty(value = "编号")
//    @TableId(type = IdType.AUTO)
//    private Long id;

    @ApiModelProperty(value = "名称")
    private String classifyName;

    @ApiModelProperty(value = "名称（法）")
    private String classifyNameFr;
}
