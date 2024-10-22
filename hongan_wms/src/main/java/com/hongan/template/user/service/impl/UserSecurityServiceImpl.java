package com.hongan.template.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hongan.template.base.security.IUserService;
import com.hongan.template.user.entity.HonganUser;
import com.hongan.template.base.entity.BaseError;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.base.security.ITokenUser;
import com.hongan.template.base.utils.ClientType;
import com.hongan.template.security.common.TokenUser;
import com.hongan.template.user.mapper.HonganUserMapper;
import com.hongan.template.user.service.IHonganUserService;
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

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author user
 * @since 2020-07-30
 */
@Service
@CacheConfig(cacheNames = "CACHE:CUSTOMER:USER")
public class UserSecurityServiceImpl extends ServiceImpl<HonganUserMapper, HonganUser> implements IUserService<HonganUser> {

    static final String[] BASE_KEYS = new String[]{"id", "name", "nickname", "phone", "avatar", "status", "gender"};

    @Autowired
    private IHonganUserService userService;

    @Override
    @Cacheable(key = "#id")
    public HonganUser getByIdWithKeys(Long id, String... keys) {
        if (keys.length == 0) {
            keys = BASE_KEYS;
        }
        return baseMapper.selectOne(new QueryWrapper<HonganUser>()
                .select(keys).eq("id", id).eq("status", ServiceConst.STATUS_USER_OK));
    }

    @Override
    // @Cacheable(key = "#name")
    public HonganUser getByName(String name, String... keys) {
        if (keys.length == 0) {
            keys = new String[]{"*"};
        }
        return baseMapper.selectOne(new QueryWrapper<HonganUser>()
                .select(keys).eq("name", name));
    }

    @Override
    public ITokenUser getTokenByName(String name, ClientType clientType) throws BaseException {
        HonganUser user = getByName(name);
        return TokenUser.fromUser(user, clientType);
    }

    @Override
    public HonganUser getByPhone(String phone, String... keys) {
        if (keys.length == 0) {
            keys = BASE_KEYS;
        }
        return baseMapper.selectOne(new QueryWrapper<HonganUser>()
                .select(keys).eq("phone", phone).eq("status", ServiceConst.STATUS_USER_OK));
    }

    @Override
    public HonganUser getByEmail(String email, String... keys) {
        if (keys.length == 0) {
            keys = BASE_KEYS;
        }
        return baseMapper.selectOne(new QueryWrapper<HonganUser>()
                .select(keys).eq("email", email).eq("status", ServiceConst.STATUS_USER_OK));
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    public Boolean updatePassword(Long id, String newPassword) {
        HonganUser user = getByIdWithKeys(id, "name", "id");
        return updatePassword(user, newPassword);
    }

    @Override
    public Boolean exist(String filed, Object value) {
        return count(new QueryWrapper<HonganUser>().eq(!StringUtils.isEmpty(filed), filed, value)) > 0;
    }

    // @Override
    public ITokenUser namePasswordLogin(String name, String password, ClientType clientType) throws BaseException {
        ITokenUser user = getTokenByName(name, clientType);
        if (user == null) {
            throw new BaseException(BaseError.DBNotFoundUser);
        }
        if (!checkPassword(user, password)) {
            throw new BaseException(BaseError.PasswordError);
        }
        return user;
    }

    // @Override
    public ITokenUser phoneCaptchaLogin(String phone, ClientType clientType) throws BaseException {
        HonganUser user = getByPhone(phone);
        if (user == null) {
            throw new BaseException(BaseError.DBNotFoundUser);
        }

        return TokenUser.fromUser(user, clientType);
    }

    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    public Boolean updatePassword(HonganUser user, String newPassword) {
        user.setPassword(encryptPassword(newPassword));
        return updateUserById(user);
    }

    @Override
    public Boolean checkPassword(ITokenUser user, String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(password, user.getPassword());
    }

    @Override
    public HonganUser checkPassword(String name, String password) throws BaseException {
        HonganUser user = getByName(name);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(password, user.getPassword())) {
            throw new BaseException(BaseError.PasswordError);
        }
        return user;
    }

    @Override
    public String encryptPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    public Boolean updatePasswordByPhone(String phone, String newPassword) {
        HonganUser user = getByPhone(phone, "id", "name");
        return updatePassword(user, newPassword);
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    public Boolean updatePasswordByEmail(String name, String newPassword) {
        HonganUser user = getByName(name);
        return updatePassword(user, newPassword);
    }


    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    public Boolean updatePhone(Long id, String newPhone) {
        HonganUser user = getByIdWithKeys(id, "id, name");
        user.setPhone(newPhone);
        return updateUserById(user);
    }

    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @CacheEvict(key = "#user.name")
    @Override
    public Boolean updateUserById(HonganUser user) {
        return updateById(user);
    }

    @Override
    @CacheEvict(key = "#user.name")
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    public boolean save(HonganUser user) {
        return super.save(user);
    }

    static class ServiceConst {
        static final int STATUS_USER_OK = 0;
        static final int STATUS_USER_LOCK = 1;
    }

}
