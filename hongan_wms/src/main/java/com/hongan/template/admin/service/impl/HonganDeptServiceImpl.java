package com.hongan.template.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hongan.template.admin.entity.HonganAdmin;
import com.hongan.template.admin.entity.HonganDept;
import com.hongan.template.admin.enums.RoleDataScope;
import com.hongan.template.admin.mapper.HonganDeptMapper;
import com.hongan.template.admin.query.QueryDept;
import com.hongan.template.admin.service.IHonganAdminService;
import com.hongan.template.admin.service.IHonganDeptService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hongan.template.base.entity.BaseError;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.base.utils.Convert;
import com.hongan.template.base.utils.DataCheckUtils;
import com.hongan.template.base.vo.TreeDataVo;
import com.hongan.template.security.common.TokenUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 部门表 服务实现类
 * </p>
 *
 * @author Administrator
 * @since 2023-07-12
 */
@Service
public class HonganDeptServiceImpl extends ServiceImpl<HonganDeptMapper, HonganDept> implements IHonganDeptService {
    @Autowired
    private IHonganAdminService adminService;

    @Override
    public List<HonganDept> queryData(QueryDept query) {
        //查询一级菜单
        query.setPid(0L);
        return getChildren(query);
    }

    //迭代获取所有部门关系
    private List<HonganDept> getChildren(QueryDept query) {
        List<HonganDept> list = baseMapper.selectList(query.toWrapper());
        for (HonganDept dept : list) {
            query.setPid(dept.getId());
            List<HonganDept> children = getChildren(query);
            if (children.size() > 0) {
                dept.setChildren(children);
            }
        }
        return list;
    }

    @Override
    public List<TreeDataVo> queryTreeData(QueryDept query) {
        //查询一级菜单
        query.setPid(0L);
        return getTreeChildren(query);
    }

    @Override
    public List<TreeDataVo> queryTreeDataSelect(QueryDept query, TokenUser user) {
        return queryTreeData(query);
    }

    private void handleDisabled(List<TreeDataVo> res, List<Long> ids) {
        for (TreeDataVo re : res) {
            re.setDisabled(!ids.contains(Long.parseLong(re.getValue())));
            if (re.getChildren() != null && re.getChildren().size() > 0) {
                handleDisabled(re.getChildren(), ids);
            }
        }
    }


    //迭代获取所有菜单关系
    private List<TreeDataVo> getTreeChildren(QueryDept query) {
        List<TreeDataVo> treeDataVos = new ArrayList<>();
        List<HonganDept> list = baseMapper.selectList(query.toWrapper());
        for (HonganDept dept : list) {
            TreeDataVo vo = new TreeDataVo();
            vo.setLabel(dept.getName());
            vo.setValue(dept.getId().toString());
            query.setPid(dept.getId());
            List<TreeDataVo> children = getTreeChildren(query);
            if (children.size() > 0) {
                vo.setChildren(children);
            }
            treeDataVos.add(vo);
        }
        return treeDataVos;
    }

    @Override
    public HonganDept selectById(Long id) throws BaseException {
        if (null == id) throw new BaseException(BaseError.BadRequest);
        HonganDept data = baseMapper.selectOne(new QueryWrapper<HonganDept>().eq("id", id));
        if (data == null) throw new BaseException(BaseError.BadRequest);
        return data;
    }

