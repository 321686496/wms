package com.hongan.template.security.service.tmpl;

import com.hongan.template.base.entity.BaseException;
import com.hongan.template.security.service.IAuthService;
import com.hongan.template.user.entity.HonganUser;
import com.hongan.template.user.service.IHonganUserService;
import com.hongan.template.external.vo.WechatVo;
import com.hongan.template.security.common.PlSecurityConstConfig;
import com.hongan.template.security.common.SecurityParameters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service("UserWeChatSecurityService")
public class UserWeChatAuthServiceTmpl extends AbstractAuthService implements IAuthService {

    @Autowired
    private IHonganUserService userService;

    SecurityParameters parameters;

    @Override
    public void verify(HttpServletRequest request) throws BaseException {
        WechatVo weChatVO = readInputStream(request, WechatVo.class);
        log.debug("登录参数：{}", weChatVO.toString());
        // 1) 获取 openid  sessionKey
        HonganUser user = userService.wechatLogin(weChatVO);
        parameters = new SecurityParameters(user.getName(), user.getPassword(), request, PlSecurityConstConfig.UserWechatLogin);
    }

    @Override
    public String getMatcher() {
        return PlSecurityConstConfig.UserWechatLogin + ":post";
    }

    @Override
    public boolean hasParameter() {
        return true;
    }

    @Override
    public SecurityParameters getParameters() {
        return parameters;
    }

    @Override
    public int getPriority() {
        return 0;
    }
}
