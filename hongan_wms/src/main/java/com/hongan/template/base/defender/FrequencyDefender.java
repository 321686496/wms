package com.hongan.template.base.defender;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 保护API 被调用的频率 超过频率触发forbidden
public abstract class FrequencyDefender extends Defender {
    public static final String ONE_CIRCLE = DEFENDER + "ONE_CIRCLE:";

    public String getForbiddenKey() {
        return DEFENDER + "FREQUENCY:FORBIDDEN:" + getName() + ":";
    }

    public String getWarningKey() {
        return DEFENDER + "FREQUENCY:WARNING:" + getName() + ":";
    }

    public boolean checkForbidden(int times, int threshold) {
        return times >= 0;
    }

    // 一个周期的时长(单位 秒)
    public abstract int oneCircle();

    // 通过禁止的次数确定禁止的分钟数, 默认是 BASE(3) 的 指数 (每禁止一次就增加BASE倍)
    public int getForbiddenTime(int times, KeyType tp) {
        switch (tp) {
            case Session:
            case IP:
                return (int) Math.round(Math.pow(2, times - 1));
            default:
                return 0;
        }
    }

    // public 如果合法返回true，不合法返回false
    public boolean checkValid(HttpServletRequest request, HttpServletResponse response) {
        String ipKey = ONE_CIRCLE + getName() + ":" + getRealIp(request);
        String sessionKey = ONE_CIRCLE + getName() + ":" + getSessionId(request);
        return !(checkFrequencyThreshold(ipKey, getIpThreshold()) || checkFrequencyThreshold(sessionKey, getSessionThreshold()));
    }

    // 判断是否超过阈值
    private boolean checkFrequencyThreshold(String key, int threshold) {
        Integer times = redisValue.getValue(key);
        if (times == null || times <= 0) {
            redisValue.setValue(key, 1, oneCircle());
        } else if(times + 1 < threshold) {
            redisValue.incr(key);
        } else {
            return true;
        }
        return false;
    }

    @Override
    public int getWarningTime(int times) {
        return times * 60 * 60;
    }
}
