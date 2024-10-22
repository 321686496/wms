package com.hongan.template.base.defender;

import com.hongan.template.base.service.IRedisValue;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public abstract class Defender {

    protected static String DEFENDER = "DEFENDER:";
    // 禁止时间指数的底, 单位：分钟
    protected static double BASE = 3;
    public IRedisValue<Integer> redisValue;

    // 错误的次数或者一个周期的次数
    public abstract int getSessionThreshold();

    public abstract int getIpThreshold();

    public abstract String getName();

    public abstract Matcher getMatcher();

    public abstract String getForbiddenKey();

    public abstract String getWarningKey();

    public abstract String getNotify(int minutes);

    public abstract int getForbiddenTime(int times, KeyType tp);

    // public 如果合法返回true，不合法返回false
    public abstract boolean checkValid(HttpServletRequest request, HttpServletResponse response);

    // public 如果合法返回true，不合法返回false
    public abstract boolean checkForbidden(int times, int threshold);

    // 通过错误次数生成警告秒数, 默认一天
    public int getWarningTime(int times) {
        return 60 * 60 * 24;
    }

    public String getDefenderSessionKey() {
        return DEFENDER + "SESSION:ID";
    }

    public int verifyIp(HttpServletRequest request) {
        String ip = getRealIp(request);
        if (ip == null) {
            log.error("Can not get the real ip addr");
            return 0;
        }
        return redisValue.getExpire(getForbiddenKey() + ip);
    }

    public int verifySession(HttpServletRequest request) {
        String sessionId = getSessionId(request);
        return redisValue.getExpire(getForbiddenKey() + sessionId);
    }

    // preCheck 不通过返回false, 通过返回true 直接返回403
    public String preCheck(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int ipForbidden = verifyIp(request);
        int sessionForbidden = verifySession(request);
        int forbidden = Math.max(ipForbidden, sessionForbidden);
        if (forbidden > 0) {
            return "DefenderForbidden: " + getNotify(forbidden);
        }
        return null;
    }


    // postCheck 如果checkValid 通过，不做任何操作返回，如果不通过则记录此次非法操作
    public void postCheck(HttpServletRequest request, HttpServletResponse response) {
        if (checkValid(request, response)) return;
        String sessionId = getSessionId(request);
        String ip = getRealIp(request);

        saveTimes(sessionId, getSessionThreshold(), KeyType.Session);
        saveTimes(ip, getIpThreshold(), KeyType.IP);
    }

    public void saveTimes(String key, int threshold, KeyType tp) {
        String warningKey = getWarningKey() + key;
        String forbiddenKey = getForbiddenKey() + key;
        Integer times = redisValue.getValue(warningKey);
        if (times == null || times <= 0) times = 1;
        else times++;

        redisValue.setValue(warningKey, times, getWarningTime(times));
        if (checkForbidden(times, threshold)) {
            int forbidden = getForbiddenTime(times, tp);
            redisValue.setValue(forbiddenKey, forbidden, forbidden * 60);
        }
    }


    public String getRealIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public String getSessionId(HttpServletRequest request) {
        String defenderKey = getDefenderSessionKey();
        HttpSession session = request.getSession();
        String sessionId = (String) session.getAttribute(defenderKey);
        if (sessionId == null) {
            sessionId = UUID.randomUUID().toString();
            session.setAttribute(defenderKey, sessionId);
        }
        return sessionId;
    }

    public enum KeyType {
        Session,
        IP
    }

}
