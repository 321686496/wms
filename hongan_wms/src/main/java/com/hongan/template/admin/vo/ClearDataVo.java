package com.hongan.template.admin.vo;

import lombok.Data;

@Data
public class ClearDataVo {
    private Long adminId;
    private Boolean clearBillData = false;
    private Boolean clearBasicData = false;
    private String password;
}
