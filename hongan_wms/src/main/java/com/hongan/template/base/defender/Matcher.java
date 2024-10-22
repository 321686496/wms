package com.hongan.template.base.defender;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2020-04-08 10:37
 * @Description: Mather
 */

@Slf4j
public class Matcher {
    final String path;
    final String method;
    final boolean regex;

    public Matcher(String path, String method, boolean regex) {
        this.path = path;
        this.regex = regex;
        this.method = method == null ? null : method.toLowerCase();
    }

    public boolean match(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        // 检查请求路径，如果matcher 包含方法，也要检查方法，匹配不上直接放行
        if (!(this.method == null || this.method.equals(method.toLowerCase()))) {
            return false;
        }
        return regex ? Pattern.matches(path, uri) : path.equals(uri);
    }

    public String getPath() {
        return path;
    }

    public String getMethod() {
        return method == null ? null : method.toLowerCase();
    }

    public String toString() {
        return "Matcher{path: " + path + ", method: " + method +", regex: " + regex + "}";
    }

}
