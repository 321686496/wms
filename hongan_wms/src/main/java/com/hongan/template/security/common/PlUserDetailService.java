package com.hongan.template.security.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hongan.template.base.entity.BaseError;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.base.security.ITokenUser;
import com.hongan.template.base.security.IUserService;
import com.hongan.template.admin.entity.HonganAdmin;
import com.hongan.template.user.entity.HonganUser;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2020-02-24 16:07
 * @Description: UserDetailService
 */

@Service
public class PlUserDetailService implements UserDetailsService {

    @Autowired
    private IUserService<HonganAdmin> adminService;
    @Autowired
    private IUserService<HonganUser> userService;

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            LoginUsername login = LoginUsername.parseJSON(username);
            switch (login.getUserType()) {
                case AdminType:
                    ITokenUser admin = adminService.getTokenByName(login.getUsername(), login.getClientType());
                    if (admin == null) {
                        throw new BaseException(BaseError.AdminNotExist);
                    }
                    return admin;
                case UserType:
                    //用户根据手机号登录
                    ITokenUser user = userService.getTokenByName(login.getUsername(), login.getClientType());
                    if (user == null) {
                        throw new UsernameNotFoundException("账号不存在！");
                    }
                    return user;
                default:
                    throw new BaseException(BaseError.LoginUserTypeError);
            }
        } catch (JsonProcessingException | BaseException e) {
            e.printStackTrace();
            throw new BaseException(20088, e.getMessage());
        }
    }
}
