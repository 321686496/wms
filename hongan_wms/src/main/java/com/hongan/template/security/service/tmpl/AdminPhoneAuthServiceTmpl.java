package com.hongan.template.security.service.tmpl;

import com.hongan.template.base.captcha.ImgServiceCaptcha;
import com.hongan.template.base.captcha.PhoneServiceCaptcha;
import com.hongan.template.base.captcha.entity.NameStepPhoneCaptcha;
import com.hongan.template.base.entity.BaseError;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.base.utils.CusAccessObjectUtil;
import com.hongan.template.security.common.PlSecurityConstConfig;
import com.hongan.template.security.service.IAuthService;
import com.hongan.template.admin.entity.HonganAdmin;
import com.hongan.template.admin.service.IHonganAdminService;
import com.hongan.template.security.common.SecurityParameters;
import com.hongan.template.security.common.StepAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * 后台管理员登录处理
 * 用户名+密码+验证码
 */

@Service("AdminNameSecurityService")
public class AdminPhoneAuthServiceTmpl extends AbstractAuthService implements IAuthService {

    @Autowired
    ImgServiceCaptcha imgServiceCaptcha;

    @Autowired
    PhoneServiceCaptcha phoneServiceCaptcha;

    @Autowired
    IHonganAdminService adminService;

    SecurityParameters parameters;
    @Value("${spring.profiles.active}")
    private String path;

    @Override
    public void verify(HttpServletRequest request) throws BaseException, StepAuthException {
        NameStepPhoneCaptcha req = readInputStream(request, NameStepPhoneCaptcha.class);
        //1.验证验证码
        if (!path.startsWith("dev")) {
            if (!imgServiceCaptcha.verifyCaptcha(request, req.getImgCaptcha())) {
                throw new BaseException(BaseError.ImgCaptchaError);
            }
        }
        //2.校验用户密码是否正确
        HonganAdmin admin = adminService.checkPassword(req.getName(), req.getPassword());
        //3.更新最后登陆IP和时间
        String ipAddr = CusAccessObjectUtil.getIpAddress(request);
        adminService.uploadLoginInfo(admin.getId(), ipAddr);
        parameters = new SecurityParameters(admin.getName(), admin.getPassword(), request, PlSecurityConstConfig.NameLogin);
    }

    @Override
    public String getMatcher() {
        return PlSecurityConstConfig.NameLogin + ":post";
    }

    @Override
    public int getPriority() {
        return 100;
    }

    @Override
    public boolean hasParameter() {
        return true;
    }

    @Override
    public SecurityParameters getParameters() {
        return parameters;
    }

}
