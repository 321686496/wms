package com.hongan.template.admin.mapper;

import com.hongan.template.admin.entity.HonganRoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 角色和菜单关联表 Mapper 接口
 * </p>
 *
 * @author Administrator
 * @since 2023-07-12
 */
public interface HonganRoleMenuMapper extends BaseMapper<HonganRoleMenu> {

    void removeMenuByRoleId(@Param("roleId") Long roleId);

}
