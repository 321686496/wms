package com.hongan.template.security.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.hongan.template.base.entity.BaseError;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.base.utils.BaseCrypto;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2020-03-09 15:38
 * @Description: SecurityParameters
 */

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SecurityParameters {
    public SecurityParameters(String username, String password, HttpServletRequest request, String path) throws BaseException {
        LoginUsername userObject = new LoginUsername(username, request, path);
        try {
            this.username = userObject.toJSON();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new BaseException(BaseError.JSONError);
        }
        this.password = BaseCrypto.hmac(password);
        this.rememberMe = "true";
    }

    public void setUsername(String username, HttpServletRequest request, String path) throws BaseException {
        LoginUsername userObject = new LoginUsername(username, request, path);
        try {
            this.username = userObject.toJSON();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new BaseException(BaseError.JSONError);
        }
    }

    public void setHmacPassword(String password) {
        try {
            this.password = BaseCrypto.hmac(password);
        } catch (BaseException e) {
            e.printStackTrace();
        }
    }

    private String username;
    private String password;
    private String rememberMe;

}
