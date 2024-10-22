package com.hongan.template.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.hongan.template.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <p>
 * 部门表
 * </p>
 *
 * @author Administrator
 * @since 2023-07-12
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "HonganDept对象", description = "部门表")
public class HonganDept extends BaseEntity {

    private static final long serialVersionUID = 1L;

    public HonganDept(String phone) {
        this.isDefault = true;
        this.pid = 0L;
        this.name = "总公司";
        this.phone = phone;
        this.enable = true;
        this.priority = 100;
    }

    @ApiModelProperty(value = "是否默认部门(true:是/false:否)")
    private Boolean isDefault;

    @ApiModelProperty(value = "父部门id")
    private Long pid;

    @ApiModelProperty(value = "祖级列表")
    private String ancestors;

    @ApiModelProperty(value = "部门名称")
    private String name;

    @ApiModelProperty(value = "负责人")
    private String leader;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "部门状态（true:正常 false:停用）")
    private Boolean enable;

    @ApiModelProperty(value = "优先级")
    private Integer priority;

    @ApiModelProperty(value = "子部门")
    @TableField(exist = false)
    private List<HonganDept> children;

}
