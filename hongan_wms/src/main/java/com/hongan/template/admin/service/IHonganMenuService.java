package com.hongan.template.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hongan.template.admin.query.QueryMenu;
import com.hongan.template.admin.entity.HonganMenu;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.base.vo.TreeDataVo;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zhangxd
 * @since 2020-02-16
 */
public interface IHonganMenuService extends IService<HonganMenu> {
    List<HonganMenu> queryList(QueryMenu query);

    //查询菜单
    List<HonganMenu> queryMenu(QueryMenu query) throws BaseException;

    List<TreeDataVo> queryMenuSelectData(QueryMenu query) throws BaseException;

    //查询商家可用的菜单
    List<TreeDataVo> getTenantMenuTree(QueryMenu query) throws BaseException;

    //查询管理员可用的菜单
    List<TreeDataVo> getAdminMenuTree(QueryMenu query) throws BaseException;

    //根据菜单Id查询菜单的可访问权限
    List<String> getMenuPermsByIds(List<Long> ids);


    //查询菜单详情
    HonganMenu selectById(Long id) throws BaseException;

    //新增菜单
    void create(HonganMenu menu);

    //修改菜单
    void update(HonganMenu menu) throws BaseException;

    //修改菜单启用状态
    void updateStatus(Long id) throws BaseException;

    //删除菜单
    void delete(Long id);

    List<HonganMenu> getMenuTree(String roleKey) throws BaseException;

}
