package com.hongan.template.base.vo;

import com.hongan.template.admin.enums.MenuType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 响应前端公共树
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreeDataVo {
    private String label;//标题
    private String value;//值
    private String code;//编码
    private String remark;//备注

    private List<TreeDataVo> children;//子节点
    private MenuType menuType;

    Boolean disabled = false;

    public TreeDataVo(String label, String value) {
        this.label = label;
        this.value = value;
    }

}
