package com.hongan.template.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hongan.template.enums.AdminStatus;
import com.hongan.template.admin.entity.HonganAdmin;
import com.hongan.template.admin.entity.HonganDept;
import com.hongan.template.admin.entity.HonganRole;
import com.hongan.template.admin.mapper.HonganAdminMapper;
import com.hongan.template.admin.service.IHonganDeptService;
import com.hongan.template.admin.service.IHonganRoleService;
import com.hongan.template.base.entity.BaseError;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.base.security.ITokenUser;
import com.hongan.template.base.security.IUserService;
import com.hongan.template.base.utils.ClientType;
import com.hongan.template.enums.ModuleType;
import com.hongan.template.security.common.TokenUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
//@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, readOnly = true, rollbackFor = Exception.class)
@CacheConfig(cacheNames = "CACHE:SECURITY:ADMIN")
public class AdminSecurityServiceImpl extends ServiceImpl<HonganAdminMapper, HonganAdmin> implements IUserService<HonganAdmin> {
    @Autowired
    private IHonganRoleService roleService;
//    @Autowired
//    private IHonganDeptService deptService;
    static final String[] BASE_KEYS = new String[]{"id", "role_id", "dept_id", "is_initial", "name", "phone",
            "status", "password", "real_name", "avatar", "sex", "email", "login_ip", "login_date",
            "pwd_update_date", "remark", "created_at", "deleted"
    };

    @Override
    public HonganAdmin getByIdWithKeys(Long id, String... keys) {
        if (keys.length == 0) {
            keys = new String[]{"*"};
        }
        return baseMapper.selectOne(new QueryWrapper<HonganAdmin>()
                .select(keys).eq("id", id).eq("status", ServiceConst.STATUS_USER_OK));
    }

    @Override
    @Cacheable(key = "#name")
    public HonganAdmin getByName(String name, String... keys) throws BaseException {
        if (keys.length == 0) {
            keys = BASE_KEYS;
        }
        HonganAdmin admin = baseMapper.selectOne(new QueryWrapper<HonganAdmin>()
                .select(keys)
                .eq("phone", name));
//        HonganDept dept = deptService.selectById(admin.getDeptId());
//        admin.setDeptName(dept.getName());
        //设置角色信息
        HonganRole role = roleService.getById(admin.getRoleId());
        admin.setRole(role.getRoleType());
        admin.setRoleKey(role.getRoleKey());
        admin.setRoleName(role.getName());
        return admin;
    }

    @Override
    public ITokenUser getTokenByName(String name, ClientType clientType) throws BaseException {
        HonganAdmin admin = getByName(name);
        if (admin == null) {
            return null;
        }
        return TokenUser.fromUser(admin, clientType);
    }

    @Override
    public HonganAdmin getByPhone(String phone, String... keys) throws BaseException {
        if (keys.length == 0) {
            keys = BASE_KEYS;
        }
        HonganAdmin admin = baseMapper.selectOne(new QueryWrapper<HonganAdmin>()
                .select(keys).eq("phone", phone));
        if (admin == null) throw new BaseException(BaseError.AdminNotExist);
        return admin;
    }


    @Override
    public HonganAdmin getByEmail(String email, String... keys) {
        if (keys.length == 0) {
            keys = BASE_KEYS;
        }
        return baseMapper.selectOne(new QueryWrapper<HonganAdmin>()
                .select(keys).eq("email", email).eq("status", ServiceConst.STATUS_USER_OK));
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    public Boolean updatePassword(Long id, String newPassword) {
        HonganAdmin honganAdmin = getByIdWithKeys(id, "name", "id");
        return updatePassword(honganAdmin, newPassword);
    }

    @Override
    public Boolean exist(String filed, Object value) {
        return count(new QueryWrapper<HonganAdmin>().eq(!StringUtils.isEmpty(filed), filed, value)) > 0;
    }

    // @Override
    public ITokenUser namePasswordLogin(String name, String password, ClientType clientType) throws BaseException {
        ITokenUser admin = getTokenByName(name, clientType);
        if (admin == null) {
            throw new BaseException(BaseError.DBNotFoundUser);
        }
        if (!checkPassword(admin, password)) {
            throw new BaseException(BaseError.PasswordError);
        }
        return admin;
    }

    // @Override
    public ITokenUser phoneCaptchaLogin(String phone, ClientType clientType) throws BaseException {
        HonganAdmin honganAdmin = getByPhone(phone);
        if (honganAdmin == null) {
            throw new BaseException(BaseError.DBNotFoundUser);
        }
        return TokenUser.fromUser(honganAdmin, clientType);
    }


    @CacheEvict(key = "#honganAdmin.name")
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    public Boolean updatePassword(HonganAdmin honganAdmin, String newPassword) {
        honganAdmin.setPassword(encryptPassword(newPassword));
        return updateUserById(honganAdmin);
    }

    @Override
    public Boolean checkPassword(ITokenUser user, String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(password, user.getPassword());
//        return password.equals(user.getPassword());
    }

    @Override
    public HonganAdmin checkPassword(String phone, String password) throws BaseException {
        HonganAdmin admin = baseMapper.selectOne(new QueryWrapper<HonganAdmin>()
                .eq("phone", phone)
                .select("id", "password", "phone", "name", "role_id", "dept_id", "status")
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
    public String encryptPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
//        return password;
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    public Boolean updatePasswordByPhone(String phone, String newPassword) throws BaseException {
        HonganAdmin honganAdmin = getByPhone(phone, "id", "name");
        return updatePassword(honganAdmin, newPassword);
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    public Boolean updatePasswordByEmail(String name, String newPassword) throws BaseException {
        HonganAdmin honganAdmin = getByName(name);
        return updatePassword(honganAdmin, newPassword);
    }


    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    public Boolean updatePhone(Long id, String newPhone) {
        HonganAdmin honganAdmin = getByIdWithKeys(id, "id, name");
        honganAdmin.setPhone(newPhone);
        return updateUserById(honganAdmin);
    }

    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @CacheEvict(key = "#honganAdmin.name")
    @Override
    public Boolean updateUserById(HonganAdmin honganAdmin) {
        return updateById(honganAdmin);
    }

    @Override
    @CacheEvict(key = "#honganAdmin.name")
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    public boolean save(HonganAdmin honganAdmin) {
        return super.save(honganAdmin);
    }

    static class ServiceConst {
        static final int STATUS_USER_OK = 0;
        static final int STATUS_USER_LOCK = 1;
    }
}
