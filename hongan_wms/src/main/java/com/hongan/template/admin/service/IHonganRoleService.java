package com.hongan.template.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hongan.template.admin.query.QueryRole;
import com.hongan.template.admin.entity.HonganRole;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.base.vo.TreeDataVo;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zhangxd
 * @since 2019-12-30
 */
public interface IHonganRoleService extends IService<HonganRole> {
    //分页查询
    IPage<HonganRole> queryPage(QueryRole query);

    //查询列表
    List<TreeDataVo> queryList(QueryRole query);

    //查询角色信息
    HonganRole getRoleById(Long id) throws BaseException;

    //新增角色
    void create(HonganRole role) throws BaseException;

    //修改角色
    void update(HonganRole role) throws BaseException;

    //删除角色
    void delete(Long id) throws BaseException;

    //复制角色
    void copyRole(Long id) throws BaseException;

}
