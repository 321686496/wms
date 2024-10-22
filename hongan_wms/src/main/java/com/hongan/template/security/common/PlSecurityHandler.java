package com.hongan.template.security.common;

import com.alibaba.fastjson.JSONObject;
import com.hongan.template.base.entity.BaseError;
import com.hongan.template.base.security.ITokenUser;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2020-03-03 10:20
 * @Description: LogoutSuccessHandler
 */

@Slf4j
public class PlSecurityHandler {

    @Component
    public static class PlLoginSuccessHandler implements AuthenticationSuccessHandler {
        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth) throws IOException, ServletException {
            response.setContentType("application/json;charset=utf-8");
//            ObjectMapper jsonMapper = JacksonObject.getJSONMapper();
            ITokenUser user = (ITokenUser) auth.getPrincipal();
            String name = new String(user.getNickName().getBytes(), StandardCharsets.UTF_8);
            log.info("user: nickName:{}, phone:{}", name, user.getPhone());
            Map<String, Object> res = new HashMap<>();
            res.put("status", 0);
            Map<String, Object> data = new HashMap<>() {
            };
            data.put("nickName", name);
            data.put("phone", user.getPhone());
            data.put("role", user.getRole());
            data.put("roleKey", user.getRoleKey());
            data.put("name", user.getName());
            data.put("roleName", user.getRoleName());
            data.put("isInitial", user.getIsInitial());
            res.put("data", data);
            response.getWriter().write(JSONObject.toJSONString(res));
        }
    }

    @Component
    public static class PlLogoutSuccessHandler implements LogoutSuccessHandler {
        @Override
        public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            response.setContentType("application/json");
            response.getWriter().write("{\"status\": 0}");
        }
    }

    @Component
    public static class PlUnauthenticatedEntryPoint implements AuthenticationEntryPoint {

        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
            BaseError e;
            if (exception instanceof InsufficientAuthenticationException) {
                e = BaseError.AuthError;
            } else {
                e = BaseError.AccessForbidden;
            }
            restResponse(response, e);

//            if (isAjaxRequest(request)) {
//                BaseError e;
//                if (exception instanceof InsufficientAuthenticationException) {
//                    e = BaseError.AuthError;
//                } else {
//                    e = BaseError.AccessForbidden;
//                }
//                restResponse(response, e);
//            } else {
//                restResponse(response, BaseError.AuthError);
//                //无权限重定向
////                response.sendRedirect(PlSecurityConstConfig.Login);
//            }
        }
    }

    @Component
    public static class PlAccessDeniedHandler implements AccessDeniedHandler {

        @SneakyThrows
        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws IOException, ServletException {
//            throw new BaseException(BaseError.AccessForbidden);
            BaseError e = BaseError.AccessForbidden;
            restResponse(response, e);
//            权限注释代码
//            if (isAjaxRequest(request)) {
//                BaseError e = BaseError.AccessForbidden;
//                restResponse(response, e);
//            }
//            else {
//                response.sendRedirect(PlSecurityConstConfig.AccessDeny);
//            }
        }
    }

    @Component
    public static class PlLoginFailureHandler implements AuthenticationFailureHandler {
        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
            BaseError e;
            if (exception instanceof UsernameNotFoundException) {
                e = BaseError.DBNotFoundUser;
            } else if (exception instanceof InsufficientAuthenticationException) {
                e = BaseError.AccessForbidden;
            } else {
                e = BaseError.checkFromMessage(exception.getMessage());
            }
            request.setAttribute(PlSecurityConstConfig.AuthErrorKey, e.getMessage());
            restResponse(response, e);
        }
    }

    public static boolean isAjaxRequest(HttpServletRequest request) {
        String ajaxFlag = request.getHeader("X-Requested-With");
        return "XMLHttpRequest".equals(ajaxFlag);
    }

    private static void restResponse(HttpServletResponse response, BaseError e) throws IOException {
        response.setContentType("application/json");
        response.getWriter().format("{\"status\": %d,\"message\": \"%s\"}", e.getStatus(), e.getMessage());
    }

}
