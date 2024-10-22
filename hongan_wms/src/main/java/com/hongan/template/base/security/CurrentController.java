package com.hongan.template.base.security;

import com.hongan.template.base.validation.PasswordValidation;
import com.hongan.template.base.validation.PhoneValidation;
import com.hongan.template.base.captcha.PhoneServiceCaptcha;
import com.hongan.template.base.entity.BaseController;
import com.hongan.template.base.entity.BaseError;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.base.entity.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2019-12-28 09:24
 * @Description: CurrentController
 */

@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class CurrentController<T> extends BaseController {

    @Autowired
    protected IUserService<T> userService;
    protected PhoneServiceCaptcha phoneServiceCaptcha;

    protected BaseResponse current(
            Authentication auth
    ) {
        ITokenUser current = (ITokenUser) auth.getPrincipal();
        log.debug("current: " + current);
        return new BaseResponse().success(current);
    }


    protected BaseResponse detail(
            Authentication auth
    ) {
        ITokenUser current = (ITokenUser) auth.getPrincipal();
        Object user = userService.getByIdWithKeys(current.getId());
        return new BaseResponse().success(user);
    }

    protected BaseResponse updatePassword(Authentication auth, UpdatePassword update) throws BaseException {
        ITokenUser current = (ITokenUser) auth.getPrincipal();
        BaseResponse response = new BaseResponse();

        if (userService.checkPassword(current, update.getOldPassword())) {
            String newPassword = update.getNewPassword();
            if (userService.updatePassword(current.getId(), newPassword)) {
                return response.success();
            }
            return response.error(BaseError.DBError);
        } else {
            throw new BaseException(BaseError.PasswordError);
        }
    }


    @Data
    protected static class UpdatePassword {
        @PasswordValidation
        private String newPassword;
        private String oldPassword;
        private String phone;
        private String phoneCaptcha;
    }

    @Data
    protected static class UpdatePhone {
        @PhoneValidation
        private String newPhone;
        @PhoneValidation
        private String oldPhone;
        private String phoneCaptcha;
    }
}
