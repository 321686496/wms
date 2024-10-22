package com.hongan.template.user.entity;

import com.hongan.template.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * <p>
 * 用户收货地址表
 * </p>
 *
 * @author Administrator
 * @since 2023-05-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "PonlayUserAddress对象", description = "用户收货地址表")
public class HonganUserAddress extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户表id")
    private Long userId;

    @ApiModelProperty(value = "收件人姓名")
    private String name;

    @ApiModelProperty(value = "收件人手机号")
    private String phone;

    @ApiModelProperty(value = "邮编")
    private String zip;

    @ApiModelProperty(value = "省份")
    private String province;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "区")
    private String district;

    @ApiModelProperty(value = "详细地址")
    private String address;
    @ApiModelProperty(value = "详细地址")
    private String addressDetail;
    @ApiModelProperty(value = "经度")
    private BigDecimal longitude;
    @ApiModelProperty(value = "纬度")
    private BigDecimal latitude;

    @ApiModelProperty(value = "是否默认 0：默认 1：不默认")
    private Boolean isDefault;


}
