package com.hongan.template.system.entity;

import com.hongan.template.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 系统协议表
 * </p>
 *
 * @author Administrator
 * @since 2024-05-10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "HonganSystemAgreement对象", description = "系统协议表")
public class HonganSystemAgreement extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "协议类型(隐私协议/服务协议/关于我们/用户使用说明/技师隐私协议/技师用户协议/技师注销协议)")
    private String type;

    @ApiModelProperty(value = "协议内容")
    private String content;


}
