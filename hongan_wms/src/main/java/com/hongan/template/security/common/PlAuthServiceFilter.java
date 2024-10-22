package com.hongan.template.security.common;

import com.hongan.template.base.entity.BaseException;
import com.hongan.template.security.service.IAuthService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2020-03-05 17:24
 * @Description: AuthServiceFilter
 */


@Slf4j
public class PlAuthServiceFilter extends OncePerRequestFilter {

    private final static Map<String, IAuthService> SecurityServiceMap = new HashMap<>();

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        try {
            PlHttpRequestWrapper requestWrapper = checkService(request);
            String uri = requestWrapper.getRequestURI();
            if (!PlSecurityConstConfig.NameLogin.equals(uri)) {
                requestWrapper.setRewrite(PlSecurityConstConfig.NameLogin);
            }
            filterChain.doFilter(requestWrapper, response);
        } catch (StepAuthException e) {
            dispatchStepException(request, response, e);
        } catch (BaseException e) {
            e.printStackTrace();
            dispatchBaseException(request, response, e);
        } catch (IOException | ServletException e) {
            e.printStackTrace();
            throw e;
        }

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String matchKey = request.getRequestURI() + ":" + request.getMethod();
        return !SecurityServiceMap.containsKey(matchKey.toLowerCase());
    }

    private PlHttpRequestWrapper checkService(final HttpServletRequest request) throws BaseException, StepAuthException {
        IAuthService service = matchService(request);
        PlHttpRequestWrapper wrapperRequest = new PlHttpRequestWrapper(request);
        service.verify(wrapperRequest);
        if (service.hasParameter()) {
            wrapperRequest.setParameters(service.getParameters());
        }
        return wrapperRequest;
    }

    public void setSecurityServices(Collection<IAuthService> securityServices) {
        for (IAuthService service : securityServices) {
            addSecurityService(service);
        }
    }

    public void addSecurityService(IAuthService service) {
        String matcher = service.getMatcher();
        Assert.isTrue(SecurityServiceMap.get(matcher) == null, "一个matcher规则只能对应一个Service,请检查配置");
        SecurityServiceMap.put(matcher, service);
    }

    private IAuthService matchService(final HttpServletRequest request) {
        String matchKey = request.getRequestURI() + ":" + request.getMethod();
        return SecurityServiceMap.get(matchKey.toLowerCase());
    }

    private void dispatchBaseException(HttpServletRequest request, HttpServletResponse response, BaseException e) throws IOException {
        request.setAttribute(PlSecurityConstConfig.AuthErrorKey, e.getMessage());
        response.setStatus(e.getStatus() / 100);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(e.toJSON());
    }

    private void dispatchStepException(HttpServletRequest request, HttpServletResponse response, StepAuthException e) throws IOException {
        response.setStatus(200);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(e.toJSON());
    }

    public void setContext(ApplicationContext context) {
        Map<String, IAuthService> authMap = context.getBeansOfType(IAuthService.class);
        setSecurityServices(authMap.values());
    }
}
