package com.hongan.template.admin.service;

import com.hongan.template.security.common.TokenUser;
import com.hongan.template.admin.entity.HonganDept;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hongan.template.admin.query.QueryDept;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.base.vo.TreeDataVo;

import java.util.List;

/**
 * <p>
 * 部门表 服务类
 * </p>
 *
 * @author Administrator
 * @since 2023-07-12
 */
public interface IHonganDeptService extends IService<HonganDept> {
    List<HonganDept> queryData(QueryDept query);

    List<TreeDataVo> queryTreeData(QueryDept query);

    List<TreeDataVo> queryTreeDataSelect(QueryDept query, TokenUser user);

    HonganDept selectById(Long id) throws BaseException;

    HonganDept selectByIdXml(Long id) throws BaseException;

    void create(HonganDept data) throws BaseException;

    void update(HonganDept data) throws BaseException;

    void updateStatus(Long id) throws BaseException;

    void delete(Long id) throws BaseException;

    List<Long> getChildren(Long id);

    //查询部门的父部门
    String getAncestors(Long pid);
}
