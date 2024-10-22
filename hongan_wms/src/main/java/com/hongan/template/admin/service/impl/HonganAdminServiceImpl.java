package com.hongan.template.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hongan.template.admin.entity.HonganDept;
import com.hongan.template.admin.entity.HonganRole;
import com.hongan.template.admin.mapper.HonganAdminMapper;
import com.hongan.template.admin.query.QueryAdmin;
import com.hongan.template.admin.service.IHonganDeptService;
import com.hongan.template.admin.service.IHonganRoleService;
import com.hongan.template.base.entity.BaseError;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.base.security.IUserService;
import com.hongan.template.base.utils.DataCheckUtils;
import com.hongan.template.base.vo.TreeDataVo;
import com.hongan.template.config.service.IHonganConfigService;
import com.hongan.template.enums.AdminStatus;
import com.hongan.template.admin.entity.HonganAdmin;
import com.hongan.template.admin.service.IHonganAdminService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhangxd
 * @since 2019-12-30
 */
@Service
@Slf4j
//@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, readOnly = true, rollbackFor = Exception.class)
@CacheConfig(cacheNames = "CACHE:SECURITY:ADMIN")
public class HonganAdminServiceImpl extends ServiceImpl<HonganAdminMapper, HonganAdmin> implements IHonganAdminService {
    @Autowired
    IUserService<HonganAdmin> userSecurityService;
    @Autowired
    IHonganRoleService roleService;
    @Autowired
    private IHonganDeptService deptService;
    @Autowired
    private IHonganConfigService configService;

    @Override
    public IPage<HonganAdmin> queryPage(QueryAdmin query) throws BaseException {
        Page<HonganAdmin> page = new Page<>(query.getCurrent(), query.getPageSize());
        List<HonganAdmin> list = baseMapper.selectAdminList(query, (query.getCurrent() - 1) * query.getPageSize(), query.getPageSize());
        for (HonganAdmin admin : list) {
//            admin.setDeptName(deptService.selectByIdXml(admin.getDeptId()).getName());
        }
        page.setRecords(list);
        page.setCurrent(query.getCurrent());
        page.setSize(query.getPageSize());
        page.setTotal(baseMapper.selectAdminListCount(query));
        return page;
    }

    @Override
    public List<TreeDataVo> querySelectList(QueryAdmin query) throws BaseException {
        List<HonganAdmin> list = baseMapper.selectList(query.toWrapper());
        List<TreeDataVo> res = new ArrayList<>();
        for (HonganAdmin data : list) {
            res.add(new TreeDataVo(data.getRealName(), data.getId() + ""));
        }
        return res;
    }

    @Override
    public HonganAdmin getById(Long id, String... keys) throws BaseException {
        if (null == id) throw new BaseException(BaseError.BadRequest);
        if (keys == null || keys.length < 1) keys = new String[]{"*"};
        HonganAdmin data = baseMapper.selectOne(new QueryWrapper<HonganAdmin>().select(keys).eq("id", id));
        //校验数据是否存在
        DataCheckUtils.checkData(data);
        return data;
    }

    @Override
    public HonganAdmin getDetailById(Long id, String... keys) throws BaseException {
        HonganAdmin admin = getById(id, keys);
//        HonganDept dept = deptService.selectById(admin.getDeptId());
//        admin.setDeptName(dept.getName());
        //设置角色信息
        HonganRole role = roleService.getById(admin.getRoleId());
        admin.setRole(role.getRoleType());
        admin.setRoleKey(role.getRoleKey());
        return admin;
    }

    @Override
    public HonganAdmin getShouAdminDataById(Long id, String... keys) throws BaseException {
        if (keys == null || keys.length < 1) keys = new String[]{"*"};
        HonganAdmin data = baseMapper.selectOne(new QueryWrapper<HonganAdmin>()
                .select(keys).eq("id", id));
        if (data == null) {
            data = new HonganAdmin();
            data.setAvatar("");
        }
        return data;
    }

    @Override
    public HonganAdmin getByIdXml(Long id) throws BaseException {
        if (null == id) throw new BaseException(BaseError.BadRequest);
        HonganAdmin data = baseMapper.selectByIdXml(id);
        //校验数据是否存在
        DataCheckUtils.checkData(data);
        return data;
    }


    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @CacheEvict(key = "#data.phone")
    public String insert(HonganAdmin data) throws Exception {
        checkData(data);
        //校验角色信息
        roleService.getRoleById(data.getRoleId());
        //校验部门信息
//        deptService.selectById(data.getDeptId());
        //判断该手机号是否存在
        HonganAdmin admin = baseMapper.selectOne(new QueryWrapper<HonganAdmin>()
                .eq("phone", data.getPhone()));
        if (admin != null) {
            //如果租户ID一致 则表示该手机号已注册
            throw new BaseException(BaseError.PhoneIsRegistered);
        }
        if (StringUtils.isEmpty(data.getAvatar())) {
            data.setAvatar(configService.getValue("system_default_avatar"));
        }
        //如果没有密码，则随机生成密码
        String password = data.getPwd();
        if (StringUtils.isEmpty(password)) {
            password = RandomStringUtils.randomAlphanumeric(10) + RandomStringUtils.randomNumeric(2);
        } else {
            //如果有 则校验密码格式
            DataCheckUtils.checkParamRegex(password, configService.getValue("system_regex_password"), BaseError.PasswordFormatError);
        }
        data.setName(data.getPhone());
        data.setPassword(encryptPassword(password));
        data.insert();
        return password;
    }

