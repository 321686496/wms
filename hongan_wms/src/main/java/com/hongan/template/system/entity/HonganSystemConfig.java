package com.hongan.template.system.entity;

import com.hongan.template.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 系统配置表
 * </p>
 *
 * @author Administrator
 * @since 2024-06-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="HonganSystemConfig对象", description="系统配置表")
public class HonganSystemConfig extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户默认头像")
    private String userDefaultAvatar;

    @ApiModelProperty(value = "用户默认昵称前缀")
    private String userDefaultNicknamePrefix;


}
