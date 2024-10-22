package com.hongan.template.security.defender;

import com.hongan.template.base.defender.Matcher;
import com.hongan.template.base.defender.ValidDefender;
import com.hongan.template.base.entity.BaseError;
import com.hongan.template.base.service.IRedisValue;
import com.hongan.template.security.common.PlSecurityConstConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
public class PasswordValidDefender extends ValidDefender {

    @Autowired
    public PasswordValidDefender(IRedisValue<Integer> redisValue) {
        this.redisValue = redisValue;
    }

    @Override
    public int getSessionThreshold() {
        return 3;
    }

    @Override
    public int getIpThreshold() {
        return 10;
    }

    @Override
    public String getName() {
        return "PasswordValidDefender";
    }

    @Override
    public String getNotify(int minutes) {
        return String.format("密码错误次数过多，请 %d 分后再试", minutes);
    }

    @Override
    public Matcher getMatcher() {
        return new Matcher(PlSecurityConstConfig.NameLogin, "post", false);
    }

    @Override
    public boolean checkValid(HttpServletRequest request, HttpServletResponse response) {
        String message = (String) request.getAttribute(PlSecurityConstConfig.AuthErrorKey);
        // 有相关错误返回false
        return !BaseError.PasswordError.getMessage().equals(message);
    }

}