    @Override
    public HonganDept selectByIdXml(Long id) throws BaseException {
        if (null == id) throw new BaseException(BaseError.BadRequest);
        HonganDept data = baseMapper.selectByIdXml(id);
        if (data == null) throw new BaseException(BaseError.BadRequest);
        return data;
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public void create(HonganDept data) throws BaseException {
        checkDate(data);
        //同级之间 不允许名称重复
        Long count = baseMapper.selectCount(new QueryWrapper<HonganDept>().eq("pid", data.getPid()).eq("name", data.getName()));
        if (count > 0) throw new BaseException(BaseError.NameRepeat);

        //查询部门的祖级列表
        String ancestors = getAncestors(data.getPid());
        data.setAncestors(ancestors);
        data.insert();
    }

    private void checkDate(HonganDept data) throws BaseException {
        DataCheckUtils.checkParam(data.getPid(), BaseError.PleaseSelectParentDept);
        DataCheckUtils.checkParam(data.getName(), BaseError.PleaseInputName);
        if (data.getId() != null && data.getPid() != null && data.getPid().equals(data.getId())) {
            throw new BaseException(BaseError.ParentCannotIsSelf);
        }
    }


    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public void update(HonganDept data) throws BaseException {
        selectById(data.getId());
        checkDate(data);
        //同级之间 不允许名称重复
        Long count = baseMapper.selectCount(new QueryWrapper<HonganDept>().ne("id", data.getId()).eq("pid", data.getPid()).eq("name", data.getName()));
        if (count > 0) throw new BaseException(BaseError.NameRepeat);


        HonganDept newParentDept = selectById(data.getPid());
        HonganDept oldDept = selectById(data.getId());
        if (newParentDept != null && oldDept != null) {
            String newAncestors = newParentDept.getAncestors() + "," + newParentDept.getId();
            String oldAncestors = oldDept.getAncestors();
            data.setAncestors(newAncestors);
            updateDeptChildren(data.getId(), newAncestors, oldAncestors);
        }

        data.updateById();

    }

    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public void updateStatus(Long id) throws BaseException {
        HonganDept data = selectById(id);
        data.setEnable(!data.getEnable());
        if (data.getEnable()) {
            updateParentDeptStatusNormal(data);
        }
        data.updateById();
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public void delete(Long id) throws BaseException {
        HonganDept data = selectById(id);
        if (data.getIsDefault()) {
            throw new BaseException(BaseError.DefaultDeptProhibitDelete);
        }
        //如果存在管理员，禁止删除
        long count = adminService.count(new QueryWrapper<HonganAdmin>().eq("dept_id", id));
        if (count > 0) {
            throw new BaseException(BaseError.DeptExistUserProhibitDelete);
        }
        baseMapper.deleteById(id);
    }

    @Override
    public List<Long> getChildren(Long id) {
        List<HonganDept> children = baseMapper.selectChildrenDeptById(id);
        List<Long> idList = children.stream().map(HonganDept::getId).collect(Collectors.toList());
        idList.add(id);
        return idList;
    }

    /**
     * 修改该部门的父级部门状态
     *
     * @param dept 当前部门
     */
    private void updateParentDeptStatusNormal(HonganDept dept) {
        String ancestors = dept.getAncestors();
        Long[] deptIds = Convert.toLongArray(ancestors);
        if (deptIds.length > 0) {
            baseMapper.updateDeptStatusNormal(deptIds);
        }
    }

    /**
     * 修改子元素关系
     *
     * @param deptId       被修改的部门ID
     * @param newAncestors 新的父ID集合
     * @param oldAncestors 旧的父ID集合
     */
    public void updateDeptChildren(Long deptId, String newAncestors, String oldAncestors) {
        List<HonganDept> children = baseMapper.selectChildrenDeptById(deptId);
        for (HonganDept child : children) {
            child.setAncestors(child.getAncestors().replaceFirst(oldAncestors, newAncestors));
        }
        if (children.size() > 0) {
            baseMapper.updateDeptChildren(children);
        }
    }


    @Override
    public String getAncestors(Long pid) {
        HonganDept dept = baseMapper.selectOne(new QueryWrapper<HonganDept>().eq("id", pid));
        if (dept.getPid() > 0) {
            return dept.getAncestors() + "," + pid;
        } else {
            return pid + "";
        }
    }

    private String getAncestors(Long pid, String ancestors) {
        HonganDept dept = baseMapper.selectOne(new QueryWrapper<HonganDept>().eq("id", pid));
        ancestors = pid + "," + ancestors;
        if (dept.getPid() > 0) {
            return getAncestors(dept.getPid(), ancestors);
        } else {
            return ancestors.substring(0, ancestors.length() - 1);
        }
    }

}
