package com.hongan.template.external.enums;

/**
 * 短信通知包提醒信息
 */
public enum VoiceNotifyTemplate {


    ;

    private String remark;
    private String content;

    VoiceNotifyTemplate(String remark, String content) {
        this.remark = remark;
        this.content = content;
    }

    public String getRemark() {
        return remark;
    }
}
