package com.hongan.template.security.defender;

import com.hongan.template.base.defender.FrequencyDefender;
import com.hongan.template.base.defender.Matcher;
import com.hongan.template.base.service.IRedisValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ImgFrequencyDefender extends FrequencyDefender {

    @Autowired
    public ImgFrequencyDefender(IRedisValue<Integer> redisValue) {
        this.redisValue = redisValue;
    }

    @Override
    public int getSessionThreshold() {
        return 10;
    }

    @Override
    public int getIpThreshold() {
        return 400;
    }

    @Override
    public String getName() {
        return "ImgFrequencyDefender";
    }

    @Override
    public String getNotify(int minutes) {
        return String.format("触发图片验证码接口保护，请 %d 分后再试", minutes);
    }

    @Override
    public Matcher getMatcher() {
        // 验证接口和发送接口都要保护
        String phonePath = String.format("^%s.*", "/base/imgCaptcha");
        return new Matcher(phonePath, null, true);
    }

    @Override
    public int oneCircle() {
        return 60;
    }

}
