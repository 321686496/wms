package com.hongan.template.listener;

import com.hongan.template.base.entity.BaseError;
import lombok.SneakyThrows;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomSessionExpiredHandler implements SessionInformationExpiredStrategy {

    @SneakyThrows
    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException {
        HttpServletRequest request = event.getRequest();
        HttpServletResponse response = event.getResponse();
        BaseError e = BaseError.AuthError;
        response.setContentType("application/json");
        response.getWriter().format("{\"status\": %d,\"message\": \"%s\"}", e.getStatus(), e.getMessage());

//        throw new BaseException(BaseError.AuthError);
//        Authentication auth = event.getSecurityContext().getAuthentication();

        // 根据需要执行自定义的处理逻辑，比如重定向到登录页面或者显示错误消息
//        response.sendRedirect("/login?expired=true");
    }
}
