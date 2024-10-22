package com.hongan.template.external.enums;

public enum ContentType {
    Comment("2"),//评论
    Forum("3"),//论坛
    ;

    private String msg;

    ContentType(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return this.msg;
    }
}
