package com.hongan.template.security.controller;

import com.hongan.template.base.entity.BaseError;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.security.common.PlSecurityConstConfig;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;


/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2019-12-28 09:24
 * @Description: ErrorController
 */
@Api(tags = "无需关注-错误页面跳转")
@Slf4j
@RestControllerAdvice
public class PlServletErrorController extends BasicErrorController {
    public PlServletErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes, new ErrorProperties());
    }

    @Override
    @RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        Map<String, Object> body = getErrorModelFromRequest(request);
        int statusCode = (int) body.get("status");
        log.error("Error model: {}", body);
        HttpStatus status = HttpStatus.resolve(statusCode);
        return new ResponseEntity<>(body, Objects.requireNonNull(status));
    }

    @Override
    @RequestMapping(produces = {MediaType.TEXT_HTML_VALUE})
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> body = getErrorModelFromRequest(request);
        int statusCode = (int) body.get("status");
        log.error("Error model: {}", body);
        ModelAndView mv = new ModelAndView();
        mv.addObject("model", body);
        switch (statusCode) {
            case 400:
                mv.setViewName("/admin/error/400");
                break;
            case 401:
                mv.setViewName("redirect:" + PlSecurityConstConfig.Login);
                break;
            case 403:
                mv.setViewName("redirect:" + PlSecurityConstConfig.AccessDeny);
                break;
            case 404:
                mv.setViewName("redirect:" + PlSecurityConstConfig.NotFound);
                break;
            default:
                mv.setViewName("/admin/error/errorHtml");
        }
        return mv;
    }

    private Map<String, Object> getErrorModelFromRequest(HttpServletRequest request) {
//        Integer statusCode = (Integer) request
//                .getAttribute("javax.servlet.error.status_code");
        Throwable throwable = (Throwable) request
                .getAttribute("javax.servlet.error.exception");
        String requestUri = (String) request
                .getAttribute("javax.servlet.error.request_uri");

        Map<String, Object> body = getErrorAttributes(request, ErrorAttributeOptions.defaults());
        body.put("url", requestUri);
        if (throwable != null) {
            BaseException baseException = BaseException.fromException(throwable);
            int statusCode = baseException.getStatus();
            statusCode = statusCode > 10000 ? statusCode / 100 : statusCode;
            body.put("message", baseException.getMessage());
            body.put("status", statusCode);
            body.put("additional", baseException.getAdditional());
            body.put("remark", baseException.getRemark());
        }
        body.putIfAbsent("status", 500);
        return body;
    }

    @Override
    public String getErrorPath() {
        return PlSecurityConstConfig.Error;
    }

    private BaseError checkCustomerError(String message) {
        if (message == null) return null;
        String tp = message.split(":")[0];
        return BaseError.checkFromMessage(tp);
    }

}
