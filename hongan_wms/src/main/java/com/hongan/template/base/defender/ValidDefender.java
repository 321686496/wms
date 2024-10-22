package com.hongan.template.base.defender;

// 保护API免于暴力破解（非法调用触发forbidden）
public abstract class ValidDefender extends Defender {

    public String getForbiddenKey() {
        return DEFENDER + "VALID:FORBIDDEN:" + getName() + ":";
    }

    public String getWarningKey() {
        return DEFENDER + "VALID:WARNING:" + getName() + ":";
    }

    public boolean checkForbidden(int times, int threshold) {
        return times > 0 && times % threshold == 0;
    }

    // 通过错误次数确定禁止的分钟数, 默认是 BASE(3) 的 指数 (每输错几次就禁止一次)
    public int getForbiddenTime(int times, KeyType key) {
        switch (key) {
            case Session:
                return (int) Math.round(Math.pow(BASE, (times - getSessionThreshold())) / getSessionThreshold());
            case IP:
                return (int) Math.round(Math.pow(BASE, (times - getIpThreshold())) / getIpThreshold());
            default:
                return 0;
        }
    }

}
