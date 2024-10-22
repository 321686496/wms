package com.hongan.template.admin.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TenantRegisterVo {
    @ApiModelProperty(value = "注册手机号")
    private String phone;

    @ApiModelProperty(value = "图片验证码")
    private String imgCaptcha;

    @ApiModelProperty(value = "短信验证码")
    private String phoneCaptcha;

    @ApiModelProperty(value = "密码")
    private String pwd;

    @ApiModelProperty(value = "重复密码")
    private String pwd2;

    private String remark;

    @ApiModelProperty(value = "确认注销操作")
    private Boolean confirmOperate;

    private String sourceType;
    private String cmdCode;
//    @ApiModelProperty(value = "已存在的部门名称")
//    private String deptName;
}
