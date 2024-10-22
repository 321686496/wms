package com.hongan.template.security.service;


import com.hongan.template.base.entity.BaseException;
import com.hongan.template.security.common.SecurityParameters;
import com.hongan.template.security.common.StepAuthException;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2020-03-07 10:14
 * @Description: ISecurityVerify
 */

public interface IAuthService extends Comparable<IAuthService> {
    // 安全验证，验证失败抛出异常
    void verify(HttpServletRequest request) throws BaseException, StepAuthException;
    // 获取Service 关联的URI:METHOD 字符串 用以匹配request
    String getMatcher();
    // 获取Service 优先级 数字越小，越靠前, 最小0
    int getPriority();
    boolean hasParameter();
    // 获取适配security接口的参数
    SecurityParameters getParameters();
}
