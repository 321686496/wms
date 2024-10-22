package com.hongan.template.config.entity;

import com.hongan.template.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 系统参数配置表
 * </p>
 *
 * @author Administrator
 * @since 2023-07-12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "PonlaySystemConfig对象", description = "系统参数配置表")
public class HonganConfig extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "参数名称")
    private String configName;

    @ApiModelProperty(value = "参数键名")
    private String configKey;

    @ApiModelProperty(value = "参数键值")
    private String configValue;

    @ApiModelProperty(value = "备注")
    private String remark;

    public HonganConfig(String configName, String configKey, String configValue) {
        this.configName = configName;
        this.configKey = configKey;
        this.configValue = configValue;
    }

}
