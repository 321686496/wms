package com.hongan.template.admin.vo;

import lombok.Data;

import java.util.List;

/**
 * @author vcoy
 * @date 2022/7/18 17:17
 */
@Data
public class RoleMenuVo {
    private String roleKey;
    private List<Long> menuIds;
}
