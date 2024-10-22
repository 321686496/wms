package com.hongan.template.base.captcha;

import com.hongan.template.base.properties.CacheProperties;
import com.hongan.template.base.service.IRedisValue;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2019-12-28 09:24
 * @Description: ImgAbstractCaptcha
 */
@Data
@Slf4j
public abstract class AbstractCaptcha {

    private static final String CAPTCHANAME = "CAPTCHANAME";
    private String nameSpace;
    private CacheProperties captchaConfig;
    private IRedisValue<String> cacheValue;

    // 请求验证码后保存缓存以待验证
    protected void saveCaptcha(HttpServletRequest request, String captcha) {
        String key = getCaptchaKey(request);
        int t = captchaConfig.getExpires();
        log.debug(key + "=====>>获取的验证码：【" + captcha + "】");
        cacheValue.setValue(key, captcha, t);
    }

    // 验证验证码，成功返回true
    public Boolean verifyCaptcha(HttpServletRequest request, String captcha) {
        String key = getCaptchaKey(request);
        String saved = cacheValue.getValue(key);
        log.debug(key + " =====>>保存的验证码：【{}】,提交的验证码：【{}】", saved, captcha);
        Boolean aBoolean = checkEquel(saved, captcha);
        if (aBoolean) {
            // 验证码如果正确立即失效，防止攻击
            cacheValue.delete(key);
        }
        return aBoolean;
    }

    // 验证验证码，成功返回true 并且保存验证凭证
    public Boolean verifyCaptchaAndSave(HttpServletRequest request, String captcha) {
        Boolean verified = verifyCaptcha(request, captcha);
        if (verified) {
            saveVerify(request, captcha);
        }
        return verified;
    }

    // 获取验证通过后的凭证值
    protected String getSavedVerifyCaptcha(HttpServletRequest request) {
        String key = getSavedVerifyKey(request);
        String captcha = cacheValue.getValue(key);
        cacheValue.delete(key);
        return captcha;
    }


    // 检查是否存在验证成功的凭证
    public boolean checkSavedVerify(HttpServletRequest request, String captcha) {
        String key = getSavedVerifyKey(request);
        String saved = cacheValue.getValue(key);
        return checkEquel(saved, captcha);
    }


    protected Boolean checkEquel(String v1, String v2) {
        if (v1 == null || v2 == null) {
            return false;
        }
        return v1.toLowerCase().equals(v2.toLowerCase());
    }

    // 保存验证成功的凭证
    private void saveVerify(HttpServletRequest request, String captcha) {
        String key = getSavedVerifyKey(request);
        int t = captchaConfig.getExpires();
        cacheValue.setValue(key, captcha, t);
    }

    private String getSessionId(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String captchaId = (String) session.getAttribute(CAPTCHANAME);
        if (captchaId == null) {
            captchaId = UUID.randomUUID().toString();
            session.setAttribute(CAPTCHANAME, captchaId);
        }
        return captchaId;
    }

    private String getCaptchaKey(HttpServletRequest request) {
        String sessionId = getSessionId(request);
        return nameSpace + ":" + captchaConfig.getCacheName() + ":" + sessionId;
    }

    private String getSavedVerifyKey(HttpServletRequest request) {
        String sessionId = getSessionId(request);
        return nameSpace + ":" + captchaConfig.getVerifyName() + ":" + sessionId;
    }


}