    private void checkData(HonganAdmin data) throws BaseException {
        DataCheckUtils.checkParam(data.getRealName(), BaseError.PleaseInputName);
        DataCheckUtils.checkParam(data.getPhone(), BaseError.PleaseInputPhone);
        DataCheckUtils.checkParam(data.getRoleId(), BaseError.PleaseSelectRole);
//        DataCheckUtils.checkParam(data.getDeptId(), BaseError.PleaseSelectDept);
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @CacheEvict(key = "#data.phone")
    public void update(HonganAdmin data) throws BaseException {
        DataCheckUtils.checkParam(data.getId(), BaseError.BadRequest);
        checkData(data);
        //校验角色信息
        roleService.getRoleById(data.getRoleId());
        //校验部门信息
//        deptService.selectById(data.getDeptId());
        //查询管理员信息
        HonganAdmin admin = baseMapper.selectOne(new QueryWrapper<HonganAdmin>().eq("id", data.getId()));
        if (admin == null) throw new BaseException(BaseError.BadRequest);

        if (!admin.getPhone().equals(data.getPhone())) {
            //判断该手机号是否存在
            HonganAdmin existPhone = baseMapper.selectOne(new QueryWrapper<HonganAdmin>()
                    .ne("id", data.getId())
                    .eq("phone", data.getPhone()));
            if (existPhone != null) {
                //如果租户ID一致 则表示该手机号已注册
                throw new BaseException(BaseError.PhoneIsRegistered);
            }
            data.setName(data.getPhone());
        }
        data.updateById();
    }


    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    public String resetPassword(HonganAdmin admin) throws BaseException {
        HonganAdmin data = getById(admin.getId());
        //如果没有密码，则随机生成密码
        String password = admin.getPwd();
        if (StringUtils.isEmpty(password)) {
            password = RandomStringUtils.randomAlphanumeric(10) + RandomStringUtils.randomNumeric(2);
        } else {
            //校验密码是否满足条件
            DataCheckUtils.checkParamRegex(password, configService.getValue("system_regex_password"), BaseError.PasswordFormatError);
        }
        data.setPassword(encryptPassword(password));
        data.setPwdUpdateDate(LocalDateTime.now());
        data.updateById();
        return password;
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    public HonganAdmin updateStatus(Long id) throws BaseException {
        HonganAdmin data = getById( id);
        data.setStatus(data.getStatus() == AdminStatus.NORMAL ? AdminStatus.LOCK : AdminStatus.NORMAL);
        data.updateById();
        return data;
    }

    @Override
    public HonganAdmin checkPassword(String phone, String password) throws BaseException {
        HonganAdmin admin = baseMapper.selectOne(new QueryWrapper<HonganAdmin>()
                .eq("phone", phone)
                .select("id", "password", "phone", "name",  "role_id", "dept_id", "status")
        );
        if (admin == null)
            throw new BaseException(BaseError.AdminNotExist);
        if (admin.getStatus() != AdminStatus.NORMAL)
            throw new BaseException(BaseError.AdminIsLock);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(password, admin.getPassword())) {
            throw new BaseException(BaseError.PasswordError);
        }
        return admin;
    }

    @Override
    public HonganAdmin loginByPhone(String phone) throws BaseException {
        HonganAdmin admin = baseMapper.selectOne(new QueryWrapper<HonganAdmin>()
                .eq("phone", phone)
                .select("id", "password", "phone", "name", "role_id", "dept_id", "status")
        );
        if (admin == null)
            throw new BaseException(BaseError.AdminNotExist);
        if (admin.getStatus() != AdminStatus.NORMAL)
            throw new BaseException(BaseError.AdminIsLock);
        return admin;
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    public void delete( Long id) throws BaseException {
        HonganAdmin data = getById( id);
        if (data.getIsInitial()) throw new BaseException(BaseError.DefaultAdminProhibitDelete);
        data.deleteById();
    }

    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @CacheEvict(key = "#honganAdmin.name")
    public Boolean updateAdmin(HonganAdmin honganAdmin) {
        return honganAdmin.updateById();
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    public void uploadLoginInfo(Long id, String ipAddr) {
        HonganAdmin admin = getById(id);
        admin.setLoginIp(ipAddr);
        admin.setLoginDate(LocalDateTime.now());
        admin.updateById();
    }

    @Override
    @CacheEvict(key = "#honganAdmin.name")
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    public boolean save(HonganAdmin honganAdmin) {
        return super.save(honganAdmin);
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @CacheEvict(key = "#honganAdmin.name")
    public String insertOrUpdate(HonganAdmin honganAdmin) throws Exception {
        String password = null;
        if (honganAdmin.getPassword() == null || "".equals(honganAdmin.getPassword())) {
            if (honganAdmin.getId() == null) {
                password = RandomStringUtils.randomAlphanumeric(10) + RandomStringUtils.randomNumeric(2);
                honganAdmin.setPassword(encryptPassword(password));
            }
        } else {
            // 初始用户不可修改
            if (honganAdmin.getId() != null && count(new QueryWrapper<HonganAdmin>().lt("id", honganAdmin.getId())) < 1) {
                honganAdmin.setName(null);
                honganAdmin.setRoleKey(null);
                honganAdmin.setRole(null);
                honganAdmin.setRoleName(null);
            }
        }
        if (!honganAdmin.insertOrUpdate()) {
            throw new BaseException(BaseError.DBSaveError);
        }
        return password;
    }


    public String encryptPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
}
