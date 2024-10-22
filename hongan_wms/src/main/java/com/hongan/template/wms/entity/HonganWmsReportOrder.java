package com.hongan.template.wms.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hongan.template.base.entity.BaseEntity;
import com.hongan.template.base.vo.AttachmentVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 报货单表
 * </p>
 *
 * @author Administrator
 * @since 2024-06-25
 */
@Data
@TableName(autoResultMap = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "HonganWmsReportOrder对象", description = "报货单表")
public class HonganWmsReportOrder extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "创建人Id")
    private Long createAdminId;

    @ApiModelProperty(value = "创建人名称(冗余字段)")
    private String createAdminName;


    @ApiModelProperty(value = "单据日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate billDate;

    @ApiModelProperty(value = "单据编号(BH+年月日+6位排序号)")
    private String billCode;

    @ApiModelProperty(value = "是否是合并订单")
    private Boolean isMerge;

    @ApiModelProperty(value = "库存总数量")
    private BigDecimal stockNumberTotal;

    @ApiModelProperty(value = "报货总数量")
    private BigDecimal reportNumberTotal;

    @ApiModelProperty(value = "附件")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<AttachmentVo> attachment;

    @ApiModelProperty(value = "备注")
    private String remark;

    @TableField(exist = false)
    @ApiModelProperty(value = "明细列表")
    private List<HonganWmsReportOrderItem> itemList;

    @TableField(exist = false)
    @ApiModelProperty(value = "删除列表Ids")
    private List<Long> deleteIds;

}
