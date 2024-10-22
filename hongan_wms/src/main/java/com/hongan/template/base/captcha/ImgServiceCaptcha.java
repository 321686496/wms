package com.hongan.template.base.captcha;

import com.hongan.template.base.properties.AppProperties;
import com.hongan.template.base.service.IRedisValue;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2019-12-28 09:24
 * @Description: ImgCaptchaService
 */
@Slf4j
@Component
public class ImgServiceCaptcha extends AbstractCaptcha {

    @Autowired
    protected ImgServiceCaptcha(IRedisValue<String> cacheValue, AppProperties appProperties) {
        this.setCacheValue(cacheValue);
        this.setCaptchaConfig(appProperties.getCaptcha().getImg());
        this.setNameSpace(appProperties.getCaptcha().getNameSpace());
    }

    private ICaptcha captcha;

    public String captcha(HttpServletRequest request) {
        String[] captcha = captcha();
        this.saveCaptcha(request, captcha[0]);
        return captcha[1];
    }

    public String[] captcha() {
        this.captcha = new GCaptcha();
//        this.captcha.
        return new String[]{this.captcha.text(), this.captcha.toBase64()};
    }

    static abstract class ICaptcha {
        private final Captcha captcha;

        ICaptcha(Captcha cap) {
            this.captcha = cap;
            this.captcha.setCharType(CharType);
            try {
//                int randFont = new Random().nextInt(9);
                int randFont = 1;
                this.captcha.setFont(randFont);
            } catch (Exception e) {
                this.captcha.setFont(new Font("Arial", 1, 32));
            }
        }

        String text() {
            return captcha.text();
        }

        String toBase64() {
            return captcha.toBase64();
        }
    }

    private static final int Width = 130;
    private static final int Height = 48;
    private static final int TextLen = 4;
    private static final int CharType = Captcha.TYPE_ONLY_CHAR;

    public static class GCaptcha extends ICaptcha {
        public GCaptcha() {
            super(new SpecCaptcha(Width, Height, TextLen));
//            super(new GifCaptcha(Width, Height, TextLen)); // 动态验证码
        }
    }
}
