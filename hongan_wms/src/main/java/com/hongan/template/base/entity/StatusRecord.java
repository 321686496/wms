package com.hongan.template.base.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2019-11-07 15:46
 * @Description: StatusRecord
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusRecord {
    private Long createdAt;
    private Long id;
    private Integer fromStatus;
    private Integer toStatus;
    private String reason;
    private String img;
}
