package com.hongan.template.base.entity;

import lombok.Data;

import java.util.List;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2019-11-15 16:20
 * @Description: Id
 */

@Data
public class Id {
    private Long id;
    private List<Long> ids;
    private Boolean enable;
    private Boolean isDefault;
    private String wxOpenId;

}
