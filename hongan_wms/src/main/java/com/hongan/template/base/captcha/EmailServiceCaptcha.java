package com.hongan.template.base.captcha;

import com.hongan.template.base.configure.EmailServerConfigure;
import com.hongan.template.base.properties.AppProperties;
import com.hongan.template.base.service.impl.RedisValueTmpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @CreateTime: 2020-02-31 16:30
 * @Description: EmailCaptchaService
 * @Author: zhangxd
 * @Version:
 */

@Component
public class EmailServiceCaptcha extends AbstractCaptcha {

    final
    EmailServerConfigure emailServer;

    @Autowired
    EmailServiceCaptcha(RedisValueTmpl<String> cacheValue, AppProperties bitProperties, EmailServerConfigure emailServer) {
        this.setNameSpace(bitProperties.getCaptcha().getNameSpace());
        this.setCaptchaConfig(bitProperties.getCaptcha().getEmail());
        this.setCacheValue(cacheValue);
        this.emailServer = emailServer;
    }

    private static final int CaptchaLen = 6;

    private String setCaptcha(String email, String captcha) {
        return email + ":" + captcha;
    }

    public String send(HttpServletRequest request, String email, String origin) {
        String num = RandomStringUtils.randomNumeric(CaptchaLen);
        EmailService service = new EmailService(emailServer.getHost(), emailServer.getDefaultUserEmail(), email, emailServer.getSubject(), getNotifyTemp());
        service.send(origin, num);
        this.saveCaptcha(request, setCaptcha(email, num));
        return num;
    }

    public Boolean verify(HttpServletRequest request, String email, String captcha) {
        return this.verifyCaptcha(request, setCaptcha(email, captcha));
    }

    public Boolean verifyAndSave(HttpServletRequest request, String email, String captcha) {
        return this.verifyCaptchaAndSave(request, setCaptcha(email, captcha));
    }

    public Boolean checkVerifiedEmail(HttpServletRequest request, String email, String captcha) {
        return checkSavedVerify(request, setCaptcha(email, captcha));
    }

    @Slf4j
    @AllArgsConstructor
    static class EmailService {
        private String host;
        private String fromEmail;
        private String toEmail;
        private String subject;
        private String temp;

        static final Map<String, String> actions = new HashMap<>();

        static {
            actions.put("verify", "账号身份验证");
            actions.put("signup", "用户注册");
            actions.put("resetPhone", "重置手机号码");
            actions.put("resetPassword", "重置密码的操作");
        }

        public void send(String origin, String code) {
            try {
                MimeMessage message = getMessage(origin, code);
                Transport.send(message);
                log.info("Sent message successfully....");
            } catch (MessagingException mex) {
                mex.printStackTrace();
            }
        }

        MimeMessage getMessage(String origin, String code) throws MessagingException {
            Properties properties = System.getProperties();
            // 设置邮件服务器
            properties.setProperty("mail.smtp.host", host);
            // 获取默认session对象
            Session session = Session.getDefaultInstance(properties);
            // 创建默认的 MimeMessage 对象
            MimeMessage message = new MimeMessage(session);
            // Set From: 头部头字段 发件人
            message.setFrom(new InternetAddress(fromEmail));
            // Set To: 头部头字段 收件人
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            // setSubject 设置主题
            message.setSubject(subject);
            // 设置消息体
            message.setContent(template(origin, code, toEmail), "text/html; charset=utf-8");
            message.setSentDate(new Date());

            return message;
        }

        String template(String origin, String code, String email) {
            String action = actions.getOrDefault(origin, actions.get("verify"));

            return String.format(temp, email, action, code);
        }

    }

    private String getNotifyTemp() {
        return String.format("<section style='font-size: 18px; font-weight: 400; max-width: 600px; margin: 0 auto;'>\n" +
                "  <div style='height: 76px; background-color: #408ff0;'>\n" +
                "    <a href='%s' target='_blank'>\n" +
                "      <img src='%s' alt='logo' style='height: 76px'>\n" +
                "    </a>\n" +
                "  </div>\n", emailServer.getHomeUrl(), emailServer.getLogoUrl()) +
                "  <p>尊敬的%s，您好！您正在进行%s，验证码如下所示, 10分钟内有效。</p>\n" +
                "  <h2 style='text-align: center; max-width: 600px; font-size: 3rem;'>\n" +
                "    <span style='padding: 10px 20px; background-color: #408ff0; border: 1px solid #60fff0; border-radius: 8px; color: white'>%s</span>\n" +
                "  </h2>\n" +
                "  <p style='color: #df0000; font-size: 20px'>为了您的账号和财产安全，请妥善保管验证码，打死不要告诉别人!! </p>\n" +
                "  <p>若非本人操作请忽略。</p>\n" +
                "</section>";
    }

}

