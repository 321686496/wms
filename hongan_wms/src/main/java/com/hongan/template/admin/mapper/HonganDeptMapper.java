package com.hongan.template.admin.mapper;

import com.hongan.template.admin.entity.HonganDept;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 部门表 Mapper 接口
 * </p>
 *
 * @author Administrator
 * @since 2023-07-12
 */
public interface HonganDeptMapper extends BaseMapper<HonganDept> {

    HonganDept selectByIdXml(@Param("id") Long id);

    /**
     * 根据ID查询所有子部门
     *
     * @param deptId 部门ID
     * @return 部门列表
     */
    public List<HonganDept> selectChildrenDeptById(@Param("deptId") Long deptId);

    /**
     * 修改子元素关系
     *
     * @param depts 子元素
     * @return 结果
     */
    public int updateDeptChildren(@Param("depts") List<HonganDept> depts);


    /**
     * 修改所在部门正常状态
     *
     * @param deptIds 部门ID组
     */
    void updateDeptStatusNormal(@Param("deptIds") Long[] deptIds);
}
