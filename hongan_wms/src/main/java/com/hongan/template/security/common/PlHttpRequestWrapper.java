package com.hongan.template.security.common;

import com.hongan.template.base.entity.BaseError;
import com.hongan.template.base.entity.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2020-03-07 10:04
 * @Description: HttpServletRequestWrapper
 */


@Slf4j
public class PlHttpRequestWrapper extends HttpServletRequestWrapper {

    private Map<String, String[]> params = new HashMap<String, String[]>();
    private String rewrite;
    private final byte[] body;

    public PlHttpRequestWrapper(HttpServletRequest request) throws BaseException {
        super(request);
        this.params.putAll(request.getParameterMap());
        try {
            this.body = StreamUtils.copyToByteArray(request.getInputStream());
        } catch (IOException e) {
            throw new BaseException(BaseError.IOHttpStreamError);
        }
    }

    private HttpServletRequest _getHttpServletRequest() {
        return (HttpServletRequest) super.getRequest();
    }


    @Override
    public String getParameter(String name) {//重写getParameter，代表参数从当前类中的map获取
        String[] values = params.get(name);
        if (values == null || values.length == 0) {
            return null;
        }
        return values[0];
    }

    @Override
    public String[] getParameterValues(String name) {//同上
        return params.get(name);
    }


    @Override
    public String getRequestURI() {
        String requestURI = this._getHttpServletRequest().getRequestURI();
        return rewrite == null ? requestURI : rewrite;
    }

    @Override
    public String getContextPath() {
        return super.getContextPath();
    }

    @Override
    public String getServletPath() {
        return getRequestURI().substring(getContextPath().length());
    }

    @Override
    public StringBuffer getRequestURL() {
        return new StringBuffer(getScheme() + "://" + getServerName() + ":" + getServerPort() + getRequestURI());
    }

    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return inputStream.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }

            @Override
            public int read() throws IOException {
                return inputStream.read();
            }
        };
    }

    public void setRewrite(String rewrite) {
        this.rewrite = rewrite;
    }

    public void setParameters(SecurityParameters parameters) {
        this.addParameter("username", parameters.getUsername());
        this.addParameter("password", parameters.getPassword());
        if (!"false".equals(parameters.getRememberMe())) {
            this.addParameter("remember-me", "true");
        }
    }


    public void addParameter(String name, Object value) {//增加参数
        if (value != null) {
            if (value instanceof ArrayList) {
                String value1 = String.valueOf(value).substring(1, String.valueOf(value).length());
                value = value1.substring(0, value1.length() - 1);
                params.put(name, new String[]{(String) value});
            } else if (value instanceof String[]) {
                params.put(name, (String[]) value);
            } else if (value instanceof String) {
                params.put(name, new String[]{(String) value});
            } else {
                params.put(name, new String[]{String.valueOf(value)});
            }
        }
    }
}
