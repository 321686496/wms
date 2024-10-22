package com.hongan.template.base.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 订单附件
 *
 * @Author: zyp
 * @Date: 2021/12/25 0025
 */
@Data
public class AttachmentVo {
    @ApiModelProperty(value = "文件名称")
    String name;
    @ApiModelProperty(value = "文件URL")
    String url;
}
