package com.hongan.template.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hongan.template.base.entity.BaseEntity;
import com.hongan.template.enums.AdminStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * <p>
 *
 * </p>
 *
 * @author admin
 * @since 2020-07-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(autoResultMap = true)
@ApiModel(value = "PonlayUser对象", description = "")
public class HonganUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "登录名")
    private String name;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @JsonIgnore
    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "状态：正常:NORMAL 禁用:LOCK")
    private AdminStatus status;

    @ApiModelProperty(value = "性別(男/女)")
    private String gender;

    @ApiModelProperty(value = "微信OpenId")
    private String wxOpenId;

    @ApiModelProperty(value = "上级Id")
    private Long pid;

    @ApiModelProperty(value = "邀请码")
    private String invitationCode;

    public String getRole() {
        return "CUSTOMER";
    }

    public void setCryptPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        this.password = encoder.encode(password);
    }

}
