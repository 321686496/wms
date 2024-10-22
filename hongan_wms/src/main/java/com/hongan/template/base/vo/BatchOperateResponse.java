package com.hongan.template.base.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 批量操作响应数据
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BatchOperateResponse {
    @ApiModelProperty(value = "数据总条数")
    Integer total;
    @ApiModelProperty(value = "成功数量")
    Integer successCount;
    @ApiModelProperty(value = "失败数量")
    Integer errorCount;
    @ApiModelProperty(value = "错误信息列表")
    List<Map<String, Object>> errList;

    public BatchOperateResponse(Integer total, Integer successCount, Integer errorCount) {
        this.total = total;
        this.successCount = successCount;
        this.errorCount = errorCount;
    }
}
