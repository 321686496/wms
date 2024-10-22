package com.hongan.template.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hongan.template.admin.query.QueryMenu;
import com.hongan.template.admin.entity.HonganMenu;
import com.hongan.template.admin.entity.HonganRole;
import com.hongan.template.admin.entity.HonganRoleMenu;
import com.hongan.template.enums.ModuleType;
import com.hongan.template.admin.mapper.HonganMenuMapper;
import com.hongan.template.admin.service.IHonganMenuService;
import com.hongan.template.admin.service.IHonganRoleMenuService;
import com.hongan.template.admin.service.IHonganRoleService;
import com.hongan.template.base.entity.BaseError;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.base.vo.TreeDataVo;
import com.hongan.template.config.service.IHonganConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhangxd
 * @since 2020-02-16
 */
@Service
@Slf4j
@CacheConfig(cacheNames = "CACHE:SECURITY:MENU")
public class HonganMenuServiceImpl extends ServiceImpl<HonganMenuMapper, HonganMenu> implements IHonganMenuService {

    @Autowired
    private IHonganRoleService roleService;
    @Autowired
    private IHonganRoleMenuService roleMenuService;
    @Autowired
    private IHonganConfigService systemConfigService;

    @Override
    public List<HonganMenu> queryList(QueryMenu query) {
        if (query.getPid() == null) query.setPid(0L);
        return baseMapper.selectList(query.toWrapper());
    }

    @Override
    public List<HonganMenu> queryMenu(QueryMenu query) throws BaseException {
        //查询一级菜单
        query.setPid(0L);
        return getChildren(query);
    }

    //迭代获取所有菜单关系
    private List<HonganMenu> getChildren(QueryMenu query) throws BaseException {
        List<HonganMenu> list = baseMapper.selectList(query.toWrapper());
        for (HonganMenu menu : list) {
            query.setPid(menu.getId());
            List<HonganMenu> children = getChildren(query);
            if (children.size() > 0) {
                menu.setChildren(children);
            }
        }
        return list;
    }

    @Override
    public List<TreeDataVo> queryMenuSelectData(QueryMenu query) throws BaseException {
        //查询一级菜单
        query.setPid(0L);
        return getTreeChildren(query);
    }

    @Override
    public HonganMenu selectById(Long id) throws BaseException {
        if (null == id) throw new BaseException(BaseError.BadRequest);
        HonganMenu data = baseMapper.selectById(id);
        if (null == data) throw new BaseException(BaseError.DataNotExist);
        return data;
    }

    @Override
    public List<TreeDataVo> getTenantMenuTree(QueryMenu query) throws BaseException {
        //查询一级菜单
        query.setPid(0L);
        return getTreeChildren(query);
    }

    @Override
    public List<TreeDataVo> getAdminMenuTree(QueryMenu query) throws BaseException {
        //查询角色信息
        HonganRole role = roleService.getOne(new QueryWrapper<HonganRole>()
                .eq("role_key", query.getRoleKey()));
        if (!role.getRoleType().equals("SUPER")) {
            List<Long> menuIds = roleMenuService.getRoleMenuIds( role.getId());
            query.setInIds(menuIds);
        }
        //查询一级菜单
        query.setPid(0L);
        query.setEnable(true);
        return getTreeChildren(query);
    }


    //迭代获取所有菜单关系
    private List<TreeDataVo> getTreeChildren(QueryMenu query) throws BaseException {
        List<TreeDataVo> treeDataVos = new ArrayList<>();
        List<HonganMenu> list = baseMapper.selectList(query.toWrapper());
        for (HonganMenu menu : list) {
            TreeDataVo vo = new TreeDataVo();
            vo.setLabel(menu.getName());
            vo.setValue(menu.getId().toString());
            vo.setMenuType(menu.getMenuType());
            query.setPid(menu.getId());
            List<TreeDataVo> children = getTreeChildren(query);
            if (children.size() > 0) {
                vo.setChildren(children);
            }
            treeDataVos.add(vo);
        }
        return treeDataVos;
    }

    @Override
    public List<String> getMenuPermsByIds(List<Long> ids) {
        if (ids == null || ids.size() < 1) return null;
        return baseMapper.getMenuPermsByIds(ids);
    }


    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public void create(HonganMenu menu) {
        menu.insert();
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public void update(HonganMenu menu) throws BaseException {
        if (menu.getId() == null) throw new BaseException(BaseError.BadRequest);
        baseMapper.updateById(menu);
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public void updateStatus(Long id) throws BaseException {
        HonganMenu menu = selectById(id);
        menu.setEnable(!menu.getEnable());
        menu.updateById();
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public void delete(Long id) {
        baseMapper.deleteById(id);
    }

    @Override
    public List<HonganMenu> getMenuTree(String roleKey) throws BaseException {
        QueryMenu query = new QueryMenu();
        //查询角色信息
        HonganRole role = roleService.getOne(new QueryWrapper<HonganRole>()
                .eq("role_key", roleKey));
        if (!role.getRoleType().equals("SUPER")) {
            List<HonganMenu> roleMenu = roleMenuService.getRoleMenu( role.getId());
            return getTree(roleMenu, 0L);
        } else {
            //查询一级菜单
            query.setPid(0L);
            query.setEnable(true);
            return getChildren(query);
        }
    }

    private List<HonganMenu> getTree(List<HonganMenu> source, Long pid) {
        List<HonganMenu> res = source.stream()
                .filter(obj -> obj.getPid().equals(pid))
                .collect(Collectors.toList());
        for (HonganMenu re : res) {
            List<HonganMenu> children = getTree(source, re.getId());
            re.setChildren(children);
        }
        //根据库存量排序
        Comparator<HonganMenu> comparator = (obj1, obj2) -> obj2.getPriority().compareTo(obj1.getPriority());
        Collections.sort(res, comparator);
        return res;
    }
}
