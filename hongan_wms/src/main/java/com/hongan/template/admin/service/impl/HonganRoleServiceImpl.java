package com.hongan.template.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hongan.template.admin.query.QueryRole;
import com.hongan.template.admin.entity.HonganAdmin;
import com.hongan.template.admin.entity.HonganRole;
import com.hongan.template.admin.mapper.HonganRoleMapper;
import com.hongan.template.admin.service.IHonganAdminService;
import com.hongan.template.admin.service.IHonganRoleMenuService;
import com.hongan.template.admin.service.IHonganRoleService;
import com.hongan.template.base.entity.BaseError;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.base.utils.DataCheckUtils;
import com.hongan.template.base.utils.StringUtils;
import com.hongan.template.base.vo.TreeDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhangxd
 * @since 2019-12-30
 */
@Service
@CacheConfig(cacheNames = "CACHE:SECURITY:ROLE")
public class HonganRoleServiceImpl extends ServiceImpl<HonganRoleMapper, HonganRole> implements IHonganRoleService {
    @Autowired
    private IHonganAdminService adminService;
    @Autowired
    private IHonganRoleMenuService roleMenuService;

    @Override
    public IPage<HonganRole> queryPage(QueryRole query) {
        IPage<HonganRole> page = new Page<>(query.getCurrent(), query.getPageSize());
        List<HonganRole> records = baseMapper.selectPage(page, query.toWrapper()).getRecords();
        for (HonganRole record : records) {
            record.setMenuIds(roleMenuService.getRoleMenuIds(record.getId()));
        }
        return page;
    }

    @Override
    public List<TreeDataVo> queryList(QueryRole query) {
        List<HonganRole> list = baseMapper.selectList(query.toWrapper());
        List<TreeDataVo> res = new ArrayList<>();
        for (HonganRole data : list) {
            res.add(new TreeDataVo(data.getName(), data.getId() + ""));
        }
        return res;
    }

    @Override
    public HonganRole getRoleById(Long id) throws BaseException {
        if (null == id) throw new BaseException(BaseError.BadRequest);
        HonganRole role = baseMapper.selectOne(new QueryWrapper<HonganRole>().eq("id", id));
        if (role == null) throw new BaseException(BaseError.BadRequest);
        return role;
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    public void create(HonganRole role) throws BaseException {
        checkData(role);
        //查询roleKey是否重复
        Long count = baseMapper.selectCount(new QueryWrapper<HonganRole>().eq("role_key", role.getRoleKey()));
        if (count > 0) {
            throw new BaseException(BaseError.RoleKeyRepeat);
        }
        role.setRoleKey(UUID.randomUUID().toString());
        role.insert();
        role.setRoleKey("ROLE-" + role.getId());
        role.updateById();
        //保存角色的菜单权限和访问权限
        roleMenuService.saveRoleMenu(role);
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    public void update(HonganRole role) throws BaseException {
        HonganRole curRole = getRoleById(role.getId());
        if (curRole.getIsDefault()) throw new BaseException(BaseError.ProhibitSettingSuperRole);
        checkData(role);
        role.setRoleKey(null);
        role.updateById();
        //保存角色的菜单权限和访问权限
        roleMenuService.saveRoleMenu(role);
    }

    private void checkData(HonganRole data) throws BaseException {
        DataCheckUtils.checkParam(data.getName(), BaseError.PleaseInputName);
        DataCheckUtils.checkParam(data.getRoleType(), BaseError.BadRequest);
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    public void delete(Long id) throws BaseException {
        HonganRole honganRole = getRoleById(id);
        if (honganRole.getIsDefault()) {
            throw new BaseException(BaseError.DefaultRoleProhibitDelete);
        }
        //如果存在管理员，禁止删除
        long count = adminService.count(new QueryWrapper<HonganAdmin>().eq("role_id", id));
        if (count > 0) {
            throw new BaseException(BaseError.RoleExistUserProhibitDelete);
        }
        baseMapper.deleteById(id);
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    public void copyRole(Long id) throws BaseException {
        HonganRole curRole = getRoleById(id);
        curRole.setMenuIds(roleMenuService.getRoleMenuIds(id));
        curRole.setIsDefault(false);
        curRole.setName(curRole.getName() + "_copy");
        curRole.setId(null);
        curRole.insert();
        curRole.setRoleKey("ROLE-" + curRole.getId());
        curRole.updateById();
        //保存角色的菜单权限和访问权限
        roleMenuService.saveRoleMenu(curRole);
    }

}
