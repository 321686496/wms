package com.hongan.template.base.security;

import com.hongan.template.base.entity.BaseException;
import com.hongan.template.base.utils.ClientType;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2019-12-28 09:24
 * @Description: IUserService
 */


public interface IUserService<T> {

    T getByIdWithKeys(Long id, String... keys);

    T getByName(String name, String... keys) throws BaseException;

    ITokenUser getTokenByName(String name, ClientType clientType) throws BaseException;

    T getByPhone(String phone, String... keys) throws BaseException;

    T getByEmail(String email, String... keys);

    Boolean updatePassword(Long id, String newPassword);

    Boolean checkPassword(ITokenUser user, String password) throws BaseException;

    T checkPassword(String name, String password) throws BaseException;

    String encryptPassword(String password);

    Boolean updatePasswordByPhone(String phone, String newPassword) throws BaseException;

    Boolean updatePasswordByEmail(String email, String newPassword) throws BaseException;

    Boolean updatePhone(Long id, String newPhone);

    Boolean exist(String filed, Object value);

    Boolean updateUserById(T user);

//    TokenUser namePasswordLogin(String name, String password) throws BaseException;
//
//    TokenUser phoneCaptchaLogin(String phone) throws BaseException;

}
