package com.hongan.template.base.captcha;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hongan.template.base.properties.AppProperties;
import com.hongan.template.base.service.impl.RedisValueTmpl;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.external.service.IExternalTencentCloudSmsService;
import com.hongan.template.external.enums.SmsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2019-12-02 15:23
 * @Description: PhoneCaptchaService
 */

/**
 * 发送手机短信服务
 */
@Slf4j
@Component
public class PhoneServiceCaptcha extends AbstractCaptcha {

    final RedisValueTmpl<String> cacheValue;
    //存储短信类型及短信模板Id
    @Autowired
    private IExternalTencentCloudSmsService tencentCloudSmsService;
    @Value("${spring.profiles.active}")
    private String path;

    /**
     * 注入属性
     *
     * @param cacheValue
     * @param AppProperties
     */
    @Autowired
    PhoneServiceCaptcha(RedisValueTmpl<String> cacheValue, AppProperties AppProperties) {
        this.setNameSpace(AppProperties.getCaptcha().getNameSpace());
        this.setCaptchaConfig(AppProperties.getCaptcha().getPhone());
        this.setCacheValue(cacheValue);
        //装配短信模板信息
        this.cacheValue = cacheValue;
    }

    /**
     * 发送验证码验证短信服务调用
     *
     * @param request
     * @param phone    手机号
     * @param template 短信模板
     * @return 返回发送结果
     */
    public Boolean sendCaptcha(HttpServletRequest request, String phone, SmsTemplate template) throws JsonProcessingException, BaseException {
        //生成验证码
        String code = RandomStringUtils.randomNumeric(CaptchaLen);
        //构造发送参数
        Map<String, String> map = new HashMap();
        map.put("code", code);
        Boolean result = true;
        if (!path.startsWith("dev")) {
            //发送验证码
            result = tencentCloudSmsService.sendWithTemplate(phone, template, new String[]{map.get("code")});
        } else {
            code = "123456";
        }
//        if (path.equals("prod")) {//生产环境下发送手机验证码
//            发送验证码
//            result = tencentCloudSmsService.sendWithTemplate(phone, template, new String[]{map.get("code")});
//        } else {
//            code = "123456";
//        }
        this.saveCaptcha(request, setCaptcha(phone, code));
        return result;
    }

    /**
     * 验证码长度
     */
    private static final int CaptchaLen = 6;

    /**
     * 设置验证码
     *
     * @param phone   手机号
     * @param captcha 验证码
     * @return
     */
    private String setCaptcha(String phone, String captcha) {
        return phone + ":" + captcha;
    }


    /**
     * 验证
     *
     * @param request
     * @param phone
     * @param captcha
     * @return
     */
    public Boolean verify(HttpServletRequest request, String phone, String captcha) {
        return this.verifyCaptcha(request, setCaptcha(phone, captcha));
    }

    public Boolean verifyAndSave(HttpServletRequest request, String phone, String captcha) {
        return this.verifyCaptchaAndSave(request, setCaptcha(phone, captcha));
    }

    public Boolean checkVerifiedPhone(HttpServletRequest request, String phone) {
        String savedCaptcha = this.getSavedVerifyCaptcha(request);
        return savedCaptcha.substring(0, savedCaptcha.indexOf(":")).equals(phone);
    }

    //根据短信类型获取短信模板Id
    private String getTemplateId(PhoneNotifyType phoneNotifyType, List<Map<String, String>> values) {
        for (Map<String, String> item : values) {
            String notifyTypeStr = phoneNotifyType.getType();
            if (item.get("name").equals(notifyTypeStr))
                return item.get("templateId");
        }
        return null;
    }

}

