package com.hongan.template.base.utils;

/**
 * @Author: zyp
 * @Date: 2021/11/19 0019
 */
public class AssertUtils {
    public static void requireTrue(boolean value, String msg) {
        if (!value) {
            throw new RuntimeException(msg);
        }
    }
}
