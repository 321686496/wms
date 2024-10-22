package com.hongan.template.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2019-12-28 09:24
 * @Description: BaseEntity
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class BaseEntity extends Model {

    private static final long serialVersionUID = -3780477152475697623L;
    @TableId(value = "id", type = IdType.AUTO)//指定自增策略
    private Long id;

    @ApiModelProperty(value = "创建日期", hidden = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "更新日期", hidden = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    @TableLogic
    @ApiModelProperty(value = "删除标识", hidden = true)
    private Integer deleted;
}
