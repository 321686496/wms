package com.hongan.template.wms.entity;

import java.math.BigDecimal;

import com.hongan.template.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <p>
 * 报货单明细表
 * </p>
 *
 * @author Administrator
 * @since 2024-06-25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "HonganWmsReportOrderItem对象", description = "报货单明细表")
public class HonganWmsReportOrderItem extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "上级单据ID")
    private Long pid;

    @ApiModelProperty(value = "物料Id")
    private Long materialId;

    @ApiModelProperty(value = "物料名称(冗余字段)")
    private String materialName;

    @ApiModelProperty(value = "Sku编码(冗余字段)")
    private String materialCode;

    @ApiModelProperty(value = "物料类型(冗余字段)")
    private Long typeId;

    @ApiModelProperty(value = "物料类型名称(冗余字段)")
    private String typeName;

    @ApiModelProperty(value = "物料类型名称【法】(冗余字段)")
    private String typeNameFr;

    @ApiModelProperty(value = "物料排序值")
    private Integer materialPriority;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty(value = "店铺排序值")
    private Integer shopPriority;

    @ApiModelProperty(value = "库存量")
    private BigDecimal stockNumber;

    @ApiModelProperty(value = "报货量")
    private BigDecimal reportNumber;

    @ApiModelProperty(value = "备注")
    private String remark;


}
