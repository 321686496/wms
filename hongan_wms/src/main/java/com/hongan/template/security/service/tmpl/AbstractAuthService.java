package com.hongan.template.security.service.tmpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hongan.template.base.entity.BaseError;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.base.utils.JacksonObject;
import com.hongan.template.security.service.IAuthService;
import com.hongan.template.security.common.SecurityParameters;
import com.hongan.template.security.common.StepAuthException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2020-03-09 16:07
 * @Description: AbstractSecurityService
 */


public abstract class AbstractAuthService {

    public abstract void verify(HttpServletRequest request) throws BaseException, StepAuthException;

    // 请求路径加方法名/test:post;
    public abstract String getMatcher();
    public abstract int getPriority();
    public boolean hasParameter() {
        return false;
    }

    public SecurityParameters getParameters() {
        return null;
    }

    // 泛型方法 解析inputStream 并处理错误
    protected <T> T readInputStream(HttpServletRequest request, Class<T> valueType) throws BaseException {
        ObjectMapper mapper = JacksonObject.getJSONMapper();

        try {
            ServletInputStream input = request.getInputStream();
            return mapper.readValue(input, valueType);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BaseException(BaseError.IOHttpStreamError);
        }
    }

    public int compareTo(IAuthService o) {
        return getPriority() - o.getPriority() ;
    }
}
