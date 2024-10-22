package com.hongan.template.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hongan.template.admin.entity.HonganMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author zhangxd
 * @since 2020-02-16
 */
public interface HonganMenuMapper extends BaseMapper<HonganMenu> {

    List<String> getMenuPermsByIds(@Param("ids") List<Long> ids);
}
