package com.hongan.template.admin.vo;

import lombok.Data;

@Data
public class VerifyPersonVo {
    //角色/部门Id
    private Long id;
    //角色/部门名称
    private String name;
    //管理员Id
    private Long adminId;
    //管理员名称
    private String adminName;
    //印章URL
    private String chapterUrl;
    //印章URL
    private String remark;
}
