package com.hongan.template.wms.vo;

import com.hongan.template.wms.entity.HonganWmsMaterial;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class HonganWmsMaterialVO extends HonganWmsMaterial {
    @ApiModelProperty(value = "产品类型名称")
    private String typeName;
    @ApiModelProperty(value = "产品类型名称（法）")
    private String typeNameFr;
}
